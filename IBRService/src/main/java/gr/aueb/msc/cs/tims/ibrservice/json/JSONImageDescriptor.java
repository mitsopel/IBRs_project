/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.json;

import gr.aueb.msc.cs.tims.ibrservice.index.ImageDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class JSONImageDescriptor {
    String url;
    String type;
    int width;
    int height;
    List<String> tags;

    public JSONImageDescriptor(ImageDescriptor id) {
        this.width = id.getWidth();
        this.height = id.getHeight();
        this.type = id.getType();
        this.url = id.getUrl();
        Collections.copy(this.tags, id.getTags());
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
    
}
