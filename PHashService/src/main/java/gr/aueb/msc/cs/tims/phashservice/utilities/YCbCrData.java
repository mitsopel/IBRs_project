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
public class YCbCrData {

    public int[][] Y;
    public int[][] Cb;
    public int[][] Cr;

    public YCbCrData(int size) {
        this.Y = new int[size][size];
        this.Cb = new int[size][size];
        this.Cr = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.Y[i][j] = 0;
                this.Cb[i][j] = 0;
                this.Cr[i][j] = 0;
            }
        }
    }

    public void print() {
        for (int i = 0; i < this.Y.length; i++) {
            for (int j = 0; j < this.Y[0].length; j++) {
                System.out.println("Y[" + i + "][" + j + "] : " + this.Y[i][j] + ", Cb[" + i + "][" + j + "] : " + this.Cb[i][j] + ", Cr[" + i + "][" + j + "] : " + this.Cr[i][j]);
            }
        }
    }
}
