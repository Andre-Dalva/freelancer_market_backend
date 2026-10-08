package com.andretk.backend1.controllers;

import com.andretk.backend1.dtos.AcceptJobRequest;
import com.andretk.backend1.dtos.AcceptedJobDto;
import com.andretk.backend1.services.AcceptedJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/accepted")
@Tag(name = "Accepted jobs")
public class AcceptedJobController {
    private final AcceptedJobService acceptedService;

    @GetMapping
    @Operation(summary = "Returns all accepted jobs")
    public ResponseEntity<List<AcceptedJobDto>> getAllAcceptedJobs(
            @RequestParam(required = false, defaultValue = "id", name = "sort") String sort
    ){
        var jobs = acceptedService.getAllAcceptedJobs(sort);

        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns an accepted job")
    public ResponseEntity<AcceptedJobDto> getAcceptedJob(
            @Parameter(description = "The Id of the accepted job")  @PathVariable Long id
    ){
        var job = acceptedService.getAcceptedJob(id);

        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates an accepted job")
    public ResponseEntity<AcceptedJobDto> updateAcceptedJob(
            @Parameter(description = "The id of the accepted job") @PathVariable Long id,
            @RequestBody AcceptJobRequest request
            ){

        var application = acceptedService.updateAcceptedApplication(id, request);
        return ResponseEntity.ok(application);
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "closes an accepted job")
    public ResponseEntity<AcceptedJobDto> closeContract(
            @Parameter(description = "The id of the accepted job") @PathVariable Long id
    ){
        var application = acceptedService.closeContract(id);

        return  ResponseEntity.ok(application);
    }
}
