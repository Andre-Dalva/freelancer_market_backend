package com.andretk.backend1.dtos;

import com.andretk.backend1.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class ApplicationDto {
    private Long id;
    private JobDto job;
    private UserDto freelancer;
    private Instant createdAt;
    private ApplicationStatus status;
}
