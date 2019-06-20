package gr.aueb.msc.cs.tims.phashservice.utilities;

// implementation according to 
import gr.aueb.msc.cs.tims.phashservice.PHashService;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Utilities {

    private static final double[] coef32 = {
        0.7071067811865475, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
        1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0
    };

    public static DCTData calculateDCTData(YCbCrData data) {
        int size = data.Y.length;
        DCTData tmpDCT = new DCTData(size);

        for (int u = 0; u < size; u++) {
            for (int v = 0; v < size; v++) {
                double sumY = 0.0;
                double sumCb = 0.0;
                double sumCr = 0.0;
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        sumY += Math.cos(((2 * i + 1) / (2.0 * size)) * u * Math.PI) * Math.cos(((2 * j + 1) / (2.0 * size)) * v * Math.PI) * data.Y[i][j];
                        sumCb += Math.cos(((2 * i + 1) / (2.0 * size)) * u * Math.PI) * Math.cos(((2 * j + 1) / (2.0 * size)) * v * Math.PI) * data.Cb[i][j];
                        sumCr += Math.cos(((2 * i + 1) / (2.0 * size)) * u * Math.PI) * Math.cos(((2 * j + 1) / (2.0 * size)) * v * Math.PI) * data.Cr[i][j];
                    }
                }
                sumY *= ((coef32[u] * coef32[v]) / 4.0);
                sumCb *= ((coef32[u] * coef32[v]) / 4.0);
                sumCr *= ((coef32[u] * coef32[v]) / 4.0);
                tmpDCT.YDCT[u][v] = sumY;
                tmpDCT.CbDCT[u][v] = sumCb;
                tmpDCT.CrDCT[u][v] = sumCr;
            }
        }

        return tmpDCT;
    }

    // http://www.cs.cf.ac.uk/Dave/Multimedia/node231.html
    public static DCTData calculateDCTDataOld(YCbCrData yData) {
        DCTData data = new DCTData(yData.Y.length);
        int sizeN = data.YDCT.length;
        double firstTerm = 2.0 / sizeN;

        for (int u = 0; u < sizeN; u++) {
            for (int v = 0; v < sizeN; v++) {
                double sumY = 0.0;
                double sumCb = 0.0;
                double sumCr = 0.0;

                double commonSum = 0.0;

                for (int i = 0; i < sizeN; i++) {
                    for (int j = 0; j < sizeN; j++) {
                        commonSum += Lfunction(i) * Lfunction(j)
                                * Math.cos(((Math.PI * u) / 2 * sizeN) * (2 * i + 1))
                                * Math.cos(((Math.PI * v) / 2 * sizeN) * (2 * j + 1));

                        sumY = commonSum * yData.Y[i][j];
                        sumCb = commonSum * yData.Cb[i][j];
                        sumCr = commonSum * yData.Cr[i][j];
                    }
                }
                sumY = sumY * firstTerm;
                sumCb = sumCb * firstTerm;
                sumCr = sumCr * firstTerm;
                data.YDCT[u][v] = sumY;
                data.CbDCT[u][v] = sumCb;
                data.CrDCT[u][v] = sumCr;
            }
        }
        return data;
    }

    public static double Lfunction(int i) {
        return (i == 0 ? 1.0 / Math.sqrt(2) : 1.0);
    }

    public static YCbCrData rgbToYCbCr(BufferedImage image) {
        if (PHashService.PRINT_RGB_TO_YCbCr) {
            System.out.println("image.getWidht()=" + image.getWidth() + " image.getHeight()" + image.getHeight());
        }
        YCbCrData tmpYCbCr = new YCbCrData(image.getWidth());
        Color c;

        // calulation formulas from https://msdn.microsoft.com/en-us/library/ff635643.aspx
        // and http://www.f4.fhtw-berlin.de/~barthel/ImageJ/ColorInspector/HTMLHelp/farbraumJava.htm
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                c = new Color(image.getRGB(i, j));
                double r = (double) c.getRed();
                double g = (double) c.getGreen();
                double b = (double) c.getBlue();
                if (PHashService.PRINT_RGB_TO_YCbCr) {
                    System.out.println("r:" + r + " g:" + g + " b:" + b);
                }
                tmpYCbCr.Y[i][j] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                tmpYCbCr.Cb[i][j] = (int) (-0.16874 * r - 0.33126 * g + 0.50000 * b);
                tmpYCbCr.Cr[i][j] = (int) (0.50000 * r - 0.41869 * g - 0.08131 * b);
            }
        }

        return tmpYCbCr;
    }

    public static BufferedImage resizeImage(BufferedImage image, int RESIZE_DIMENSION, int RESIZE_DIMENSION0) {
        BufferedImage tmpBI = new BufferedImage(RESIZE_DIMENSION, RESIZE_DIMENSION, image.getType());

        Graphics2D g2d = tmpBI.createGraphics();
        g2d.drawImage(image, 0, 0, RESIZE_DIMENSION, RESIZE_DIMENSION0, null);
        g2d.dispose();
        return tmpBI;
    }

    public static void main(String[] args) {

    }

    public static int[][] getSubMatrix(int columns, int rows, double[][] input, boolean fromEnd) {
        int[][] output = new int[rows][columns];
        int columnLimit, rowLimit, rowStart, columnStart;

        if (fromEnd) {
            rowStart = input.length - rows;
            columnStart = input[0].length - columns;
            columnLimit = input[0].length;
            rowLimit = input.length;
        } else {
            rowStart = 0;
            columnStart = 0;
            columnLimit = columns;
            rowLimit = rows;
        }

        double sum = 0.0;

        //calculate sum of cells
        for (int i = rowStart; i < rowLimit; i++) {
            for (int j = columnStart; j < columnLimit; j++) {
                sum += input[i][j];
            }
        }

        //calculate average
        double average = sum / ((rowLimit - rowStart) * (columnLimit - columnStart));

        //now set each cell of the output array to 1
        //if the cell value is greater than average
        //and 0 if the cell value is lower than average
        for (int i = rowStart; i < rowLimit; i++) {
            for (int j = columnStart; j < columnLimit; j++) {
                if (input[i][j] >= average) {
                    output[i - rowStart][j - columnStart] = 1;
                } else {
                    output[i - rowStart][j - columnStart] = 0;
                }
            }
        }

        return output;
    }

    public static String getCombinedBytes(int[][] input) {
        String outputString = "";
        
//        double bytesPerLine = ((double) input[0].length) / 8.0;
//        System.out.println("CHECK THIS LINE IT SHOULD BE 1 OR 2 => " + bytesPerLine);


        for(int i=0 ; i< input.length ; i++ ) {
            for(int j=0; j<input[0].length; j++) {
                outputString += Integer.toString(input[i][j]);
            }
        }
        
        return outputString;
    }
}
