package com.andretk.backend1.services;

import com.andretk.backend1.dtos.AcceptJobRequest;
import com.andretk.backend1.dtos.AcceptedJobDto;
import com.andretk.backend1.enums.AcceptedApplicationStatus;
import com.andretk.backend1.enums.JobStatus;
import com.andretk.backend1.enums.UserRole;
import com.andretk.backend1.exceptions.ForbiddenException;
import com.andretk.backend1.exceptions.ResourceConflictException;
import com.andretk.backend1.exceptions.ResourceNotFoundException;
import com.andretk.backend1.mappers.AcceptedJobsMapper;
import com.andretk.backend1.repositories.AcceptedJobRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AcceptedJobService {
    private final AcceptedJobRepository acceptedRepository;
    private final AcceptedJobsMapper acceptedMapper;
    private final UtilityService utilityService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<AcceptedJobDto> getAllAcceptedJobs(String sort){
        sort = checkSort(sort);
        var acceptedApplication = acceptedRepository.findAll(Sort.by(sort));

        return acceptedApplication.stream().map(acceptedMapper::toDto).toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','FREELANCER','RECRUITER')")
    @Transactional(readOnly = true)
    public AcceptedJobDto getAcceptedJob(Long id){
        var acceptedApplication = acceptedRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not accepted or doesn't exist")
        );

        var isFreelancer = utilityService.isOwner(acceptedApplication.getApplication().getFreelancer().getId());
        var isRecruiter = utilityService.isOwner(acceptedApplication.getApplication().getJob().getRecruiter().getId());
        var isAdmin = utilityService.matchesThisRole(UserRole.ADMIN);

        if (!isAdmin && !isFreelancer && !isRecruiter)
            throw new ForbiddenException("You are not authorized to execute this action");

        return acceptedMapper.toDto(acceptedApplication);
    }

    @PreAuthorize("hasAnyRole('FREELANCER','RECRUITER')")
    @Transactional
    public AcceptedJobDto updateAcceptedApplication(Long id, AcceptJobRequest request){
        var acceptedApplication = acceptedRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Application not found")
        );

        var isFreelancer = utilityService.isOwner(acceptedApplication.getApplication().getFreelancer().getId());
        var isRecruiter = utilityService.isOwner(acceptedApplication.getApplication().getJob().getRecruiter().getId());

        if (!isFreelancer && !isRecruiter) throw new ForbiddenException("You are not authorized to execute this action");

        if (acceptedApplication.getStatus() == AcceptedApplicationStatus.FINISHED)
            throw new ResourceConflictException("Cannot modify a finished contract");

        acceptedMapper.update(request, acceptedApplication);

        return acceptedMapper.toDto(acceptedApplication);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @Transactional
    public AcceptedJobDto closeContract(Long id){

        var acceptedApplication = acceptedRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contract not found")
        );

        if (!utilityService.isOwner(acceptedApplication.getApplication().getJob().getRecruiter().getId()))
            throw new ForbiddenException("You do not have authorization to execute this action");

        if (acceptedApplication.getStatus() == AcceptedApplicationStatus.FINISHED)
            throw new ResourceConflictException("Job is already closed");

        acceptedApplication.setStatus(AcceptedApplicationStatus.FINISHED);
        acceptedApplication.setFinishedAt(Instant.now());
        acceptedApplication.getApplication().getJob().setStatus(JobStatus.FINISHED);

        return acceptedMapper.toDto(acceptedApplication);
    }

    public String checkSort(String sort){
        if (!Set.of("id", "assignedAt").contains(sort))
            sort = "id";
        return sort;
    }
}
