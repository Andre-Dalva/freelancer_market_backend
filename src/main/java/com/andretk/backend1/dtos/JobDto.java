package com.andretk.backend1.dtos;

import com.andretk.backend1.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class JobDto {
    private Long id;
    private String title;
    private String requirements;
    private String deadlines;
    private String about;
    private String benefitsAndPayments;
    private Instant createdAt;
    private String recruiterName;
    private Long recruiterId;
    private JobStatus status;
}
