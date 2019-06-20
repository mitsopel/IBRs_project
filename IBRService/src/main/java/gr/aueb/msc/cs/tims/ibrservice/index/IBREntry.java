/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.index;

import gr.aueb.msc.cs.tims.ibrservice.core.IBR;
import java.util.Collections;
import java.util.List;
import net.openhft.lang.model.Byteable;
import net.openhft.lang.model.constraints.MaxSize;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.SerializationUtils;


public class IBREntry {
    IBR ibr;
    List<ImageDescriptor> imgs;

    public IBREntry(ByteableIBREntry entry) {
        ibr = new IBR(entry.getIBR(), IBR.IBRStringRepresentation.HEX);
        imgs = (List<ImageDescriptor>)SerializationUtils.deserialize(Base64.decodeBase64(entry.getImages()));
        //System.out.println(imgs);
        //Collections.copy(imgs, lstImages);
    }

    public IBR getIbr() {
        return ibr;
    }

    public void setIbr(IBR ibr) {
        this.ibr = ibr;
    }

    public List<ImageDescriptor> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImageDescriptor> imgs) {
        this.imgs = imgs;
    }
    
}
