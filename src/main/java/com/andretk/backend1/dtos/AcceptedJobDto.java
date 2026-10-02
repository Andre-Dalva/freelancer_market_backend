package com.andretk.backend1.dtos;

import com.andretk.backend1.enums.AcceptedApplicationStatus;
import com.andretk.backend1.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class AcceptedJobDto {
    private Long id;
    private AcceptedApplicationStatus status;
    private String optionalLink1;
    private String optionalLink2;
    private String comments;
    private Instant assignedAt;
    private Instant finishedAt;
}
