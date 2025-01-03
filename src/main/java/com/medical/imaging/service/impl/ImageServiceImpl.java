package com.medical.imaging.service.impl;

import com.medical.imaging.dto.image.RotateRequest;
import com.medical.imaging.dto.image.UploadProgress;
import com.medical.imaging.enums.ImageFormat;
import com.medical.imaging.exception.BusinessException;
import com.medical.imaging.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Override
    public String uploadImage(MultipartFile file) {
        // TODO: 实现图片上传逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public byte[] getImage(String imageId) {
        // TODO: 实现获取图片逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public byte[] getImageThumbnail(String imageId) {
        // TODO: 实现获取缩略图逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public byte[] rotateImage(String imageId, RotateRequest request) {
        // TODO: 实现图片旋转逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public byte[] convertImage(String imageId, ImageFormat targetFormat) {
        // TODO: 实现图片格式转换逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public UploadProgress getUploadProgress(String uploadId) {
        // TODO: 实现获取上传进度逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteImage(String imageId) {
        // TODO: 实现删除图片逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }
} 