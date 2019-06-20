/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.index;

import java.util.List;
import net.openhft.lang.model.Byteable;
import net.openhft.lang.model.constraints.MaxSize;


public interface ByteableIBREntry extends Byteable {

    String getIBR();

    void setIBR(@MaxSize(192) String ibr);
    
    String getImages();

    void setImages(@MaxSize(65536) String imgs);
}
