/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.index;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ImageDescriptor implements Serializable {
    String url;
    String type;
    int width;
    int height;
    List<String> tags;

    public ImageDescriptor(String url, String type, int width, int height) {
        this.url = url;
        this.type = type;
        this.width = width;
        this.height = height;
        this.tags = new ArrayList<>();
        final Random rnd = new Random(System.currentTimeMillis());
        final int numOfTags = rnd.nextInt(10);
        for ( int i = 0; i < numOfTags; i++ ) {
             this.tags.add("tag"+i);
        }
    }

    public ImageDescriptor() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nImage : " + this.url);
        sb.append("\nWidth : " + this.width);
        sb.append("\nHeight : " + this.height);
        sb.append("\nType : " + this.type);
        sb.append("\nTags : " + this.tags);
        
        return sb.toString();
    }
}
