package gr.aueb.msc.cs.tims.crawlers;

import java.util.ArrayList;
import java.util.List;

import ij.ImagePlus;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class NineGagCrawler extends Crawler {
	private List<String> imageUrls;
	
	public NineGagCrawler(WebDriver driver) {
        super(driver);
        this.imageUrls = new ArrayList<String>();
    }

    public NineGagCrawler() {
        super(null);
        this.imageUrls = new ArrayList<String>();
    }

	@Override
	public void initialize() {
		driver.get("http://9gag.com/");
		
		// Images in 9gag front page are located as :
		// div.main-wrap article a img.badge-item-img
		List<WebElement> imageElements = driver.findElements(By.cssSelector("div.main-wrap article a img.badge-item-img"));
		
		for(WebElement imgElm:imageElements) {
			this.imageUrls.add(imgElm.getAttribute("src"));
		}
		
		printImgURLList();
	}

    /**
     *
     */
    @Override
	public ImagePlus fetchNext() {
		// TODO Auto-generated method stub
            return null;
	}

	@Override
	public void deinitialize() {
		// TODO Auto-generated method stub

	}

	public void printImgURLList() {
		for(int i=1;i<this.imageUrls.size(); i++) {
			System.out.println(this.imageUrls.get(i));
		}
	}
	/*
	public static void main(String[] args) {
		//for test purposes only
		WebDriver driver = new FirefoxDriver();
		NineGagCrawler nineGagCrawler = new NineGagCrawler(driver);
		nineGagCrawler.initialize();
	}
        */

    @Override
    public String getStoragePath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
