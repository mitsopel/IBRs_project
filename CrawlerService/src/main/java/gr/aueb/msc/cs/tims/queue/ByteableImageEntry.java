/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.queue;

import net.openhft.lang.model.Byteable;
import net.openhft.lang.model.constraints.MaxSize;


public interface ByteableImageEntry extends Byteable {
    String getPath();
    void setPath(@MaxSize(0x100) String path);

    void setRead(boolean read);
    boolean getRead();

    void setTimestamp(long timestamp);
    long getTimestamp();
}