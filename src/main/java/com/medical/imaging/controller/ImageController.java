package com.medical.imaging.controller;

import com.medical.imaging.dto.image.*;
import com.medical.imaging.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Tag(name = "Image Management", description = "APIs for managing medical images")
public class ImageController {

    private final ImageService imageService;

    @Operation(
        summary = "Upload a single DICOM image",
        description = "Upload a DICOM image file and process it for storage"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Image uploaded successfully",
        content = @Content(schema = @Schema(implementation = ImageUploadResult.class))
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResult> uploadImage(
        @Parameter(description = "DICOM image file to upload")
        @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(imageService.uploadImage(file));
    }

    @Operation(
        summary = "Get image details",
        description = "Retrieve details of a specific image by its instance ID"
    )
    @GetMapping("/{instanceId}")
    public ResponseEntity<ImageDTO> getImage(
        @Parameter(description = "Instance ID of the image")
        @PathVariable String instanceId
    ) {
        return ResponseEntity.ok(imageService.getImage(instanceId));
    }

    @Operation(
        summary = "Adjust image contrast",
        description = "Adjust the contrast and brightness of an image"
    )
    @PostMapping("/{instanceId}/contrast")
    public ResponseEntity<ImageDTO> adjustContrast(
        @Parameter(description = "Instance ID of the image")
        @PathVariable String instanceId,
        @Valid @RequestBody ContrastAdjustRequest request
    ) {
        return ResponseEntity.ok(imageService.adjustContrast(instanceId, request));
    }

    @Operation(
        summary = "Get image thumbnail",
        description = "Generate and retrieve a thumbnail of the specified image"
    )
    @GetMapping(value = "/{instanceId}/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getThumbnail(
        @Parameter(description = "Study ID")
        @RequestParam String studyId,
        @Parameter(description = "Instance ID of the image")
        @PathVariable String instanceId
    ) {
        return ResponseEntity.ok(imageService.getThumbnail(studyId, instanceId));
    }

    @Operation(
        summary = "Export images",
        description = "Export multiple images in the specified format"
    )
    @PostMapping("/export")
    public ResponseEntity<BatchExportResult> exportImages(
        @Valid @RequestBody BatchExportRequest request
    ) {
        return ResponseEntity.ok(imageService.exportImages(request));
    }
} 