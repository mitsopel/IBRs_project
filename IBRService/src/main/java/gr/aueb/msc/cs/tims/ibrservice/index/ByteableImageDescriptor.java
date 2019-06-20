/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.index;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.openhft.lang.model.Byteable;
import net.openhft.lang.model.constraints.MaxSize;


public interface ByteableImageDescriptor extends Byteable {

    String getURL();
    void setURL(@MaxSize(256) String url);

    void setWidth(int w);
    int getWidth();

    void setHeight(int h);
    int getHeight();
    
    void setType(@MaxSize(10) String type);
    String getType();
    
    /*
    string base64 = Convert.ToBase64String(bytes);
    byte[] bytes = Convert.FromBase64String(base64);
    */
    void setTags(@MaxSize(4096) String serializedTags);
    String getTags();
    
}
