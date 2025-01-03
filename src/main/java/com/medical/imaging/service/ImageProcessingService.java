package com.medical.imaging.service;

public interface ImageProcessingService {
    byte[] generateThumbnail(String studyId, String instanceId) throws Exception;
    byte[] adjustContrast(String instanceId, double factor) throws Exception;
    byte[] rotateImage(String instanceId, int degrees) throws Exception;
    byte[] exportDicom(String instanceId) throws Exception;
    byte[] adjustWindowLevel(String instanceId, int windowWidth, int windowCenter) throws Exception;
    byte[] generateAxialView(String seriesId, int sliceIndex) throws Exception;
    byte[] generateCoronalView(String seriesId, int sliceIndex) throws Exception;
    byte[] generateSagittalView(String seriesId, int sliceIndex) throws Exception;
} 