package com.medical.imaging.service.impl;

import com.medical.imaging.config.OrthancConfig;
import com.medical.imaging.service.ImageProcessingService;
import lombok.RequiredArgsConstructor;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.medical.imaging.util.DicomImageUtil;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import java.nio.file.Path;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ImageProcessingServiceImpl implements ImageProcessingService {

    private final OrthancConfig orthancConfig;
    private final RestTemplate restTemplate;

    @Override
    public byte[] generateThumbnail(String studyId, String instanceId) throws Exception {
        // 从Orthanc获取DICOM实例
        byte[] dicomData = fetchDicomInstance(instanceId);
        
        // 将DICOM转换为BufferedImage
        BufferedImage image = convertDicomToBufferedImage(dicomData);
        
        // 生成缩略图
        BufferedImage thumbnail = createThumbnail(image, 200, 200);
        
        // 转换为PNG格式
        return convertToBytes(thumbnail, "PNG");
    }

    @Override
    public byte[] adjustContrast(String instanceId, double factor) throws Exception {
        byte[] dicomData = fetchDicomInstance(instanceId);
        BufferedImage image = convertDicomToBufferedImage(dicomData);
        
        // 调整对比度
        BufferedImage adjustedImage = new BufferedImage(
            image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (int) (((rgb >> 16) & 0xFF) * factor);
                int green = (int) (((rgb >> 8) & 0xFF) * factor);
                int blue = (int) ((rgb & 0xFF) * factor);
                
                // 确保值在0-255范围
                red = Math.min(255, Math.max(0, red));
                green = Math.min(255, Math.max(0, green));
                blue = Math.min(255, Math.max(0, blue));
                
                adjustedImage.setRGB(x, y, (alpha << 24) | (red << 16) | (green << 8) | blue);
            }
        }
        
        return convertToBytes(adjustedImage, "PNG");
    }

    @Override
    public byte[] rotateImage(String instanceId, int degrees) throws Exception {
        byte[] dicomData = fetchDicomInstance(instanceId);
        BufferedImage image = convertDicomToBufferedImage(dicomData);
        
        // 创建旋转后的图像
        double rads = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));
        
        int newWidth = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        int newHeight = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotatedImage.createGraphics();
        
        g2d.translate((newWidth - image.getWidth()) / 2, (newHeight - image.getHeight()) / 2);
        g2d.rotate(rads, image.getWidth() / 2, image.getHeight() / 2);
        g2d.drawRenderedImage(image, null);
        g2d.dispose();
        
        return convertToBytes(rotatedImage, "PNG");
    }

    @Override
    public byte[] convertToPng(String instanceId) throws Exception {
        byte[] dicomData = fetchDicomInstance(instanceId);
        BufferedImage image = convertDicomToBufferedImage(dicomData);
        return convertToBytes(image, "PNG");
    }

    @Override
    public byte[] exportDicom(String instanceId) throws Exception {
        return fetchDicomInstance(instanceId);
    }

    @Override
    public byte[] adjustWindowLevel(String instanceId, int windowWidth, int windowCenter) throws Exception {
        byte[] dicomData = fetchDicomInstance(instanceId);
        BufferedImage image = convertDicomToBufferedImage(dicomData);
        
        // 应用窗宽窗位调整
        BufferedImage adjustedImage = DicomImageUtil.applyWindowLevel(image, windowWidth, windowCenter);
        return convertToBytes(adjustedImage, "PNG");
    }

    @Override
    public byte[] generateAxialView(String seriesId, int sliceIndex) throws Exception {
        List<byte[]> instancesData = fetchSeriesInstances(seriesId);
        
        // 使用 DCM4CHE 处理 DICOM 数据
        List<BufferedImage> slices = new ArrayList<>();
        for (byte[] instanceData : instancesData) {
            try (DicomInputStream dis = new DicomInputStream(new ByteArrayInputStream(instanceData))) {
                Attributes attrs = dis.readDataset();
                BufferedImage image = convertDicomToBufferedImage(instanceData);
                slices.add(image);
            }
        }
        
        // 根据切片索引获取对应的轴向视图
        if (sliceIndex >= 0 && sliceIndex < slices.size()) {
            BufferedImage axialView = slices.get(sliceIndex);
            return convertToBytes(axialView, "PNG");
        }
        
        throw new IllegalArgumentException("Invalid slice index");
    }

    @Override
    public byte[] generateCoronalView(String seriesId, int sliceIndex) throws Exception {
        List<byte[]> instancesData = fetchSeriesInstances(seriesId);
        
        // 读取所有切片
        List<BufferedImage> slices = new ArrayList<>();
        int width = 0;
        int height = 0;
        
        for (byte[] instanceData : instancesData) {
            BufferedImage slice = convertDicomToBufferedImage(instanceData);
            slices.add(slice);
            width = Math.max(width, slice.getWidth());
            height = Math.max(height, slice.getHeight());
        }
        
        // 创建冠状面重建图像
        if (sliceIndex >= 0 && sliceIndex < width) {
            BufferedImage coronalView = new BufferedImage(height, slices.size(), BufferedImage.TYPE_INT_RGB);
            
            // 对每个切片进行处理
            for (int z = 0; z < slices.size(); z++) {
                BufferedImage slice = slices.get(z);
                for (int y = 0; y < slice.getHeight(); y++) {
                    int rgb = slice.getRGB(sliceIndex, y);
                    coronalView.setRGB(y, z, rgb);
                }
            }
            
            return convertToBytes(coronalView, "PNG");
        }
        
        throw new IllegalArgumentException("Invalid slice index");
    }

    @Override
    public byte[] generateSagittalView(String seriesId, int sliceIndex) throws Exception {
        List<byte[]> instancesData = fetchSeriesInstances(seriesId);
        
        // 读取所有切片
        List<BufferedImage> slices = new ArrayList<>();
        int width = 0;
        int height = 0;
        
        for (byte[] instanceData : instancesData) {
            BufferedImage slice = convertDicomToBufferedImage(instanceData);
            slices.add(slice);
            width = Math.max(width, slice.getWidth());
            height = Math.max(height, slice.getHeight());
        }
        
        // 创建矢状面���建图像
        if (sliceIndex >= 0 && sliceIndex < height) {
            BufferedImage sagittalView = new BufferedImage(width, slices.size(), BufferedImage.TYPE_INT_RGB);
            
            // 对每个切片进行处理
            for (int z = 0; z < slices.size(); z++) {
                BufferedImage slice = slices.get(z);
                for (int x = 0; x < slice.getWidth(); x++) {
                    int rgb = slice.getRGB(x, sliceIndex);
                    sagittalView.setRGB(x, z, rgb);
                }
            }
            
            return convertToBytes(sagittalView, "PNG");
        }
        
        throw new IllegalArgumentException("Invalid slice index");
    }

    private byte[] fetchDicomInstance(String instanceId) {
        String url = orthancConfig.getServerUrl() + "/instances/" + instanceId + "/file";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(orthancConfig.getUsername(), orthancConfig.getPassword());
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<byte[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            byte[].class
        );
        
        return response.getBody();
    }

    private BufferedImage convertDicomToBufferedImage(byte[] dicomData) throws Exception {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("DICOM");
        ImageReader reader = readers.next();
        
        try (ByteArrayInputStream bis = new ByteArrayInputStream(dicomData);
             ImageInputStream iis = ImageIO.createImageInputStream(bis)) {
            
            reader.setInput(iis);
            DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
            return reader.read(0, param);
        } finally {
            reader.dispose();
        }
    }

    private BufferedImage createThumbnail(BufferedImage original, int width, int height) {
        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = thumbnail.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return thumbnail;
    }

    private byte[] convertToBytes(BufferedImage image, String format) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        }
    }

    private List<byte[]> fetchSeriesInstances(String seriesId) {
        String url = orthancConfig.getServerUrl() + "/series/" + seriesId + "/instances";
        
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<List> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            List.class
        );
        
        List<String> instanceIds = (List<String>) response.getBody();
        return instanceIds.stream()
            .map(this::fetchDicomInstance)
            .collect(Collectors.toList());
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(orthancConfig.getUsername(), orthancConfig.getPassword());
        return headers;
    }
} 