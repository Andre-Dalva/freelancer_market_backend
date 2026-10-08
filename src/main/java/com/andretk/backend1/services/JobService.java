package com.andretk.backend1.services;

import com.andretk.backend1.dtos.CreateJobRequest;
import com.andretk.backend1.dtos.JobDto;
import com.andretk.backend1.enums.JobStatus;
import com.andretk.backend1.exceptions.ResourceConflictException;
import com.andretk.backend1.exceptions.ResourceNotFoundException;
import com.andretk.backend1.mappers.JobMapper;
import com.andretk.backend1.repositories.JobRepository;
import com.andretk.backend1.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final UserRepository recruiterRepository;
    private final UtilityService utilityService;

    @Transactional(readOnly = true)
    public List<JobDto> getAllJobs(String sort){
        if (!Set.of("title", "deadline", "createdAt").contains(sort))
            sort = "createdAt";

        return jobRepository.findAll(Sort.by(sort)).stream().map(jobMapper::toJobDto).toList();
    }

    @Transactional(readOnly = true)
    public JobDto getJobById(Long id){
        var job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not found, check id")
        );

        return jobMapper.toJobDto(job);
    }

    @Transactional(readOnly = true)
    public List<JobDto> getJobByRecruiterId(Long id){

        return jobRepository.findAllByRecruiterId(id)
                .stream().map(jobMapper::toJobDto).toList();
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @Transactional
    public JobDto createJob(CreateJobRequest request){
        var recruiterId = utilityService.getCurrentUserId();

        var recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + recruiterId + " not " +
                        "found"));

        var job = jobMapper.toJob(request);
        job.addRecruiter(recruiter);
        jobRepository.save(job);

        return jobMapper.toJobDto(job);
    }

    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    @Transactional
    public void deleteJob(Long id){
        var job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not found, check id")
        );

        utilityService.validateOwnerOrAdmin(job.getRecruiter().getId());

        jobRepository.delete(job);
    }

    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    @Transactional
    public JobDto updateJob(Long id, CreateJobRequest request){
        var job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not found, check id")
        );

        utilityService.validateOwnerOrAdmin(job.getRecruiter().getId());

        jobMapper.update(request, job);

        return jobMapper.toJobDto(job);
    }


    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    @Transactional
    public JobDto closeJob(Long id){
        var job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );

        utilityService.validateOwnerOrAdmin(job.getRecruiter().getId());

        if (job.getStatus().equals(JobStatus.CLOSED)) throw new ResourceConflictException("Job already closed");

        job.setStatus(JobStatus.CLOSED);

        return jobMapper.toJobDto(job);
    }


}
