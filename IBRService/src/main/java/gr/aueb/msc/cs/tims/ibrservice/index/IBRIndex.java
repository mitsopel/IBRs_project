/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.index;

import gr.aueb.msc.cs.tims.ibrservice.core.IBR;
import gr.aueb.msc.cs.tims.ibrservice.core.IBR.IBRStringRepresentation;
import gr.aueb.msc.cs.tims.ibrservice.util.Distances;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import net.openhft.chronicle.map.ReadContext;
import net.openhft.chronicle.map.WriteContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.SerializationUtils;
import shaded.org.apache.commons.io.FileUtils;


public class IBRIndex {

    //Simple random generator for random IBRs and Hash functions

    public static final Random rnd = new Random((new Date()).getTime());
    public static final int NUMBER_OF_FUNCTIONS = 20;
    public static final int NUMBER_OF_SAMPLES = 20;
    public static final int HAMMING_DISTANCE_THRESHOLD = 100;

    List< ChronicleMap<Integer, ByteableIBREntry>> ibrDB;

    public IBRIndex() {
        this.ibrDB = new ArrayList<>(NUMBER_OF_FUNCTIONS);
    }

    public void printHashes(IBR ibr) {
        for (LSH ls : hashers) {
            System.out.println("Hash : " + ls.hash(ibr.getIbr()));
        }
    }

    // Prototype implementation where IBRs map to simple Strings
    // Switch to image locations and metadata as soon as the API is ready
    public void add(Map.Entry<IBR, ImageDescriptor> entry) {
        IBR ref = entry.getKey();
        ImageDescriptor val = entry.getValue();
        int matches = 0;
        for (LSH ls : hashers) {
            int hashValue = ls.hash(ref.getIbr());
            int mapIdx = hashValue % 20;
            ChronicleMap<Integer, ByteableIBREntry> map = ibrDB.get(mapIdx);
            //System.out.println(hashValue+ " at Map : " + map.size());
            // Chronicle maps advice this kind of data handling
            ByteableIBREntry using = map.newValueInstance();
            ByteableIBREntry bond = map.acquireUsing(hashValue, using);
            //System.out.println("Bond " + bond);

            try (WriteContext<?, ByteableIBREntry> context = map.acquireUsingLocked(hashValue, bond)) {
                assert bond == context.value();
                String serImages = bond.getImages();
                String ibr = bond.getIBR();
                IBR retIBR = new IBR(ibr, IBRStringRepresentation.HEX);
                //System.out.println("Distance between\n"+retIBR +"\n"+ ref);
                //System.out.println("Distance : "+Distances.HammingDistance(retIBR.getIbr(), ref.getIbr()));
                //System.out.println("IBR 1 : "+retIBR);
                //System.out.println("IBR 2 : "+ref);
                if (ibr.equals("")) {
                    matches++;
                    bond.setIBR(ref.toString());
                    //System.out.println(ref.toString());
                    //System.out.println(bond.getIBR());
                    List<ImageDescriptor> lstImages = new ArrayList<>();
                    lstImages.add(val);
                    bond.setImages(Base64.encodeBase64String(SerializationUtils.serialize((Serializable) lstImages)));
                } else {
                    // Reject those that are above the threshold and EXACT Matches
                    // * EXACT matches are rejected to avoid exact duplicates (for the demo mostly) 
                    int hamDist = Distances.HammingDistance(retIBR.getIbr(), ref.getIbr());
                    if ( ( hamDist > HAMMING_DISTANCE_THRESHOLD ) ) {
                        continue;
                    }
                            
                    matches++;
                    List<ImageDescriptor> lstImages = (List<ImageDescriptor>) SerializationUtils.deserialize(Base64.decodeBase64(bond.getImages()));
                    
                    // Testing Java 8 lambdas for simple find
                    // If the url is the same skip the image
                    Optional<ImageDescriptor> value = lstImages
                                                        .stream()
                                                        .filter(img -> ( img.url.equalsIgnoreCase(val.url) == true ) )
                                                        .findFirst();
                    
                    if ( !value.isPresent() ) {
                        System.out.println(val.url);
                        lstImages.add(val);
                    } else {
                        System.out.println(value.get().url);
                        continue;
                    }
                        
                    bond.setImages(Base64.encodeBase64String(SerializationUtils.serialize((Serializable) lstImages)));
                    //System.out.println("Image List [0] : " + lstImages.get(0));
                }
            }
        }

        System.out.println("Found in " + matches + " buckets");
    }

    // Will be returning a list of image locations and metadata
    //public List<IBREntry> get(IBR ref) {
    public IBREntry get(IBR ref) {
        List<IBREntry> lst = new ArrayList<>();
        IBREntry entry = null;
        for (LSH ls : hashers) {
            int hashValue = ls.hash(ref.getIbr());
            int mapIdx = hashValue % 20;
            ChronicleMap<Integer, ByteableIBREntry> map = ibrDB.get(mapIdx);

            ByteableIBREntry using = map.newValueInstance();
            ByteableIBREntry bond = map.getUsing(hashValue, using);
            if (bond != null) {
                try (ReadContext<?, ByteableIBREntry> context = map.getUsingLocked(hashValue, bond)) {
                    assert bond == context.value();
                    String serImages = bond.getImages();
                    String ibr = bond.getIBR();
                    IBR retIBR = new IBR(ibr, IBRStringRepresentation.HEX);

                    int hamDist = Distances.HammingDistance(retIBR.getIbr(), ref.getIbr());
                    if (hamDist > HAMMING_DISTANCE_THRESHOLD) {
                        continue;
                    }

                    entry = new IBREntry(context.value());
                    // System.out.println("Got " + ibr);

                    lst.add(entry);
                }
            }
            //l.add(map.get(ref));
        }

        // Locate entry with most ImageDescriptors
        IBREntry maxEntry = lst.get(0);
        for (IBREntry e : lst) {
            if (e.getImgs().size() > maxEntry.getImgs().size()) {
                maxEntry = e;
            }
        }

        return maxEntry;
    }

    public void initialize(boolean clearDirectory) {
        try {
            // Create directory IF it does not exist
            File dbDir = new File("../ImageNetDB");
            if (!dbDir.exists()) {
                dbDir.mkdir();
                for (int i = 0; i < NUMBER_OF_FUNCTIONS; i++) {
                    String pathname = "../ImageNetDB/IBR_DB_" + i + ".dat";

                    File file = new File(pathname);

                    ChronicleMapBuilder<Integer, ByteableIBREntry> builder
                            = ChronicleMapBuilder.of(Integer.class, ByteableIBREntry.class)
                            .entries(1000);
                    ChronicleMap<Integer, ByteableIBREntry> map = builder.createPersistedTo(file);
                    ibrDB.add(map);
                }
            } else {
                if (clearDirectory) {
                    FileUtils.cleanDirectory(dbDir);
                }

                for (int i = 0; i < NUMBER_OF_FUNCTIONS; i++) {
                    String pathname = "../ImageNetDB/IBR_DB_" + i + ".dat";

                    File file = new File(pathname);

                    ChronicleMapBuilder<Integer, ByteableIBREntry> builder
                            = ChronicleMapBuilder.of(Integer.class, ByteableIBREntry.class)
                            .entries(1000);
                    ChronicleMap<Integer, ByteableIBREntry> map = builder.createPersistedTo(file);
                    ibrDB.add(map);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deinitialize() {
    }

    public LSH[] getHashers() {
        return hashers;
    }

    public interface LSH {

        int hash(byte[] hash64);
    }

    class Hash0 {

        public int hash0(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[2] >>> 4) & 1) != 0));
            bs.set(1, (((hash64[1] >>> 3) & 1) != 0));
            bs.set(2, (((hash64[7] >>> 2) & 1) != 0));
            bs.set(3, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(4, (((hash64[1] >>> 0) & 1) != 0));
            bs.set(5, (((hash64[1] >>> 4) & 1) != 0));
            bs.set(6, (((hash64[2] >>> 0) & 1) != 0));
            bs.set(7, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(8, (((hash64[1] >>> 7) & 1) != 0));
            bs.set(9, (((hash64[3] >>> 6) & 1) != 0));
            bs.set(10, (((hash64[3] >>> 7) & 1) != 0));
            bs.set(11, (((hash64[6] >>> 1) & 1) != 0));
            bs.set(12, (((hash64[5] >>> 5) & 1) != 0));
            bs.set(13, (((hash64[0] >>> 6) & 1) != 0));
            bs.set(14, (((hash64[3] >>> 0) & 1) != 0));
            bs.set(15, (((hash64[4] >>> 5) & 1) != 0));
            bs.set(16, (((hash64[3] >>> 3) & 1) != 0));
            bs.set(17, (((hash64[6] >>> 3) & 1) != 0));
            bs.set(18, (((hash64[4] >>> 3) & 1) != 0));
            bs.set(19, (((hash64[5] >>> 0) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash1 {

        public int hash1(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[4] >>> 0) & 1) != 0));
            bs.set(1, (((hash64[1] >>> 2) & 1) != 0));
            bs.set(2, (((hash64[0] >>> 1) & 1) != 0));
            bs.set(3, (((hash64[0] >>> 2) & 1) != 0));
            bs.set(4, (((hash64[5] >>> 1) & 1) != 0));
            bs.set(5, (((hash64[6] >>> 5) & 1) != 0));
            bs.set(6, (((hash64[3] >>> 1) & 1) != 0));
            bs.set(7, (((hash64[2] >>> 6) & 1) != 0));
            bs.set(8, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(9, (((hash64[3] >>> 7) & 1) != 0));
            bs.set(10, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(11, (((hash64[6] >>> 7) & 1) != 0));
            bs.set(12, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(13, (((hash64[6] >>> 0) & 1) != 0));
            bs.set(14, (((hash64[3] >>> 5) & 1) != 0));
            bs.set(15, (((hash64[5] >>> 3) & 1) != 0));
            bs.set(16, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(17, (((hash64[2] >>> 2) & 1) != 0));
            bs.set(18, (((hash64[4] >>> 1) & 1) != 0));
            bs.set(19, (((hash64[5] >>> 2) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash2 {

        public int hash2(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[6] >>> 3) & 1) != 0));
            bs.set(1, (((hash64[1] >>> 7) & 1) != 0));
            bs.set(2, (((hash64[3] >>> 2) & 1) != 0));
            bs.set(3, (((hash64[7] >>> 4) & 1) != 0));
            bs.set(4, (((hash64[1] >>> 2) & 1) != 0));
            bs.set(5, (((hash64[6] >>> 2) & 1) != 0));
            bs.set(6, (((hash64[0] >>> 4) & 1) != 0));
            bs.set(7, (((hash64[3] >>> 5) & 1) != 0));
            bs.set(8, (((hash64[3] >>> 0) & 1) != 0));
            bs.set(9, (((hash64[3] >>> 3) & 1) != 0));
            bs.set(10, (((hash64[6] >>> 4) & 1) != 0));
            bs.set(11, (((hash64[6] >>> 1) & 1) != 0));
            bs.set(12, (((hash64[4] >>> 2) & 1) != 0));
            bs.set(13, (((hash64[5] >>> 2) & 1) != 0));
            bs.set(14, (((hash64[7] >>> 3) & 1) != 0));
            bs.set(15, (((hash64[7] >>> 2) & 1) != 0));
            bs.set(16, (((hash64[6] >>> 7) & 1) != 0));
            bs.set(17, (((hash64[4] >>> 0) & 1) != 0));
            bs.set(18, (((hash64[2] >>> 2) & 1) != 0));
            bs.set(19, (((hash64[6] >>> 0) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash3 {

        public int hash3(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[4] >>> 3) & 1) != 0));
            bs.set(1, (((hash64[6] >>> 6) & 1) != 0));
            bs.set(2, (((hash64[7] >>> 0) & 1) != 0));
            bs.set(3, (((hash64[5] >>> 6) & 1) != 0));
            bs.set(4, (((hash64[5] >>> 3) & 1) != 0));
            bs.set(5, (((hash64[0] >>> 1) & 1) != 0));
            bs.set(6, (((hash64[3] >>> 1) & 1) != 0));
            bs.set(7, (((hash64[1] >>> 0) & 1) != 0));
            bs.set(8, (((hash64[6] >>> 1) & 1) != 0));
            bs.set(9, (((hash64[1] >>> 4) & 1) != 0));
            bs.set(10, (((hash64[2] >>> 4) & 1) != 0));
            bs.set(11, (((hash64[7] >>> 1) & 1) != 0));
            bs.set(12, (((hash64[1] >>> 1) & 1) != 0));
            bs.set(13, (((hash64[0] >>> 2) & 1) != 0));
            bs.set(14, (((hash64[3] >>> 0) & 1) != 0));
            bs.set(15, (((hash64[2] >>> 2) & 1) != 0));
            bs.set(16, (((hash64[1] >>> 5) & 1) != 0));
            bs.set(17, (((hash64[2] >>> 6) & 1) != 0));
            bs.set(18, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(19, (((hash64[5] >>> 1) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash4 {

        public int hash4(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[1] >>> 3) & 1) != 0));
            bs.set(1, (((hash64[5] >>> 3) & 1) != 0));
            bs.set(2, (((hash64[5] >>> 2) & 1) != 0));
            bs.set(3, (((hash64[5] >>> 6) & 1) != 0));
            bs.set(4, (((hash64[6] >>> 7) & 1) != 0));
            bs.set(5, (((hash64[3] >>> 1) & 1) != 0));
            bs.set(6, (((hash64[1] >>> 6) & 1) != 0));
            bs.set(7, (((hash64[6] >>> 2) & 1) != 0));
            bs.set(8, (((hash64[2] >>> 4) & 1) != 0));
            bs.set(9, (((hash64[6] >>> 1) & 1) != 0));
            bs.set(10, (((hash64[4] >>> 0) & 1) != 0));
            bs.set(11, (((hash64[7] >>> 2) & 1) != 0));
            bs.set(12, (((hash64[6] >>> 4) & 1) != 0));
            bs.set(13, (((hash64[1] >>> 4) & 1) != 0));
            bs.set(14, (((hash64[0] >>> 2) & 1) != 0));
            bs.set(15, (((hash64[7] >>> 0) & 1) != 0));
            bs.set(16, (((hash64[7] >>> 4) & 1) != 0));
            bs.set(17, (((hash64[1] >>> 1) & 1) != 0));
            bs.set(18, (((hash64[1] >>> 0) & 1) != 0));
            bs.set(19, (((hash64[4] >>> 1) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash5 {

        public int hash5(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(1, (((hash64[2] >>> 0) & 1) != 0));
            bs.set(2, (((hash64[7] >>> 1) & 1) != 0));
            bs.set(3, (((hash64[1] >>> 2) & 1) != 0));
            bs.set(4, (((hash64[2] >>> 2) & 1) != 0));
            bs.set(5, (((hash64[4] >>> 7) & 1) != 0));
            bs.set(6, (((hash64[5] >>> 1) & 1) != 0));
            bs.set(7, (((hash64[4] >>> 5) & 1) != 0));
            bs.set(8, (((hash64[2] >>> 3) & 1) != 0));
            bs.set(9, (((hash64[5] >>> 3) & 1) != 0));
            bs.set(10, (((hash64[4] >>> 0) & 1) != 0));
            bs.set(11, (((hash64[2] >>> 7) & 1) != 0));
            bs.set(12, (((hash64[3] >>> 7) & 1) != 0));
            bs.set(13, (((hash64[1] >>> 0) & 1) != 0));
            bs.set(14, (((hash64[6] >>> 6) & 1) != 0));
            bs.set(15, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(16, (((hash64[3] >>> 0) & 1) != 0));
            bs.set(17, (((hash64[5] >>> 4) & 1) != 0));
            bs.set(18, (((hash64[1] >>> 1) & 1) != 0));
            bs.set(19, (((hash64[7] >>> 0) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash6 {

        public int hash6(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[7] >>> 6) & 1) != 0));
            bs.set(1, (((hash64[4] >>> 4) & 1) != 0));
            bs.set(2, (((hash64[3] >>> 6) & 1) != 0));
            bs.set(3, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(4, (((hash64[3] >>> 0) & 1) != 0));
            bs.set(5, (((hash64[5] >>> 7) & 1) != 0));
            bs.set(6, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(7, (((hash64[7] >>> 1) & 1) != 0));
            bs.set(8, (((hash64[1] >>> 1) & 1) != 0));
            bs.set(9, (((hash64[7] >>> 5) & 1) != 0));
            bs.set(10, (((hash64[6] >>> 7) & 1) != 0));
            bs.set(11, (((hash64[0] >>> 3) & 1) != 0));
            bs.set(12, (((hash64[7] >>> 3) & 1) != 0));
            bs.set(13, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(14, (((hash64[0] >>> 2) & 1) != 0));
            bs.set(15, (((hash64[6] >>> 3) & 1) != 0));
            bs.set(16, (((hash64[6] >>> 2) & 1) != 0));
            bs.set(17, (((hash64[6] >>> 0) & 1) != 0));
            bs.set(18, (((hash64[5] >>> 2) & 1) != 0));
            bs.set(19, (((hash64[2] >>> 6) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash7 {

        public int hash7(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[5] >>> 5) & 1) != 0));
            bs.set(1, (((hash64[3] >>> 2) & 1) != 0));
            bs.set(2, (((hash64[0] >>> 2) & 1) != 0));
            bs.set(3, (((hash64[7] >>> 5) & 1) != 0));
            bs.set(4, (((hash64[1] >>> 6) & 1) != 0));
            bs.set(5, (((hash64[0] >>> 3) & 1) != 0));
            bs.set(6, (((hash64[6] >>> 4) & 1) != 0));
            bs.set(7, (((hash64[1] >>> 3) & 1) != 0));
            bs.set(8, (((hash64[4] >>> 7) & 1) != 0));
            bs.set(9, (((hash64[3] >>> 1) & 1) != 0));
            bs.set(10, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(11, (((hash64[1] >>> 4) & 1) != 0));
            bs.set(12, (((hash64[6] >>> 7) & 1) != 0));
            bs.set(13, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(14, (((hash64[1] >>> 7) & 1) != 0));
            bs.set(15, (((hash64[2] >>> 7) & 1) != 0));
            bs.set(16, (((hash64[4] >>> 3) & 1) != 0));
            bs.set(17, (((hash64[3] >>> 4) & 1) != 0));
            bs.set(18, (((hash64[6] >>> 1) & 1) != 0));
            bs.set(19, (((hash64[1] >>> 5) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash8 {

        public int hash8(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[4] >>> 1) & 1) != 0));
            bs.set(1, (((hash64[4] >>> 4) & 1) != 0));
            bs.set(2, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(3, (((hash64[3] >>> 4) & 1) != 0));
            bs.set(4, (((hash64[4] >>> 7) & 1) != 0));
            bs.set(5, (((hash64[7] >>> 0) & 1) != 0));
            bs.set(6, (((hash64[7] >>> 6) & 1) != 0));
            bs.set(7, (((hash64[5] >>> 4) & 1) != 0));
            bs.set(8, (((hash64[6] >>> 3) & 1) != 0));
            bs.set(9, (((hash64[7] >>> 5) & 1) != 0));
            bs.set(10, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(11, (((hash64[6] >>> 2) & 1) != 0));
            bs.set(12, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(13, (((hash64[3] >>> 1) & 1) != 0));
            bs.set(14, (((hash64[0] >>> 7) & 1) != 0));
            bs.set(15, (((hash64[1] >>> 6) & 1) != 0));
            bs.set(16, (((hash64[6] >>> 7) & 1) != 0));
            bs.set(17, (((hash64[1] >>> 0) & 1) != 0));
            bs.set(18, (((hash64[0] >>> 2) & 1) != 0));
            bs.set(19, (((hash64[0] >>> 5) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash9 {

        public int hash9(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(1, (((hash64[3] >>> 4) & 1) != 0));
            bs.set(2, (((hash64[2] >>> 3) & 1) != 0));
            bs.set(3, (((hash64[1] >>> 7) & 1) != 0));
            bs.set(4, (((hash64[4] >>> 7) & 1) != 0));
            bs.set(5, (((hash64[6] >>> 3) & 1) != 0));
            bs.set(6, (((hash64[0] >>> 1) & 1) != 0));
            bs.set(7, (((hash64[5] >>> 3) & 1) != 0));
            bs.set(8, (((hash64[7] >>> 0) & 1) != 0));
            bs.set(9, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(10, (((hash64[0] >>> 2) & 1) != 0));
            bs.set(11, (((hash64[6] >>> 1) & 1) != 0));
            bs.set(12, (((hash64[2] >>> 4) & 1) != 0));
            bs.set(13, (((hash64[1] >>> 2) & 1) != 0));
            bs.set(14, (((hash64[6] >>> 6) & 1) != 0));
            bs.set(15, (((hash64[3] >>> 5) & 1) != 0));
            bs.set(16, (((hash64[6] >>> 4) & 1) != 0));
            bs.set(17, (((hash64[3] >>> 1) & 1) != 0));
            bs.set(18, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(19, (((hash64[7] >>> 2) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash10 {

        public int hash10(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[0] >>> 3) & 1) != 0));
            bs.set(1, (((hash64[4] >>> 1) & 1) != 0));
            bs.set(2, (((hash64[6] >>> 5) & 1) != 0));
            bs.set(3, (((hash64[1] >>> 4) & 1) != 0));
            bs.set(4, (((hash64[3] >>> 2) & 1) != 0));
            bs.set(5, (((hash64[5] >>> 5) & 1) != 0));
            bs.set(6, (((hash64[0] >>> 1) & 1) != 0));
            bs.set(7, (((hash64[6] >>> 0) & 1) != 0));
            bs.set(8, (((hash64[7] >>> 4) & 1) != 0));
            bs.set(9, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(10, (((hash64[1] >>> 5) & 1) != 0));
            bs.set(11, (((hash64[2] >>> 2) & 1) != 0));
            bs.set(12, (((hash64[7] >>> 7) & 1) != 0));
            bs.set(13, (((hash64[6] >>> 6) & 1) != 0));
            bs.set(14, (((hash64[1] >>> 2) & 1) != 0));
            bs.set(15, (((hash64[0] >>> 2) & 1) != 0));
            bs.set(16, (((hash64[6] >>> 1) & 1) != 0));
            bs.set(17, (((hash64[3] >>> 3) & 1) != 0));
            bs.set(18, (((hash64[7] >>> 1) & 1) != 0));
            bs.set(19, (((hash64[4] >>> 6) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash11 {

        public int hash11(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(1, (((hash64[6] >>> 7) & 1) != 0));
            bs.set(2, (((hash64[2] >>> 7) & 1) != 0));
            bs.set(3, (((hash64[1] >>> 7) & 1) != 0));
            bs.set(4, (((hash64[5] >>> 4) & 1) != 0));
            bs.set(5, (((hash64[5] >>> 1) & 1) != 0));
            bs.set(6, (((hash64[7] >>> 4) & 1) != 0));
            bs.set(7, (((hash64[6] >>> 5) & 1) != 0));
            bs.set(8, (((hash64[5] >>> 5) & 1) != 0));
            bs.set(9, (((hash64[2] >>> 4) & 1) != 0));
            bs.set(10, (((hash64[2] >>> 2) & 1) != 0));
            bs.set(11, (((hash64[1] >>> 3) & 1) != 0));
            bs.set(12, (((hash64[3] >>> 3) & 1) != 0));
            bs.set(13, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(14, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(15, (((hash64[5] >>> 2) & 1) != 0));
            bs.set(16, (((hash64[7] >>> 3) & 1) != 0));
            bs.set(17, (((hash64[6] >>> 6) & 1) != 0));
            bs.set(18, (((hash64[4] >>> 7) & 1) != 0));
            bs.set(19, (((hash64[3] >>> 2) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash12 {

        public int hash12(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[1] >>> 3) & 1) != 0));
            bs.set(1, (((hash64[0] >>> 7) & 1) != 0));
            bs.set(2, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(3, (((hash64[6] >>> 1) & 1) != 0));
            bs.set(4, (((hash64[5] >>> 1) & 1) != 0));
            bs.set(5, (((hash64[4] >>> 4) & 1) != 0));
            bs.set(6, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(7, (((hash64[4] >>> 1) & 1) != 0));
            bs.set(8, (((hash64[4] >>> 5) & 1) != 0));
            bs.set(9, (((hash64[6] >>> 7) & 1) != 0));
            bs.set(10, (((hash64[3] >>> 2) & 1) != 0));
            bs.set(11, (((hash64[3] >>> 7) & 1) != 0));
            bs.set(12, (((hash64[3] >>> 1) & 1) != 0));
            bs.set(13, (((hash64[1] >>> 0) & 1) != 0));
            bs.set(14, (((hash64[7] >>> 0) & 1) != 0));
            bs.set(15, (((hash64[4] >>> 7) & 1) != 0));
            bs.set(16, (((hash64[3] >>> 4) & 1) != 0));
            bs.set(17, (((hash64[5] >>> 3) & 1) != 0));
            bs.set(18, (((hash64[4] >>> 6) & 1) != 0));
            bs.set(19, (((hash64[2] >>> 6) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash13 {

        public int hash13(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[0] >>> 7) & 1) != 0));
            bs.set(1, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(2, (((hash64[6] >>> 5) & 1) != 0));
            bs.set(3, (((hash64[2] >>> 0) & 1) != 0));
            bs.set(4, (((hash64[7] >>> 7) & 1) != 0));
            bs.set(5, (((hash64[1] >>> 6) & 1) != 0));
            bs.set(6, (((hash64[6] >>> 6) & 1) != 0));
            bs.set(7, (((hash64[4] >>> 3) & 1) != 0));
            bs.set(8, (((hash64[2] >>> 3) & 1) != 0));
            bs.set(9, (((hash64[4] >>> 1) & 1) != 0));
            bs.set(10, (((hash64[4] >>> 6) & 1) != 0));
            bs.set(11, (((hash64[0] >>> 3) & 1) != 0));
            bs.set(12, (((hash64[3] >>> 4) & 1) != 0));
            bs.set(13, (((hash64[4] >>> 0) & 1) != 0));
            bs.set(14, (((hash64[1] >>> 0) & 1) != 0));
            bs.set(15, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(16, (((hash64[1] >>> 3) & 1) != 0));
            bs.set(17, (((hash64[7] >>> 2) & 1) != 0));
            bs.set(18, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(19, (((hash64[3] >>> 7) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash14 {

        public int hash14(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[2] >>> 7) & 1) != 0));
            bs.set(1, (((hash64[3] >>> 1) & 1) != 0));
            bs.set(2, (((hash64[4] >>> 2) & 1) != 0));
            bs.set(3, (((hash64[7] >>> 0) & 1) != 0));
            bs.set(4, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(5, (((hash64[1] >>> 6) & 1) != 0));
            bs.set(6, (((hash64[7] >>> 5) & 1) != 0));
            bs.set(7, (((hash64[3] >>> 6) & 1) != 0));
            bs.set(8, (((hash64[6] >>> 2) & 1) != 0));
            bs.set(9, (((hash64[3] >>> 3) & 1) != 0));
            bs.set(10, (((hash64[4] >>> 6) & 1) != 0));
            bs.set(11, (((hash64[6] >>> 7) & 1) != 0));
            bs.set(12, (((hash64[0] >>> 7) & 1) != 0));
            bs.set(13, (((hash64[4] >>> 5) & 1) != 0));
            bs.set(14, (((hash64[2] >>> 4) & 1) != 0));
            bs.set(15, (((hash64[7] >>> 4) & 1) != 0));
            bs.set(16, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(17, (((hash64[1] >>> 4) & 1) != 0));
            bs.set(18, (((hash64[4] >>> 0) & 1) != 0));
            bs.set(19, (((hash64[7] >>> 1) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash15 {

        public int hash15(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[5] >>> 2) & 1) != 0));
            bs.set(1, (((hash64[3] >>> 5) & 1) != 0));
            bs.set(2, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(3, (((hash64[5] >>> 7) & 1) != 0));
            bs.set(4, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(5, (((hash64[6] >>> 4) & 1) != 0));
            bs.set(6, (((hash64[5] >>> 6) & 1) != 0));
            bs.set(7, (((hash64[3] >>> 2) & 1) != 0));
            bs.set(8, (((hash64[0] >>> 1) & 1) != 0));
            bs.set(9, (((hash64[1] >>> 7) & 1) != 0));
            bs.set(10, (((hash64[6] >>> 0) & 1) != 0));
            bs.set(11, (((hash64[5] >>> 1) & 1) != 0));
            bs.set(12, (((hash64[5] >>> 5) & 1) != 0));
            bs.set(13, (((hash64[5] >>> 3) & 1) != 0));
            bs.set(14, (((hash64[3] >>> 0) & 1) != 0));
            bs.set(15, (((hash64[3] >>> 7) & 1) != 0));
            bs.set(16, (((hash64[6] >>> 2) & 1) != 0));
            bs.set(17, (((hash64[6] >>> 5) & 1) != 0));
            bs.set(18, (((hash64[0] >>> 2) & 1) != 0));
            bs.set(19, (((hash64[5] >>> 4) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash16 {

        public int hash16(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[0] >>> 6) & 1) != 0));
            bs.set(1, (((hash64[3] >>> 1) & 1) != 0));
            bs.set(2, (((hash64[6] >>> 0) & 1) != 0));
            bs.set(3, (((hash64[3] >>> 6) & 1) != 0));
            bs.set(4, (((hash64[1] >>> 5) & 1) != 0));
            bs.set(5, (((hash64[1] >>> 2) & 1) != 0));
            bs.set(6, (((hash64[1] >>> 4) & 1) != 0));
            bs.set(7, (((hash64[7] >>> 5) & 1) != 0));
            bs.set(8, (((hash64[3] >>> 4) & 1) != 0));
            bs.set(9, (((hash64[5] >>> 2) & 1) != 0));
            bs.set(10, (((hash64[7] >>> 1) & 1) != 0));
            bs.set(11, (((hash64[4] >>> 6) & 1) != 0));
            bs.set(12, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(13, (((hash64[6] >>> 6) & 1) != 0));
            bs.set(14, (((hash64[4] >>> 2) & 1) != 0));
            bs.set(15, (((hash64[4] >>> 5) & 1) != 0));
            bs.set(16, (((hash64[0] >>> 3) & 1) != 0));
            bs.set(17, (((hash64[1] >>> 1) & 1) != 0));
            bs.set(18, (((hash64[6] >>> 4) & 1) != 0));
            bs.set(19, (((hash64[7] >>> 3) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash17 {

        public int hash17(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[3] >>> 2) & 1) != 0));
            bs.set(1, (((hash64[1] >>> 6) & 1) != 0));
            bs.set(2, (((hash64[0] >>> 6) & 1) != 0));
            bs.set(3, (((hash64[5] >>> 1) & 1) != 0));
            bs.set(4, (((hash64[7] >>> 1) & 1) != 0));
            bs.set(5, (((hash64[7] >>> 2) & 1) != 0));
            bs.set(6, (((hash64[4] >>> 7) & 1) != 0));
            bs.set(7, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(8, (((hash64[2] >>> 0) & 1) != 0));
            bs.set(9, (((hash64[2] >>> 4) & 1) != 0));
            bs.set(10, (((hash64[6] >>> 5) & 1) != 0));
            bs.set(11, (((hash64[6] >>> 3) & 1) != 0));
            bs.set(12, (((hash64[5] >>> 2) & 1) != 0));
            bs.set(13, (((hash64[5] >>> 5) & 1) != 0));
            bs.set(14, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(15, (((hash64[1] >>> 2) & 1) != 0));
            bs.set(16, (((hash64[2] >>> 6) & 1) != 0));
            bs.set(17, (((hash64[4] >>> 4) & 1) != 0));
            bs.set(18, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(19, (((hash64[4] >>> 3) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash18 {

        public int hash18(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[5] >>> 1) & 1) != 0));
            bs.set(1, (((hash64[2] >>> 4) & 1) != 0));
            bs.set(2, (((hash64[7] >>> 6) & 1) != 0));
            bs.set(3, (((hash64[7] >>> 2) & 1) != 0));
            bs.set(4, (((hash64[3] >>> 2) & 1) != 0));
            bs.set(5, (((hash64[7] >>> 1) & 1) != 0));
            bs.set(6, (((hash64[5] >>> 4) & 1) != 0));
            bs.set(7, (((hash64[2] >>> 5) & 1) != 0));
            bs.set(8, (((hash64[4] >>> 6) & 1) != 0));
            bs.set(9, (((hash64[5] >>> 0) & 1) != 0));
            bs.set(10, (((hash64[6] >>> 0) & 1) != 0));
            bs.set(11, (((hash64[2] >>> 7) & 1) != 0));
            bs.set(12, (((hash64[1] >>> 4) & 1) != 0));
            bs.set(13, (((hash64[1] >>> 6) & 1) != 0));
            bs.set(14, (((hash64[1] >>> 2) & 1) != 0));
            bs.set(15, (((hash64[1] >>> 5) & 1) != 0));
            bs.set(16, (((hash64[0] >>> 5) & 1) != 0));
            bs.set(17, (((hash64[2] >>> 1) & 1) != 0));
            bs.set(18, (((hash64[0] >>> 3) & 1) != 0));
            bs.set(19, (((hash64[4] >>> 1) & 1) != 0));
            return bs.hashCode();
        }
    }

    class Hash19 {

        public int hash19(byte[] hash64) {
            BitSet bs = new BitSet(20);

            bs.set(0, (((hash64[4] >>> 4) & 1) != 0));
            bs.set(1, (((hash64[1] >>> 0) & 1) != 0));
            bs.set(2, (((hash64[5] >>> 1) & 1) != 0));
            bs.set(3, (((hash64[0] >>> 6) & 1) != 0));
            bs.set(4, (((hash64[6] >>> 5) & 1) != 0));
            bs.set(5, (((hash64[0] >>> 5) & 1) != 0));
            bs.set(6, (((hash64[7] >>> 2) & 1) != 0));
            bs.set(7, (((hash64[6] >>> 2) & 1) != 0));
            bs.set(8, (((hash64[0] >>> 0) & 1) != 0));
            bs.set(9, (((hash64[3] >>> 0) & 1) != 0));
            bs.set(10, (((hash64[4] >>> 1) & 1) != 0));
            bs.set(11, (((hash64[4] >>> 3) & 1) != 0));
            bs.set(12, (((hash64[1] >>> 6) & 1) != 0));
            bs.set(13, (((hash64[3] >>> 2) & 1) != 0));
            bs.set(14, (((hash64[5] >>> 6) & 1) != 0));
            bs.set(15, (((hash64[4] >>> 6) & 1) != 0));
            bs.set(16, (((hash64[1] >>> 2) & 1) != 0));
            bs.set(17, (((hash64[4] >>> 0) & 1) != 0));
            bs.set(18, (((hash64[3] >>> 4) & 1) != 0));
            bs.set(19, (((hash64[4] >>> 5) & 1) != 0));
            return bs.hashCode();
        }
    }

    Hash0 hash0 = new Hash0();
    Hash1 hash1 = new Hash1();
    Hash2 hash2 = new Hash2();
    Hash3 hash3 = new Hash3();
    Hash4 hash4 = new Hash4();
    Hash5 hash5 = new Hash5();
    Hash6 hash6 = new Hash6();
    Hash7 hash7 = new Hash7();
    Hash8 hash8 = new Hash8();
    Hash9 hash9 = new Hash9();
    Hash10 hash10 = new Hash10();
    Hash11 hash11 = new Hash11();
    Hash12 hash12 = new Hash12();
    Hash13 hash13 = new Hash13();
    Hash14 hash14 = new Hash14();
    Hash15 hash15 = new Hash15();
    Hash16 hash16 = new Hash16();
    Hash17 hash17 = new Hash17();
    Hash18 hash18 = new Hash18();
    Hash19 hash19 = new Hash19();

    public final LSH[] hashers = new LSH[]{
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash0.hash0(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash1.hash1(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash2.hash2(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash3.hash3(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash4.hash4(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash5.hash5(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash6.hash6(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash7.hash7(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash8.hash8(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash9.hash9(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash10.hash10(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash11.hash11(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash12.hash12(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash13.hash13(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash14.hash14(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash15.hash15(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash16.hash16(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash17.hash17(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash18.hash18(hash64);
            }
        },
        new LSH() {
            @Override
            public int hash(byte[] hash64) {
                return hash19.hash19(hash64);
            }
        }
    };

}
