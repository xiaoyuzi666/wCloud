package com.medical.imaging.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class DicomImageProcessor {

    public BufferedImage adjustWindowLevel(String imagePath, int windowWidth, int windowCenter) throws IOException {
        try {
            // 读取DICOM图像
            BufferedImage originalImage = readDicomImage(imagePath);
            
            // 应用窗宽窗位调整
            return applyWindowLevel(originalImage, windowWidth, windowCenter);
        } catch (IOException e) {
            log.error("Failed to process DICOM image: {}", imagePath, e);
            throw e;
        }
    }

    private BufferedImage readDicomImage(String imagePath) throws IOException {
        File dicomFile = new File(imagePath);
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("DICOM");
        
        if (!readers.hasNext()) {
            throw new IOException("No DICOM image reader found");
        }

        ImageReader reader = readers.next();
        try (ImageInputStream iis = ImageIO.createImageInputStream(dicomFile)) {
            reader.setInput(iis);
            DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
            return reader.read(0, param);
        } finally {
            reader.dispose();
        }
    }

    private BufferedImage applyWindowLevel(BufferedImage image, int windowWidth, int windowCenter) {
        BufferedImage result = new BufferedImage(
            image.getWidth(), 
            image.getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );

        double min = windowCenter - (windowWidth / 2.0);
        double max = windowCenter + (windowWidth / 2.0);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y) & 0xff;
                double normalized;

                if (pixel <= min) {
                    normalized = 0;
                } else if (pixel >= max) {
                    normalized = 255;
                } else {
                    normalized = ((pixel - min) / windowWidth) * 255;
                }

                int gray = (int) Math.round(normalized);
                int rgb = (gray << 16) | (gray << 8) | gray;
                result.setRGB(x, y, rgb);
            }
        }

        return result;
    }
} 