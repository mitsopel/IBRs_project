/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.phashservice.utilities;

/**
 *
 * @author vasilis
 */
public class DCTData {

    public double[][] YDCT;
    public double[][] CbDCT;
    public double[][] CrDCT;

    public DCTData(int size) {
        this.YDCT = new double[size][size];
        this.CbDCT = new double[size][size];
        this.CrDCT = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.YDCT[i][j] = 0.0;
                this.CbDCT[i][j] = 0.0;
                this.CrDCT[i][j] = 0.0;
            }
        }
    }

    public void print() {
        for (int i = 0; i < this.YDCT.length; i++) {
            for (int j = 0; j < this.YDCT[0].length; j++) {
                System.out.println("YDCT[" + i + "][" + j + "] : " + this.YDCT[i][j] + ", CbDCT[" + i + "][" + j + "] : " + this.CbDCT[i][j] + ", CrDCT[" + i + "][" + j + "] : " + this.CrDCT[i][j]);
            }
        }
    }
}
