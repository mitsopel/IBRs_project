/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.crawlers;

import gr.aueb.msc.cs.tims.crawlerservice.CrawlerService;
import ij.ImagePlus;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;


public abstract class Crawler {
    protected final WebDriver driver;
    protected static final org.slf4j.Logger Log = LoggerFactory.getLogger(Crawler.class);
    
    public Crawler(WebDriver driver) {
        this.driver = driver;
    }
    
    public Crawler() {
        this.driver = null;
    }
    
    // Initialize source specific crawler
    public abstract void initialize();
    // Fetch next image from source
    public abstract ImagePlus fetchNext();
    // Deinitialize source specific crawler
    public abstract void deinitialize();  
    // Return images path
    public abstract String getStoragePath();
}
