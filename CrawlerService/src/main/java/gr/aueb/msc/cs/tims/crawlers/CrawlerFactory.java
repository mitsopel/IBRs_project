/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.crawlers;

import org.openqa.selenium.WebDriver;
import gr.aueb.msc.cs.tims.crawlers.ImgurCrawler;


public class CrawlerFactory {
    //Possible supported image sites
    public enum ImageSources {
        IMGUR,
        REDDIT,
        FLICKR,
        GOOGLE
    }
            
    public static Crawler create(WebDriver driver, ImageSources source) {
                
        switch (source) {
            case IMGUR:
                return new ImgurCrawler(driver);
            case GOOGLE:
                return null;
            case REDDIT:
                return null;
            case FLICKR:
                return null;
        }
        
        return null;
    }
}
