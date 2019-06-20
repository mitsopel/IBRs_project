/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.phashservice;

import gr.aueb.msc.cs.tims.ibrservice.core.IBR;
import gr.aueb.msc.cs.tims.ibrservice.index.ImageDescriptor;
import static gr.aueb.msc.cs.tims.phashservice.PHashService.getIBRFromFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

public class ImageHasher implements Runnable {
    private static org.slf4j.Logger Log = LoggerFactory.getLogger(PHashService.class);
    String url;
    SocketChannel sc;
    
    public ImageHasher(SocketChannel sc, String url) {
        this.url = url;
        this.sc = sc;
    }
    
    
    @Override
    public void run() {
        try {
            System.out.println("URL over the net "+url);
            Map.Entry<IBR, ImageDescriptor> entry = getIBRFromFile(url);
            Log.info("\nIBR ------ {}", entry.getKey() +"\n" +  entry.getValue());
            // Hash Image then close socket channel
            ByteBuffer bb = ByteBuffer.allocate(entry.getKey().toString().length());
            bb.put(entry.getKey().toString().getBytes());
            bb.rewind();
            //bb.put(entry.getKey().getIbr());
            sc.write(bb);
            System.out.println("IBR "+entry.getKey().toString());
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(ImageHasher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
