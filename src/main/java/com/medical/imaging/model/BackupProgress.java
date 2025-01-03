package com.medical.imaging.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BackupProgress {
    private final Integer progress;
    private final Long processedSize;
    private final LocalDateTime lastUpdateTime;
} 