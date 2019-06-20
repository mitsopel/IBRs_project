/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.json;

import gr.aueb.msc.cs.tims.ibrservice.index.*;
import gr.aueb.msc.cs.tims.ibrservice.core.IBR;
import java.util.List;
import net.openhft.lang.model.Byteable;
import net.openhft.lang.model.constraints.MaxSize;


public class JSONIBREntry {
    String ibr;
    List<JSONImageDescriptor> imgs;

    public JSONIBREntry(IBREntry entry) {
    }

    public String getIbr() {
        return ibr;
    }

    public void setIbr(String ibr) {
        this.ibr = ibr;
    }

    public List<JSONImageDescriptor> getImgs() {
        return imgs;
    }

    public void setImgs(List<JSONImageDescriptor> imgs) {
        this.imgs = imgs;
    }
    
}
