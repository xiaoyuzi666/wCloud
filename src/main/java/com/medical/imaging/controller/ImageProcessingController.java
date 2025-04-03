package com.medical.imaging.controller;

import com.medical.imaging.service.ImageProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageProcessingController {

    private final ImageProcessingService imageProcessingService;

    @GetMapping("/thumbnail/{studyId}/{instanceId}")
    public ResponseEntity<byte[]> getThumbnail(
            @PathVariable String studyId,
            @PathVariable String instanceId) throws Exception {
        byte[] thumbnail = imageProcessingService.generateThumbnail(studyId, instanceId);
        return createImageResponse(thumbnail, MediaType.IMAGE_PNG);
    }

    @PostMapping("/contrast/{instanceId}")
    public ResponseEntity<byte[]> adjustContrast(
            @PathVariable String instanceId,
            @RequestParam double factor) throws Exception {
        byte[] processed = imageProcessingService.adjustContrast(instanceId, factor);
        return createImageResponse(processed, MediaType.IMAGE_PNG);
    }

    @PostMapping("/rotate/{instanceId}")
    public ResponseEntity<byte[]> rotateImage(
            @PathVariable String instanceId,
            @RequestParam int degrees) throws Exception {
        byte[] rotated = imageProcessingService.rotateImage(instanceId, degrees);
        return createImageResponse(rotated, MediaType.IMAGE_PNG);
    }

    @GetMapping("/export/{instanceId}")
    public ResponseEntity<byte[]> exportDicom(@PathVariable String instanceId) throws Exception {
        byte[] dicom = imageProcessingService.exportDicom(instanceId);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/dicom"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.dcm\"")
            .body(dicom);
    }

    @PostMapping("/window-level/{instanceId}")
    public ResponseEntity<byte[]> adjustWindowLevel(
            @PathVariable String instanceId,
            @RequestParam int windowWidth,
            @RequestParam int windowCenter) throws Exception {
        byte[] processed = imageProcessingService.adjustWindowLevel(instanceId, windowWidth, windowCenter);
        return createImageResponse(processed, MediaType.IMAGE_PNG);
    }

    @GetMapping("/mpr/axial/{seriesId}/{sliceIndex}")
    public ResponseEntity<byte[]> getAxialView(
            @PathVariable String seriesId,
            @PathVariable int sliceIndex) throws Exception {
        byte[] image = imageProcessingService.generateAxialView(seriesId, sliceIndex);
        return createImageResponse(image, MediaType.IMAGE_PNG);
    }

    @GetMapping("/mpr/coronal/{seriesId}/{sliceIndex}")
    public ResponseEntity<byte[]> getCoronalView(
            @PathVariable String seriesId,
            @PathVariable int sliceIndex) throws Exception {
        byte[] image = imageProcessingService.generateCoronalView(seriesId, sliceIndex);
        return createImageResponse(image, MediaType.IMAGE_PNG);
    }

    @GetMapping("/mpr/sagittal/{seriesId}/{sliceIndex}")
    public ResponseEntity<byte[]> getSagittalView(
            @PathVariable String seriesId,
            @PathVariable int sliceIndex) throws Exception {
        byte[] image = imageProcessingService.generateSagittalView(seriesId, sliceIndex);
        return createImageResponse(image, MediaType.IMAGE_PNG);
    }

    private ResponseEntity<byte[]> createImageResponse(byte[] imageData, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
} 