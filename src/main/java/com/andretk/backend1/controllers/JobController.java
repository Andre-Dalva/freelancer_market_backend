package com.andretk.backend1.controllers;

import com.andretk.backend1.dtos.CreateJobRequest;
import com.andretk.backend1.dtos.JobDto;
import com.andretk.backend1.services.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/jobs")
@Tag(name = "Jobs")
public class JobController {
    private final JobService jobService;

    @GetMapping
    @Operation(summary = "Returns all jobs")
    public ResponseEntity<List<JobDto>> getAllJobs(
            @RequestParam(required = false, defaultValue = "createdAt", name = "sort") String sort
    ) {
        var allJobs = jobService.getAllJobs(sort);

        return ResponseEntity.ok(allJobs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns one job")
    public ResponseEntity<JobDto> getJobById(@Parameter(description = "The id of the job") @PathVariable Long id){
        var job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/recruiter/{id}")
    public ResponseEntity<List<JobDto>> getJobByRecruiterId(
            @Parameter(description = "The id of the recruiter") @PathVariable Long id
    ){
        var jobs = jobService.getJobByRecruiterId(id);

        return ResponseEntity.ok(jobs);
    }

    @PostMapping
    @Operation(summary = "Creates a job (using recruiter's Id)")
    public ResponseEntity<JobDto> createJob(
            @Valid @RequestBody CreateJobRequest request,
            UriComponentsBuilder uriBuilder
    ){
        var newJob = jobService.createJob(request);

        var uri = uriBuilder.path("/jobs/{id}")
                .buildAndExpand(newJob.getId()).toUri();

        return ResponseEntity.created(uri).body(newJob);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a job")
    public ResponseEntity<Void> deleteJob(
            @Parameter(description = "The id of the job") @PathVariable Long id){
        jobService.deleteJob(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a job")
    public ResponseEntity<JobDto> updateJob(
            @Parameter(description = "The id of the job") @PathVariable Long id,
            @Valid @RequestBody CreateJobRequest request
    ){
        var updatedJob = jobService.updateJob(id,request);

        return ResponseEntity.ok(updatedJob);
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "Closes a job")
    public  ResponseEntity<JobDto> closeJob(
            @Parameter(description = "The id of the job") @PathVariable Long id
    ){
        var closedJob = jobService.closeJob(id);

        return ResponseEntity.ok(closedJob);
    }
}
