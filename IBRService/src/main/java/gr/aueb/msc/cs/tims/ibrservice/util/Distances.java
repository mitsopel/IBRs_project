/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.util;

public class Distances {
    // pre calculate bitCounts fro very fast Hamming Distance of IBRs
    public static int bitCount[] = new int[256];
    static {
        for (int i = 0; i < 256; i++ ) {
            bitCount[i] = Integer.bitCount((int)(i << 1));
        }
    }
    
    public static int HammingDistance(byte[] l, byte[] r) {
        int dist = 0;
        int i, b;
        
        for (i = 0; i <l.length; i++) {
            b = ((int)l[i] ^ r[i]) & 0x000000ff;
            dist += bitCount[b];
            /*
            b = (byte) ((int)l[i] ^ r[i] & 0x000000ff);
            dist += ((b>>>3)&1)+((b>>>2)&1)+((b>>>1)&1)+(b&1);
            */
        }
        return dist;
    }
}