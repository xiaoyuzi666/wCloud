package com.medical.imaging.util;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class DicomImageUtil {
    
    public static BufferedImage applyWindowLevel(BufferedImage image, int windowWidth, int windowCenter) {
        BufferedImage result = new BufferedImage(
            image.getWidth(), 
            image.getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        
        Raster raster = image.getRaster();
        WritableRaster resultRaster = result.getRaster();
        
        double min = windowCenter - (windowWidth / 2.0);
        double max = windowCenter + (windowWidth / 2.0);
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                double value = raster.getSampleDouble(x, y, 0);
                
                // Apply window/level transformation
                if (value <= min) {
                    value = 0;
                } else if (value >= max) {
                    value = 255;
                } else {
                    value = ((value - min) / windowWidth) * 255;
                }
                
                int pixelValue = (int) Math.round(value);
                resultRaster.setSample(x, y, 0, pixelValue);
                resultRaster.setSample(x, y, 1, pixelValue);
                resultRaster.setSample(x, y, 2, pixelValue);
            }
        }
        
        return result;
    }
} 