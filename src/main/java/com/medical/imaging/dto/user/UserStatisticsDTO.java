package com.medical.imaging.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UserStatisticsDTO {
    private long totalUsers;
    private long activeUsers;
    private long disabledUsers;
    private long newUsersThisMonth;
    private Map<String, Long> usersByDepartment;
    private Map<String, Long> usersByRole;
} 