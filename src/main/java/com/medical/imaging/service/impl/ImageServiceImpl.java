package com.medical.imaging.service.impl;

import com.medical.imaging.dto.image.*;
import com.medical.imaging.entity.Image;
import com.medical.imaging.entity.ImageUploadTask;
import com.medical.imaging.repository.ImageRepository;
import com.medical.imaging.repository.ImageUploadTaskRepository;
import com.medical.imaging.service.ImageService;
import com.medical.imaging.exception.ResourceNotFoundException;
import com.medical.imaging.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageUploadTaskRepository uploadTaskRepository;
    private final ConcurrentHashMap<String, Integer> uploadProgress = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public ImageUploadResult uploadImage(MultipartFile file) {
        String uploadId = UUID.randomUUID().toString();
        
        try {
            // 创建上传任务
            ImageUploadTask task = new ImageUploadTask();
            task.setUploadId(uploadId);
            task.setStatus("PROCESSING");
            task.setProgress(0);
            uploadTaskRepository.save(task);

            // 保存文件
            String filePath = saveFile(file);
            task.setFilePath(filePath);

            // 解析DICOM文件
            Image image = parseDicomFile(filePath);
            task.setInstanceId(image.getInstanceId());

            // 保存图像信息
            imageRepository.save(image);

            // 更新任务状态
            task.setStatus("COMPLETED");
            task.setProgress(100);
            uploadTaskRepository.save(task);

            return ImageUploadResult.builder()
                .uploadId(uploadId)
                .instanceId(image.getInstanceId())
                .status("COMPLETED")
                .progress(100)
                .build();

        } catch (Exception e) {
            log.error("Failed to upload image", e);
            ImageUploadTask task = uploadTaskRepository.findById(uploadId)
                .orElseThrow(() -> new ResourceNotFoundException("Upload task not found"));
            task.setStatus("FAILED");
            task.setMessage(e.getMessage());
            uploadTaskRepository.save(task);

            throw new BusinessException("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    public ImageDTO getImage(String instanceId) {
        Image image = imageRepository.findById(instanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        return convertToDTO(image);
    }

    @Override
    public List<ImageDTO> getSeriesImages(String seriesId) {
        List<Image> images = imageRepository.findBySeriesIdOrdered(seriesId);
        return images.stream()
            .map(this::convertToDTO)
            .toList();
    }

    @Override
    @Transactional
    public ImageDTO adjustContrast(String instanceId, ContrastAdjustRequest request) {
        Image image = imageRepository.findById(instanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
            
        // 实现对比度调整逻辑
        // 这里需要调用图像处理库来实现具体的调整功能
        
        return convertToDTO(image);
    }

    @Override
    @Transactional
    public BatchUploadResult uploadImages(List<MultipartFile> files) {
        String batchId = UUID.randomUUID().toString();
        List<ImageUploadResult> results = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (MultipartFile file : files) {
            try {
                ImageUploadResult result = uploadImage(file);
                results.add(result);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
                results.add(ImageUploadResult.builder()
                    .status("FAILED")
                    .message(e.getMessage())
                    .build());
                failureCount++;
            }
        }

        return BatchUploadResult.builder()
            .batchId(batchId)
            .totalFiles(files.size())
            .successCount(successCount)
            .failureCount(failureCount)
            .status(failureCount == 0 ? "COMPLETED" : "PARTIAL")
            .results(results)
            .build();
    }

    @Override
    public UploadProgress getUploadProgress(String uploadId) {
        ImageUploadTask task = uploadTaskRepository.findById(uploadId)
            .orElseThrow(() -> new ResourceNotFoundException("Upload task not found"));
            
        return UploadProgress.builder()
            .uploadId(uploadId)
            .status(task.getStatus())
            .progress(task.getProgress())
            .message(task.getMessage())
            .build();
    }

    @Override
    @Transactional
    public void pauseUpload(String taskId) {
        ImageUploadTask task = uploadTaskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Upload task not found"));
            
        if ("PROCESSING".equals(task.getStatus())) {
            task.setStatus("PAUSED");
            uploadTaskRepository.save(task);
        } else {
            throw new BusinessException("Task cannot be paused in current status: " + task.getStatus());
        }
    }

    @Override
    @Transactional
    public void resumeUpload(String taskId) {
        ImageUploadTask task = uploadTaskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Upload task not found"));
            
        if ("PAUSED".equals(task.getStatus())) {
            task.setStatus("PROCESSING");
            uploadTaskRepository.save(task);
            // 重新启动上传处理
            processUpload(task);
        } else {
            throw new BusinessException("Task cannot be resumed in current status: " + task.getStatus());
        }
    }

    @Override
    public ImageDTO adjustWindowLevel(String instanceId, WindowLevelRequest request) {
        Image image = imageRepository.findById(instanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
            
        // 使用图像处理库调整窗宽窗位
        try {
            String outputPath = adjustImageWindowLevel(
                image.getStoragePath(),
                request.getWindowWidth(),
                request.getWindowCenter()
            );
            image.setStoragePath(outputPath);
            return convertToDTO(image);
        } catch (Exception e) {
            throw new BusinessException("Failed to adjust window level: " + e.getMessage());
        }
    }

    @Override
    public byte[] getThumbnail(String studyId, String instanceId) {
        Image image = imageRepository.findById(instanceId)
            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
            
        try {
            return generateThumbnail(image.getStoragePath());
        } catch (Exception e) {
            throw new BusinessException("Failed to generate thumbnail: " + e.getMessage());
        }
    }

    @Override
    public BatchExportResult exportImages(BatchExportRequest request) {
        String exportId = UUID.randomUUID().toString();
        List<String> exportedFiles = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();

        for (String instanceId : request.getInstanceIds()) {
            try {
                Image image = imageRepository.findById(instanceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + instanceId));
                    
                String exportedPath = exportImage(
                    image.getStoragePath(),
                    request.getFormat(),
                    request.getExportPath()
                );
                exportedFiles.add(exportedPath);
            } catch (Exception e) {
                log.error("Failed to export image: {}", instanceId, e);
                failedFiles.add(instanceId);
            }
        }

        return BatchExportResult.builder()
            .exportId(exportId)
            .totalFiles(request.getInstanceIds().size())
            .processedFiles(exportedFiles.size())
            .status(failedFiles.isEmpty() ? "COMPLETED" : "PARTIAL")
            .exportPath(request.getExportPath())
            .exportedFiles(exportedFiles)
            .failedFiles(failedFiles)
            .build();
    }

    private String saveFile(MultipartFile file) throws IOException {
        // 实现文件保存逻辑
        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + ".dcm";
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }

    private Image parseDicomFile(String filePath) throws IOException {
        // 实现DICOM文件解析逻辑
        // 使用dcm4che库解析DICOM文件并提取相关信息
        try (DicomInputStream dis = new DicomInputStream(new File(filePath))) {
            Attributes attrs = dis.readDataset();
            
            Image image = new Image();
            image.setInstanceId(attrs.getString(Tag.SOPInstanceUID));
            image.setSeriesId(attrs.getString(Tag.SeriesInstanceUID));
            image.setStudyId(attrs.getString(Tag.StudyInstanceUID));
            image.setPatientId(attrs.getString(Tag.PatientID));
            image.setModality(attrs.getString(Tag.Modality));
            image.setStoragePath(filePath);
            // ... 设置其他属性
            
            return image;
        }
    }

    private ImageDTO convertToDTO(Image image) {
        return ImageDTO.builder()
            .instanceId(image.getInstanceId())
            .seriesId(image.getSeriesId())
            .studyId(image.getStudyId())
            .patientId(image.getPatientId())
            .modality(image.getModality())
            .imageType(image.getImageType())
            .instanceNumber(image.getInstanceNumber())
            .storagePath(image.getStoragePath())
            .fileSize(image.getFileSize())
            .width(image.getWidth())
            .height(image.getHeight())
            .bitsAllocated(image.getBitsAllocated())
            .transferSyntax(image.getTransferSyntax())
            .acquisitionDateTime(image.getAcquisitionDateTime())
            .createdAt(image.getCreatedAt())
            .build();
    }

    private void processUpload(ImageUploadTask task) {
        // 实现异步上传处理逻辑
    }

    private String adjustImageWindowLevel(String imagePath, int windowWidth, int windowCenter) {
        // 实现窗宽窗位调整逻辑
        return "adjusted_" + imagePath;
    }

    private byte[] generateThumbnail(String imagePath) throws IOException {
        // 实现缩略图生成逻辑
        return new byte[0];
    }

    private String exportImage(String imagePath, ImageFormat format, String exportPath) {
        // 实现图像导出逻辑
        return exportPath + "/exported_image." + format.name().toLowerCase();
    }

    // ... 其他方法的实现
} 