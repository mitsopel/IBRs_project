package gr.aueb.msc.cs.tims.ibrservice.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import gr.aueb.msc.cs.tims.ibrservice.index.IBRIndex;
import gr.aueb.msc.cs.tims.ibrservice.util.Distances;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.openhft.lang.io.Bytes;
import net.openhft.lang.io.serialization.BytesMarshallable;


/*
    Chronicle Map requires that values implement Serializable 
    or their custom interface BytesMarshallable
*/
public class IBR implements Serializable, Comparable<IBR>, Comparator<IBR> {
//public class IBR implements BytesMarshallable, Comparable<IBR>, Comparator<IBR> {

    // Constructor types
    public enum IBRStringRepresentation {
        BINARY, HEX
    }
    
    // Constants used in the IBR generation
    public static final int IBR_BYTE_LENGTH = 92;
    public static final int IBR_BIT_LENGTH = 8 * IBR_BYTE_LENGTH;
    public static final int IBR_HASH64_OFFSET = 0;
    public static final int IBR_LUM_HIGH_OFFSET = IBR_HASH64_OFFSET + 8;
    public static final int IBR_CHROMA_BLUE_OFFSET = IBR_LUM_HIGH_OFFSET + 12;
    public static final int IBR_CHROMA_RED_OFFSET = IBR_CHROMA_BLUE_OFFSET + 32;

    //IBR byte array
    byte[] ibr =  new byte[IBR_BYTE_LENGTH];
    {
        // init to all 0s (just in case)
        for (int i = 0; i < IBR_BYTE_LENGTH; i++ ) {
            ibr[i] = 0;
        }
    }
    
    // Simple hex value lookup
    private static final char[] hex_values = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    // Simple decimal value lookup
    private static final Map<Character, Integer> dec_values = new HashMap<>();
    static {
        for (int i = 0; i < hex_values.length; i++ )
            dec_values.put(hex_values[i], i);
    }
    
    // random IBR (useful for testing)
    public IBR() {
        int i;
        for(i = 0; i < ibr.length; i++) {
            //In java everything is signed
            //Move to a larger datatype and then back to preserve the sign bit
            ibr[i] = (byte) ( (int)IBRIndex.rnd.nextInt(255) & 0x000000ff);
        }
    }
    
    //IBR generation using an Integer
    public IBR(int initVal) {
        int i;
        for(i = 0; i < ibr.length; i++) {
            ibr[i] = (byte) ( (int)initVal & 0x000000ff);
        }
    }
    
    // We use BINARY and HEXADECIMAL representations of IBRs
    // This constructor handles both according to the String rep type
    public IBR(String s, IBRStringRepresentation sr) {
        if ( sr == IBRStringRepresentation.BINARY ) {
            int i;
            int div, mod;
            for (i = 0; i < s.length(); i++) {
                div = i / 8;
                mod = i % 8;
                int conv = Character.getNumericValue(s.charAt(i));
                ibr[div] = (byte) ((int) ibr[div] | (conv << mod));
            }
        } else if ( sr == IBRStringRepresentation.HEX ) {
            for (int i = 0; i < s.length(); i+=2) {
                ibr[i/2] = (byte) ((int) dec_values.get(s.charAt(i)));
                ibr[i/2] = (byte) (((int) ibr[i/2] << 4) | dec_values.get(s.charAt(i+1)));
            }
        }
    }
    
    //Probably not the fastest but a simple one
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IBR other = (IBR) obj;
        return this.hashCode() == other.hashCode();
    }
    
    @Override
    public int compareTo(IBR o) {
        // Return hamming distance
        return Distances.HammingDistance(this.getIbr(), o.getIbr());
    }
    
    // NEEDS TESTING
    @Override
    public int compare(IBR o1, IBR o2) {
        return o1.compareTo(o2);
    }
    
    public int getHammingDistance(IBR comp) {
        return this.compareTo(comp);
    }
            
    
    @Override
    public String toString() {
        int hi, lo, i;
        StringBuilder out = new StringBuilder();
        for (i = 0; i < IBR_BYTE_LENGTH; i++) {
            out.append("");
            hi = (int)ibr[i] & 0x0000000f;
            lo = ((int)ibr[i] & 0x000000f0) >>> 4;
            out.append(hex_values[lo]);
            out.append(hex_values[hi]);
        }

        return out.toString();
    }

    public byte[] getIbr() {
        return ibr;
    }
    
    // Override default serializer ( The GETTER above = getIbr() )
    @JsonProperty
    public String ibr() {
        return this.toString();
    }
    
    /*
    // NEEDS TESTING
    @Override
    public void readMarshallable(Bytes bytes) throws IllegalStateException {
        bytes.read(ibr);
    }
    
    // NEEDS TESTING
    @Override
    public void writeMarshallable(Bytes bytes) {
        bytes.write(this.getIbr());
    }
    */
    
}
