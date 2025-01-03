package com.medical.imaging.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.awt.image.BufferedImage;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryableImageProcessor {

    private final DicomImageProcessor imageProcessor;

    @Retryable(
        value = {IOException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public BufferedImage processImageWithRetry(String imagePath, int windowWidth, int windowCenter) throws IOException {
        try {
            return imageProcessor.adjustWindowLevel(imagePath, windowWidth, windowCenter);
        } catch (IOException e) {
            log.error("Failed to process image: {}, attempt will be retried", imagePath, e);
            throw e;
        }
    }
} 