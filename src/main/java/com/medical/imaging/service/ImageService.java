package com.medical.imaging.service;

import com.medical.imaging.dto.image.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ImageUploadResult uploadImage(MultipartFile file);
    BatchUploadResult uploadImages(List<MultipartFile> files);
    UploadProgress getUploadProgress(String uploadId);
    void pauseUpload(String taskId);
    void resumeUpload(String taskId);
    void deleteUpload(String taskId);
    
    ImageDTO getImage(String instanceId);
    List<ImageDTO> getSeriesImages(String seriesId);
    ImageDTO adjustContrast(String instanceId, ContrastAdjustRequest request);
    ImageDTO adjustWindowLevel(String instanceId, WindowLevelRequest request);
    ImageDTO rotateImage(String instanceId, RotateRequest request);
    byte[] getThumbnail(String studyId, String instanceId);
    
    byte[] exportImage(String instanceId, ImageFormat format);
    BatchExportResult exportImages(BatchExportRequest request);
} 