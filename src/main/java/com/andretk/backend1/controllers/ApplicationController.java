package com.andretk.backend1.controllers;

import com.andretk.backend1.dtos.ApplicationDto;
import com.andretk.backend1.services.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/applications")
@Tag(name = "Applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    @Operation(summary = "Returns all applications")
    public ResponseEntity<List<ApplicationDto>> getAllApplications(
            @RequestParam(required = false, defaultValue = "id", name = "sort") String sort
    ){
        var allApplications = applicationService.getAllApplications(sort);

        return ResponseEntity.ok(allApplications);
    }

    @GetMapping("/{id}")
    @Operation(summary = "returns an applications")
    public ResponseEntity<ApplicationDto> getApplication(
           @Parameter(description = "The id of the application") @PathVariable Long id){
        return ResponseEntity.ok(applicationService.getApplication(id));
    }

    @GetMapping("/freelancer/{id}")
    @Operation(summary = "Returns all applications made by one freelancer")
    public ResponseEntity<List<ApplicationDto>> getApplicationsByFreelancerId(
            @Parameter(description = "The id of the freelancer") @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "id", name = "sort") String sort
    ){
        var freelancerApplications = applicationService.getApplicationsByFreelancerId(id, sort);

        return ResponseEntity.ok(freelancerApplications);
    }

    @GetMapping("/job/{id}")
    @Operation(summary = "Returns all applications made to a specific job")
    public ResponseEntity<List<ApplicationDto>> getJobApplications(
            @Parameter(description = "The id of the job") @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "id", name = "sort") String sort
    ){
        var jobApplications = applicationService.getJobApplications(id, sort);

        return ResponseEntity.ok(jobApplications);
    }

    @PostMapping("/job/{jobId}")
    @Operation(summary = "Applies to a job (using the freelancer Id)")
    public ResponseEntity<ApplicationDto> applyToJob(
            @Parameter(description = "The id of the job") @PathVariable Long jobId,
            UriComponentsBuilder uriBuilder
    ){

        var newApplication = applicationService.createApplication(jobId);
        var uri = uriBuilder.path("/applications/{id}")
                .buildAndExpand(newApplication.getId()).toUri();

        return ResponseEntity.created(uri).body(newApplication);
    }

    @PatchMapping("/{id}/accept")
    @Operation(summary = "Accepts an application")
    public ResponseEntity<ApplicationDto> acceptNewApplication(
            @Parameter(description = "The id of the application") @PathVariable Long id
    ){
        var acceptedJob = applicationService.acceptNewApplication(id);

        return ResponseEntity.ok(acceptedJob);
    }

    @PatchMapping("/{id}/reject")
    @Operation(summary = "Rejects an application")
    public ResponseEntity<ApplicationDto> rejectApplication(
            @Parameter(description = "The id of the application") @PathVariable Long id
    ){
        var application = applicationService.rejectApplication(id);

        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes an application")
    public ResponseEntity<Void> deleteApplications(
            @Parameter(description = "The id of the application") @PathVariable Long id
    ){
        applicationService.deleteApplication(id);

        return ResponseEntity.noContent().build();
    }
}
