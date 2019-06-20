/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.util;

import ij.ImagePlus;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class ImageUtils {
    // returns PATH to stored file
    public static String saveImage(ImagePlus imp, String path) {
        String absoluteFilePath = null;
        try {
            BufferedImage bi = imp.getBufferedImage();
            int fileNameStart = imp.getOriginalFileInfo().url.lastIndexOf("/");
            String fileNameOut = imp.getOriginalFileInfo().url.substring(fileNameStart);
            String filePath = path+"/"+fileNameOut;
            File outputfile = new File(filePath);
            absoluteFilePath = outputfile.getCanonicalPath();
            ImageIO.write(bi, "jpg", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return absoluteFilePath;
    }
}
