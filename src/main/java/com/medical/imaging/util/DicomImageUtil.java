package com.medical.imaging.util;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;

public class DicomImageUtil {
    
    public static BufferedImage applyWindowLevel(BufferedImage original, int windowWidth, int windowCenter) {
        BufferedImage result = new BufferedImage(
            original.getWidth(), 
            original.getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        
        Raster raster = original.getRaster();
        WritableRaster outRaster = result.getRaster();
        
        // 计算窗宽窗位的范围
        double min = windowCenter - windowWidth / 2.0;
        double max = windowCenter + windowWidth / 2.0;
        
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                // 获取像素值
                int pixelValue = raster.getSample(x, y, 0);
                
                // 应用窗宽窗位
                int mappedValue;
                if (pixelValue <= min) {
                    mappedValue = 0;
                } else if (pixelValue >= max) {
                    mappedValue = 255;
                } else {
                    mappedValue = (int) (((pixelValue - min) / windowWidth) * 255);
                }
                
                // 设置RGB值
                outRaster.setSample(x, y, 0, mappedValue);
                outRaster.setSample(x, y, 1, mappedValue);
                outRaster.setSample(x, y, 2, mappedValue);
            }
        }
        
        return result;
    }
    
    public static Attributes readDicomAttributes(byte[] dicomData) throws Exception {
        try (DicomInputStream dis = new DicomInputStream(new ByteArrayInputStream(dicomData))) {
            return dis.readDataset();
        }
    }
    
    public static double[] getPixelSpacing(Attributes attrs) {
        double[] spacing = new double[3];
        
        // 获取像素间距
        String[] pixelSpacing = attrs.getStrings(Tag.PixelSpacing);
        if (pixelSpacing != null && pixelSpacing.length == 2) {
            spacing[0] = Double.parseDouble(pixelSpacing[0]);
            spacing[1] = Double.parseDouble(pixelSpacing[1]);
        }
        
        // 获取层厚
        String sliceThickness = attrs.getString(Tag.SliceThickness);
        if (sliceThickness != null) {
            spacing[2] = Double.parseDouble(sliceThickness);
        }
        
        return spacing;
    }
} 