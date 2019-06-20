/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.crawlers;

import ij.ImagePlus;
import ij.io.FileInfo;
import ij.io.Opener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


public class ImgurCrawler extends Crawler {

    private static final String IMGUR_IMAGES_PATH = "../ImgurImages";

    private List<String> imageURLs;
    private int nextImgIndex;
    private int nextImgListIndex;
    private WebElement element = null;
    
    public ImgurCrawler(WebDriver driver) {
        super(driver);
        imageURLs = new ArrayList<String>();
        nextImgIndex = 0;
    }

    public ImgurCrawler() {
        super(null);
        imageURLs = new ArrayList<String>();
        nextImgIndex = 0;
    }

    private void nextBatch(int lastIdx) {
        long end;
        int prev_size = -1;
        int current_index = lastIdx;
        imageURLs.clear();
        
        element = getStaleElemByClass("imagelist-loader");
        List<WebElement> resultsDiv = null;
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        while (true) {
            //System.out.println("Still going " + prev_size + "    " + imageURLs.size() );
            if (prev_size < imageURLs.size() ) {
                boolean foundNew = true;
                prev_size = imageURLs.size(); 
                end = System.currentTimeMillis() + 10000;
                while (System.currentTimeMillis() < end) {
                    resultsDiv = driver.findElements(By.className("cards"));

                    // If results have been returned, the images are displayed.
                    if (resultsDiv.size() > current_index) {
                        break;
                    }
                }
                //System.out.println("RDIV "+resultsDiv.size()+ " + " + current_index );
                if (resultsDiv.size() == current_index)
                    continue;
                
                List<WebElement> images = resultsDiv.get(current_index++).findElements(By.className("post"));

                for (WebElement image : images) {
                    WebElement imageLink = image.findElement(By.tagName("a"));
                    imageURLs.add(imageLink.getAttribute("href"));
                }
                for (String uri : imageURLs) {
                    //System.out.println(uri);
                }
                Set<String> map = new HashSet<String>();
                for (String i : imageURLs) {
                    map.add(i);
                }
                //System.out.println("In map" + map.size());
                //System.out.println(imageURLs.size());
                Log.info("Feching next {} images", imageURLs.size() - prev_size);
                element = driver.findElement(By.className("imagelist-loader"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            } else {
                end = System.currentTimeMillis() + 10000;
                while (System.currentTimeMillis() < end) {
                    //System.out.println("Load More");
                    element = driver.findElement(By.id("load-more"));
                    // If results have been returned, the images are displayed.
                    if ( element.isDisplayed() ) {
                        //System.out.println("found");
                        new Actions(driver).moveToElement(element).click().perform();
                        break;
                    }
                }
                
                nextImgListIndex = current_index;
                
                break;
            }
        }
    }
    
    private WebElement getStaleElemByClass(String htmlClass) {
        try {
            return driver.findElement(By.className(htmlClass));
        } catch (StaleElementReferenceException e) {
            System.out.println("Attempting to recover from StaleElementReferenceException ...");
            return getStaleElemByClass(htmlClass);
        }
    }

    @Override
    public void initialize() {
        // Driver initialize for imgur and get first batch
        driver.get("http://imgur.com/");
        nextBatch(nextImgListIndex);
    }

    @Override
    public ImagePlus fetchNext() {
        boolean imageFound = false;
        ImagePlus imp = null;
        FileInfo fi = null;
        while (!imageFound && (nextImgIndex < imageURLs.size())) {
            try {
                String url = this.imageURLs.get(nextImgIndex);
                nextImgIndex++;
                //System.out.println("Fetching " + url);
                driver.get(url);
                long end = System.currentTimeMillis() + 10000;
                WebElement resultsDiv = null;
                while (System.currentTimeMillis() < end) {
                    resultsDiv = driver.findElement(By.id("image"));
                    //System.out.println("Fetching 4 " + url);
                    // If results have been returned, the results are displayed in a drop down.
                    if (resultsDiv.isDisplayed()) {
                        break;
                    }
                }
                //System.out.println("Fetching 3 " + url);
                WebElement images = resultsDiv.findElement(By.tagName("img"));
                //System.out.println("Fetching 2 " + url);
                Opener opener = new Opener();
                imp = opener.openImage(images.getAttribute("src"));
                fi = new FileInfo();
                fi.url = images.getAttribute("src");
                imp.setFileInfo(fi);
                //System.out.println("Width : " + imp.getWidth());
                //System.out.println("Width : " + imp.getHeight());
                //System.out.println("URL : " + imp.getFileInfo().url);

                imageFound = true;
            } catch (org.openqa.selenium.NoSuchElementException ex) {
                //System.out.println("eskase");
                imageFound = false;
                //ex.printStackTrace();
            }
        }
        
        // Reached end of automatic reload (Fetch next batch)
        if (imp == null) {
            this.nextBatch(nextImgListIndex);
        }
        
        return imp;
    }

    @Override
    public void deinitialize() {
        if (imageURLs != null) {
            imageURLs.clear();
        }
    }

    @Override
    public String getStoragePath() {
        return IMGUR_IMAGES_PATH;
    }

}
