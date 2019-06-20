/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.crawlerservice;

import gr.aueb.msc.cs.tims.crawlers.Crawler;
import gr.aueb.msc.cs.tims.crawlers.CrawlerFactory;
import gr.aueb.msc.cs.tims.queue.ByteableImageEntry;
import gr.aueb.msc.cs.tims.queue.ImageQueue;
import gr.aueb.msc.cs.tims.util.ImageUtils;
import ij.ImagePlus;
import net.openhft.lang.model.DataValueClasses;
import org.apache.log4j.BasicConfigurator;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrawlerService {
 
    // Swap between WebDrivers
    private static final WebDriver driver = new PhantomJSDriver();
    //private static final WebDriver driver = new FirefoxDriver();
    private static final int MAX_NUM_OF_BATCHES = 1;
    private static Logger Log = LoggerFactory.getLogger(CrawlerService.class);
    
    public static void main(String[] args) {
        Crawler crawler = CrawlerFactory.create(driver, CrawlerFactory.ImageSources.IMGUR);
        ImageQueue iq = new ImageQueue();
        
        iq.initialize(true);
        crawler.initialize();
        ImagePlus imp = crawler.fetchNext();
        int batchCounter = 0;
        while (batchCounter < MAX_NUM_OF_BATCHES) {
            batchCounter++;
            while ( imp != null) {
                String filePath = ImageUtils.saveImage(imp, crawler.getStoragePath());
                Log.info("Storing file : {}", filePath);
                iq.add(filePath);
                imp = crawler.fetchNext();
            }
        }
        crawler.deinitialize();
        
        driver.quit();
    }
    
    public static void runTests() {
        try {
            System.out.println("TESTING");
            ImageQueue iq = new ImageQueue();
            iq.initialize(true);
            System.out.println("TESTING PRODUCE");
            for (int i = 0; i < 100; i++) {

                Thread.sleep(400);
                System.out.println(i);
                //iq.add(new ImageEntry("a" + i));
            } 
            System.out.println("TESTING ACCESS");
            for (int i = 0; i < 100; i++) {

                System.out.println(iq.get());
            } 
            
            System.out.println(iq);
        } catch (InterruptedException ex) {
            //Logger.getLogger(CrawlerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("READING");
        ImageQueue iq = new ImageQueue();
        iq.initialize(true);
        System.out.println("READING AFTER INITIALIZE");
        for (int i = 0; i < 100; i++) {
            
            System.out.println(iq.get());
        }
        System.out.println("PRINTING AFTER READING");
        System.out.println(iq.get());
        System.out.println(iq); 
    }
}
