package com.andretk.backend1.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateJobRequest {
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "requirements cannot be empty")
    private String requirements;
    private String deadlines;

    @NotBlank(message = "about is required")
    private String about;
    private String benefitsAndPayments;
}
