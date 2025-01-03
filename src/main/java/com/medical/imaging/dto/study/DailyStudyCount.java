package com.medical.imaging.dto.study;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyStudyCount {
    private LocalDate date;
    private Long count;
} 