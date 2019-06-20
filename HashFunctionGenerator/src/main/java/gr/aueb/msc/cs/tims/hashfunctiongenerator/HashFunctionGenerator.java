/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.hashfunctiongenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;


 // Generates NUMBER_OF_FUNCTIONS Hash functions for use in LSH

public class HashFunctionGenerator {
    public static final int NUMBER_OF_FUNCTIONS = 20;
    public static final int NUMBER_OF_SAMPLES = 20;
    public static final int IBR_BYTE_LENGTH = 92;
    public static final int IBR_HASH64_BYTE_LENGTH = 8;
    public static final int DIMENSION = IBR_HASH64_BYTE_LENGTH * 8;
    public static final Random rnd = new Random((new Date()).getTime());
    
    public static void main(String[] args) {
        PrintWriter out = null;
        try {
            FileWriter fstream = new FileWriter("LSH.java", false); //true tells to append data.
            out = new PrintWriter(fstream);

            int i, j;

            out.println("public interface LSH {\n"
                    + "  int hash(byte[] hash64);\n"
                    + "}");

            for (i = 0; i < NUMBER_OF_FUNCTIONS; i++) {
                HashSet<Integer> usedIndexes = new HashSet<>();
                out.println("class Hash" + i + " {\n"
                        //+ "  public int hash" + i + "(byte[] hash64) {\nStringBuilder sb = new StringBuilder("+NUMBER_OF_SAMPLES+");\n");
                        + "  public int hash" + i + "(byte[] hash64) {\nBitSet bs = new BitSet("+NUMBER_OF_SAMPLES+");\n");
                for (j = 0; j < NUMBER_OF_SAMPLES; j++) {
                    int nextInt = rnd.nextInt(DIMENSION);
                    int bitOffset = nextInt % 8;
                    int byteOffset = nextInt / 8;
                    int index = 8 * byteOffset + bitOffset;
                    // make sure the same bit index is not used
                    while (usedIndexes.contains(index)) {
                        nextInt = rnd.nextInt(DIMENSION);
                        bitOffset = nextInt % 8;
                        byteOffset = nextInt / 8;
                        index = 8 * byteOffset + bitOffset;
                    }
                    usedIndexes.add(index);
                    //out.print("sb.append((hash64[" + byteOffset + "] >>> " + bitOffset + ") & 1);\n");
                    out.print("bs.set("+j+", (((hash64[" + byteOffset + "] >>> " + bitOffset + ") & 1 ) != 0 ));\n");
                }
            
            //out.println(" return sb.toString().hashCode();}\n"+ "}\n\n");
            out.println(" return bs.hashCode();}\n"+ "}\n\n");
            }
            
            for (j = 0; j < NUMBER_OF_FUNCTIONS; j++) {
                out.println("Hash" + j + " hash"+j+" = new Hash"+j+"();");
            }
            out.println("\n\n");
            out.println("public final LSH[] hashers = new LSH[] {");
            for (j = 0; j < NUMBER_OF_FUNCTIONS-1; j++) {
                out.println("new LSH() {@Override public int hash(byte[] hash64) { return hash"+j+".hash"+j+"(hash64); } },");
            }
            j = NUMBER_OF_FUNCTIONS-1;
            out.println("new LSH() {@Override public int hash(byte[] hash64) { return hash"+j+".hash"+j+"(hash64); } }");
            out.println("};");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }
}
