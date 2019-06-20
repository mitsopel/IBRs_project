/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.phashservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import gr.aueb.msc.cs.tims.crawlerservice.CrawlerService;
import gr.aueb.msc.cs.tims.ibrservice.core.IBR;
import gr.aueb.msc.cs.tims.ibrservice.core.IBR.IBRStringRepresentation;
import gr.aueb.msc.cs.tims.ibrservice.index.ByteableImageDescriptor;
import gr.aueb.msc.cs.tims.ibrservice.index.IBREntry;
import gr.aueb.msc.cs.tims.ibrservice.index.IBRIndex;
import gr.aueb.msc.cs.tims.ibrservice.index.ImageDescriptor;
import gr.aueb.msc.cs.tims.ibrservice.util.Distances;
import gr.aueb.msc.cs.tims.phashservice.utilities.DCTData;
import gr.aueb.msc.cs.tims.phashservice.utilities.Utilities;
import gr.aueb.msc.cs.tims.phashservice.utilities.YCbCrData;
import gr.aueb.msc.cs.tims.queue.ByteableImageEntry;
import gr.aueb.msc.cs.tims.queue.ImageQueue;
import ij.IJ;
import ij.ImagePlus;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.channels.Selector;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptAppender;
import net.openhft.lang.model.DataValueClasses;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.LoggerFactory;
import shaded.org.codehaus.plexus.util.StringUtils;

public class PHashService {
    private static org.slf4j.Logger Log = LoggerFactory.getLogger(PHashService.class);
    
    private static boolean POPULATE_WITH_LOCAL_DATA = true;
    
    // debug flags
    public static boolean DISPLAY_ORIGINAL_IMAGE = false;
    public static boolean PRINT_ORIGINAL_IMAGE_DATA = false;

    public static boolean DISPLAY_SCALED_IMAGE = false;
    public static boolean PRINT_SCALED_IMAGE_DATA = false;

    public static boolean DISPLAY_Y = false;
    public static boolean DISPLAY_Cb = false;
    public static boolean DISPLAY_Cr = false;

    public static boolean PRINT_RGB_TO_YCbCr = false;

    public static boolean PRINT_DCT_TABLE_Y = false;
    public static boolean PRINT_DCT_TABLE_Cb = false;
    public static boolean PRINT_DCT_TABLE_Cr = false;
    public static boolean PRINT_DCT_TABLE_DATA = false;

    public static boolean PRINT_YCbCr_DATA = false;
    public static boolean SHOW_YCbCr_IMAGES = false;
    public static boolean PRINT_CROPPED_BIT_ARRAYS = false;
    public static boolean CALCULATE_DCT_TIME = false;

    public static int RESIZE_DIMENSION = 32;

    public static int Y_LOW_CROP_SIZE = 8;
    public static int Y_HIGH_CROP_WIDTH = 8;
    public static int Y_HIGH_CROP_HEIGHT = 12;
    public static int Cb_Cr_CROP_SIZE_WIDTH = 16;
    public static int Cb_Cr_CROP_SIZE_HEIGHT = 18;

    // Simple terminator variable
    private static boolean alive = true;
    public static void setAlive(boolean val) { alive = val; }
    public static boolean isAlive() { return alive; }
    
    public static Map.Entry<IBR, ImageDescriptor> getIBRFromFile(String urlLocation) {
        // return the ibr
        int width, height;
        String type, url;
        
        
        long time = 0;
        if (CALCULATE_DCT_TIME) {
            time = System.currentTimeMillis();
            System.out.println("TIME STARTED");
        }
        
        // If there was a problem opening the file, last error message wont be null
        System.out.println("Opening "+urlLocation);
        ImagePlus imagePlus = new ImagePlus(urlLocation);
        if ( IJ.getErrorMessage() != null ) 
            return null;
        BufferedImage bImage = imagePlus.getBufferedImage();
        
        
        height =  bImage.getHeight();
        width = bImage.getWidth();
        url = urlLocation;
        int formatStart = urlLocation.indexOf(".");
        if ( formatStart < 0 )
            type = "Unknown";
        else {
            type = urlLocation.substring(formatStart+1).toUpperCase();
        }
        ImageDescriptor id = new ImageDescriptor(url, type, width, height);
        
        // debug methods
        showOriginalImage(bImage);
        printOriginalImageData(bImage);

        //resize image to RESIZE DIMENSION, default should be 128X128
        // use smaller number for faster results, but less precise
        bImage = Utilities.resizeImage(bImage, RESIZE_DIMENSION, RESIZE_DIMENSION);
        //debug methods
        showScaledImage(bImage);

        //convert Image to YCbCr
        YCbCrData ycbcrData = Utilities.rgbToYCbCr(bImage);

        //debug function
        printYCbCrData(ycbcrData);
        showYCbCrImages(ycbcrData);

        // now we have the 32X32 YCbCr data
        // generating the DCT coefficients
        DCTData dctData = Utilities.calculateDCTData(ycbcrData);

        // debug function
        printDCTData(dctData);

        // now we have our DCT values for Y, Cb, Cr
        // crop for the values we are interested in
        int[][] yLowCropped = Utilities.getSubMatrix(Y_LOW_CROP_SIZE, Y_LOW_CROP_SIZE, dctData.YDCT, false);
        int[][] yHighCropped = Utilities.getSubMatrix(Y_HIGH_CROP_WIDTH, Y_HIGH_CROP_HEIGHT, dctData.YDCT, true);
        int[][] cbCropped = Utilities.getSubMatrix(Cb_Cr_CROP_SIZE_WIDTH, Cb_Cr_CROP_SIZE_HEIGHT, dctData.CbDCT, false);
        int[][] crCropped = Utilities.getSubMatrix(Cb_Cr_CROP_SIZE_WIDTH, Cb_Cr_CROP_SIZE_HEIGHT, dctData.CrDCT, false);

        
        if(PRINT_CROPPED_BIT_ARRAYS) {
            System.out.println("");
            for(int i = 0; i< yLowCropped.length; i++) {
                for(int j=0; j<yLowCropped[0].length;j++) {
                    System.out.print(" " + yLowCropped[i][j]);
                }
            }
            System.out.println("");
            for(int i = 0; i< yHighCropped.length; i++) {
                for(int j=0; j<yHighCropped[0].length;j++) {
                    System.out.print(" " + yHighCropped[i][j]);
                }
            }
            System.out.println("");
            for(int i = 0; i< cbCropped.length; i++) {
                for(int j=0; j<cbCropped[0].length;j++) {
                    System.out.print(" " + cbCropped[i][j]);
                }
            }
            System.out.println("");
            for(int i = 0; i< crCropped.length; i++) {
                for(int j=0; j<crCropped[0].length;j++) {
                    System.out.print(" " + crCropped[i][j]);
                }
            }
            System.out.println("");
        }
        
        // now combine them into bytes
        String yLowHash = Utilities.getCombinedBytes(yLowCropped);
        String yHighHash = Utilities.getCombinedBytes(yHighCropped);
        String cbHash = Utilities.getCombinedBytes(cbCropped);
        String crHash = Utilities.getCombinedBytes(crCropped);
        
        if(PRINT_CROPPED_BIT_ARRAYS) {
            System.out.println("yLowBytes = " + yLowHash);
            System.out.println("yHighBytes = " + yHighHash);
            System.out.println("cbHash = " + cbHash);
            System.out.println("crHash = " + crHash);
        }
        
        // now combine the 4 components into one IBR 
        // and return
        String IBRString = yLowHash + yHighHash + cbHash + crHash;
        
        if (CALCULATE_DCT_TIME) {
            System.out.println("TIME ENDED: " + ((System.currentTimeMillis() - time) / 1000.0) + " seconds");
        }
        
        IBR ibr = new IBR(IBRString, IBRStringRepresentation.BINARY);
        String br = ibr.toString();
        ibr = new IBR(br, IBRStringRepresentation.HEX);
        //System.out.println("New "+ibr);
        //System.out.println("Old "+br);
        // return entry with IBR and ImageDescriptor
        return new AbstractMap.SimpleImmutableEntry<>(ibr, id);
    }

    public static void main(String args[]) throws IOException {
        // Server using a socket selector
        Selector sel = Selector.open();
        Thread server_t = new Thread(new PHashServer(sel));
        server_t.start();        
        
        // Console for termination
        Thread console_t = new Thread(new ServiceConsole());
        console_t.start();  
        
        System.out.println("Server running");
        System.out.println("Console running");
           
        if ( POPULATE_WITH_LOCAL_DATA )
            populateLocalDB();

        while ( isAlive() );
        
        if ( server_t.isAlive() )
            server_t.interrupt();
        
        if ( console_t.isAlive() )
            console_t.interrupt();
        
        return;
        
        /*
        ByteableImageEntry ie;
        while( isAlive() )  {
            ie = imQ.get();
            Log.info("Path {}", ie.getPath() + " at " + ie.getTimestamp() + " read " + ie.getRead());
            urlString = ie.getPath();
            String urlStringNormalized = StringUtils.replace(urlString, " ", "\\ ");
            System.out.println("String "+ urlStringNormalized);
            Map.Entry<IBR, ImageDescriptor> entry = getIBRFromFile(urlString);
            Log.info("\nIBR ------ {}", entry.getKey() +"\n" +  entry.getValue());
            ibrIdx.add(entry);
            //System.out.print("\n\n\n\n\n");
            ibrIdx.get(entry.getKey());
        }
        */
    }

    private static void populateLocalDB () {
        try {
            String urlString;

            File dir = new File("../ViewerService/public_html/img/LocalImageDB");
            FileUtils.forceDelete(new File("../LocalImageDB/IMAGE_URL_DB.dat.data"));
            FileUtils.forceDelete(new File("../LocalImageDB/IMAGE_URL_DB.dat.index"));
            
            ImageQueue imQ = new ImageQueue();
            imQ.initialize(true);
            ImageQueue imQLocal = new ImageQueue();
            imQLocal.initialize("LocalImageDB", false);
            IBRIndex ibrIdx = new IBRIndex();
            ibrIdx.initialize(true);
            
            List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            
            for ( int i = 0; i < files.size(); i++ ) {
                File f = files.get(i);
                System.out.println(f.getCanonicalPath());
                imQLocal.add(f.getCanonicalPath());
            }
            
            List<Map.Entry<IBR, ImageDescriptor>> entries = new ArrayList<>();
            ByteableImageEntry ie;
            int count = 0;
            while(count++ < 3 || isAlive() ) {
                ie = imQLocal.get();
                if ( ie == null )
                    continue;
                Log.info("Path {}", ie.getPath() + " at " + ie.getTimestamp() + " read " + ie.getRead());
                urlString = ie.getPath();
                Map.Entry<IBR, ImageDescriptor> entry = getIBRFromFile(urlString);
                
                if ( entry == null )
                    continue;
                
                entries.add(entry);
                //Log.info("\nIBR ------ {}", entry.getKey() +"\n" +  entry.getValue());
                ibrIdx.add(entry);
                //System.out.print("\n\n\n\n\n");
                //ibrIdx.get(entry.getKey());
            }
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            for (Map.Entry<IBR, ImageDescriptor> entry : entries) {
                IBREntry maxEntry = ibrIdx.get(entry.getKey());
                System.out.println("\n\nEntry Size : "+maxEntry.getImgs().size());
                System.out.println("Entry : "+maxEntry.getImgs());
                System.out.println(mapper.writeValueAsString(maxEntry));
            }
        } catch (IOException ex) {
            Logger.getLogger(PHashService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void showOriginalImage(BufferedImage im) {
        if (DISPLAY_ORIGINAL_IMAGE) {
            new ImagePlus("Original Image", im).show();
        }
    }

    private static void printOriginalImageData(BufferedImage im) {
        if (PRINT_ORIGINAL_IMAGE_DATA) {
            System.out.println("image name: " + im.toString()
                    + " <<width:" + im.getWidth()
                    + ">> <<height:" + im.getHeight() + ">>");
        }
    }

    private static void showScaledImage(BufferedImage im) {
        if (DISPLAY_SCALED_IMAGE) {
            new ImagePlus("Scaled Image", im).show();
        }
    }

    private static void printYCbCrData(YCbCrData data) {
        if (PRINT_YCbCr_DATA) {
            data.print();
        }
    }

    private static void showYCbCrImages(YCbCrData data) {
        //to be filled
    }

    private static void printDCTData(DCTData data) {
        if (PRINT_DCT_TABLE_DATA) {
            data.print();
        }
    }
}
