package com.medical.imaging.service.impl;

import com.medical.imaging.service.StorageService;
import com.medical.imaging.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {

    @Value("${app.storage.location}")
    private String storageLocation;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        rootLocation = Paths.get(storageLocation);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new BusinessException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + getFileExtension(file);
        Files.copy(file.getInputStream(), rootLocation.resolve(filename));
        return filename;
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void delete(String filename) throws IOException {
        Files.deleteIfExists(rootLocation.resolve(filename));
    }

    @Override
    public void deleteAll() {
        try {
            Files.walk(rootLocation)
                .filter(path -> !path.equals(rootLocation))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new BusinessException("Failed to delete " + path, e);
                    }
                });
        } catch (IOException e) {
            throw new BusinessException("Failed to clean storage", e);
        }
    }

    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf("."))
            : "";
    }
} 