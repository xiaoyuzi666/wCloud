package com.medical.imaging.service;

import com.medical.imaging.dto.image.RotateRequest;
import com.medical.imaging.dto.image.UploadProgress;
import com.medical.imaging.enums.ImageFormat;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile file);
    byte[] getImage(String imageId);
    byte[] getImageThumbnail(String imageId);
    byte[] rotateImage(String imageId, RotateRequest request);
    byte[] convertImage(String imageId, ImageFormat targetFormat);
    UploadProgress getUploadProgress(String uploadId);
    void deleteImage(String imageId);
} 