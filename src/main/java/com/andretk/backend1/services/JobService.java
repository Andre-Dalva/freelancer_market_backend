package com.andretk.backend1.services;

import com.andretk.backend1.dtos.CreateJobRequest;
import com.andretk.backend1.dtos.JobDto;
import com.andretk.backend1.enums.JobStatus;
import com.andretk.backend1.enums.UserRole;
import com.andretk.backend1.exceptions.ResourceConflictException;
import com.andretk.backend1.exceptions.ResourceNotFoundException;
import com.andretk.backend1.mappers.JobMapper;
import com.andretk.backend1.repositories.JobRepository;
import com.andretk.backend1.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
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

    @Transactional(readOnly = true)
    public List<JobDto> getAllJobs(String sort){
        if (!Set.of("title", "deadlines", "createdAt").contains(sort))
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

    @Transactional
    public JobDto createJob(Long recruiterId, CreateJobRequest request){

        var recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + recruiterId + " not " +
                        "found"));
        if (recruiter.getRole().equals(UserRole.FREELANCER)) throw new ResourceConflictException("Only Admins or " +
                "Recruiter can create jobs");
        var job = jobMapper.toJob(request);
        job.addRecruiter(recruiter);
        jobRepository.save(job);

        return jobMapper.toJobDto(job);
    }

    @Transactional
    public void deleteJob(Long id){
        var job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not found, check id")
        );

        jobRepository.delete(job);
    }

    @Transactional
    public JobDto updateJob(Long id, CreateJobRequest request){
        var job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not found, check id")
        );

        jobMapper.update(request, job);

        return jobMapper.toJobDto(job);
    }

    @Transactional
    public JobDto closeJob(Long id){
        var job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );

        if (job.getStatus().equals(JobStatus.CLOSED)) throw new ResourceConflictException("Job already closed");

        job.setStatus(JobStatus.CLOSED);

        return jobMapper.toJobDto(job);
    }
}
