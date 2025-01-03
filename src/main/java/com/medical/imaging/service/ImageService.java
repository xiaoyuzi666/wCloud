package com.medical.imaging.service;

import com.medical.imaging.dto.image.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImageService {
    ImageUploadResult uploadImage(MultipartFile file);
    ImageDTO getImage(String instanceId);
    List<ImageDTO> getSeriesImages(String seriesId);
    ImageDTO adjustContrast(String instanceId, ContrastAdjustRequest request);
    BatchUploadResult uploadImages(List<MultipartFile> files);
    UploadProgress getUploadProgress(String uploadId);
    void pauseUpload(String taskId);
    void resumeUpload(String taskId);
    ImageDTO adjustWindowLevel(String instanceId, WindowLevelRequest request);
    byte[] getThumbnail(String studyId, String instanceId);
    BatchExportResult exportImages(BatchExportRequest request);
} 