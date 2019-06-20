/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.queue;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptAppender;
import net.openhft.chronicle.ExcerptTailer;
import net.openhft.lang.model.DataValueClasses;
import org.apache.commons.io.FileUtils;


public class ImageQueue {
    Chronicle imageQueue;
    ExcerptTailer reader;
    ExcerptAppender appender;
    
    public void initialize(boolean clearDatabase) {
        try {
            File dbDir = new File("../ImageNetQueue");
            if (!dbDir.exists()) {

                dbDir.mkdir();
                String pathname = "../ImageNetQueue/IMAGE_URL_DB.dat";

                imageQueue = ChronicleQueueBuilder.indexed(pathname).build();
                appender = imageQueue.createAppender();
                reader = imageQueue.createTailer();
            } else {
                if ( clearDatabase )
                    FileUtils.cleanDirectory(dbDir);
                
                String pathname = "../ImageNetQueue/IMAGE_URL_DB.dat";

                imageQueue = ChronicleQueueBuilder.indexed(pathname).build();
                appender = imageQueue.createAppender();
                reader = imageQueue.createTailer();
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Init with custom DIR
    // This will be used mainly for the demo Database
    public void initialize(String dir, boolean clearDatabase) {
        try {
            File dbDir = new File("../"+dir);
            if (!dbDir.exists()) {

                dbDir.mkdir();
                String pathname = "../"+dir+"/IMAGE_URL_DB.dat";
                
                imageQueue = ChronicleQueueBuilder.indexed(pathname).build();
                appender = imageQueue.createAppender();
                reader = imageQueue.createTailer();
            } else {
                if ( clearDatabase )
                    FileUtils.cleanDirectory(dbDir);
                
                String pathname = "../"+dir+"/IMAGE_URL_DB.dat";

                imageQueue = ChronicleQueueBuilder.indexed(pathname).build();
                appender = imageQueue.createAppender();
                reader = imageQueue.createTailer();
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void add(String path) {      
        final ByteableImageEntry entry = DataValueClasses.newDirectInstance(ByteableImageEntry.class);
        
        appender.startExcerpt(entry.maxSize());
        
        // Timestamp is not used at all
        entry.bytes(appender, 0);
        entry.setPath(path);
        entry.setRead(false);
        entry.setTimestamp(System.currentTimeMillis());
                
        appender.position(entry.maxSize());
        
        appender.finish();
    }
    
    public ByteableImageEntry get() {
        //System.out.println("SIZE "+imageQueue.size());
        ByteableImageEntry ret = DataValueClasses.newDirectReference(ByteableImageEntry.class);
        
        // We need to periodically refresh the Reader/Tailer to keep up with
        // the updates of the Crawler
        long end = System.currentTimeMillis() + 2000;
        while (!reader.nextIndex() && (System.currentTimeMillis() < end) ) {
            try {
                reader = imageQueue.createTailer();
                end = System.currentTimeMillis() + 2000;
            } catch (IOException ex) {
                Logger.getLogger(ImageQueue.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ret.bytes(reader, 0);
        
        if ( ret.getRead() )
            return null;
        
        // Setting read Flag to TRUE ensures we dont rehash the image
        ret.setRead(true);
        
        reader.finish();
        
        return ret;
    }
    
    public void deinitialize() {
        
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            ExcerptTailer reader = imageQueue.createTailer();
            for (long i = 0; i < imageQueue.size(); i++) {
// While until there is a new Excerpt to read
                while (!reader.index(i));
// Read the objecy
                String ret = (String)reader.readObject();
                sb.append(ret+"\n");
// Make the reader ready for next read
                reader.finish();
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }
}
