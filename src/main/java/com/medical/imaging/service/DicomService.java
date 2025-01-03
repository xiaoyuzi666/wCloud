package com.medical.imaging.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface DicomService {
    void processDicomFile(MultipartFile file) throws IOException;
} 