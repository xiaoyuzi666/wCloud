package com.medical.imaging.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che3.io.DicomInputStream;
import org.springframework.stereotype.Component;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class DicomImageProcessor {

    public BufferedImage adjustWindowLevel(String imagePath, int windowWidth, int windowCenter) throws IOException {
        try (DicomInputStream dis = new DicomInputStream(new File(imagePath))) {
            DicomImageReadParam param = new DicomImageReadParam();
            param.setWindowCenter(windowCenter);
            param.setWindowWidth(windowWidth);
            
            ImageReader reader = ImageIO.getImageReadersByFormatName("DICOM").next();
            try (ImageInputStream iis = ImageIO.createImageInputStream(new File(imagePath))) {
                reader.setInput(iis);
                return reader.read(0, param);
            } finally {
                reader.dispose();
            }
        }
    }

    public BufferedImage adjustContrast(BufferedImage image, double contrast, double brightness) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                red = adjustPixel(red, contrast, brightness);
                green = adjustPixel(green, contrast, brightness);
                blue = adjustPixel(blue, contrast, brightness);

                result.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);
            }
        }
        return result;
    }

    public BufferedImage generateThumbnail(String imagePath, int width, int height) throws IOException {
        BufferedImage original = ImageIO.read(new File(imagePath));
        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = thumbnail.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return thumbnail;
    }

    private int adjustPixel(int pixel, double contrast, double brightness) {
        double newValue = pixel * contrast + brightness;
        return Math.min(Math.max((int) newValue, 0), 255);
    }
} 