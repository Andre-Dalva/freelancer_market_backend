package com.andretk.backend1.services;

import com.andretk.backend1.dtos.ApplicationDto;
import com.andretk.backend1.entities.AcceptedJobs;
import com.andretk.backend1.entities.Application;
import com.andretk.backend1.enums.ApplicationStatus;
import com.andretk.backend1.enums.JobStatus;
import com.andretk.backend1.enums.UserRole;
import com.andretk.backend1.exceptions.ForbiddenException;
import com.andretk.backend1.exceptions.ResourceConflictException;
import com.andretk.backend1.exceptions.ResourceNotFoundException;
import com.andretk.backend1.mappers.ApplicationMapper;
import com.andretk.backend1.repositories.ApplicationRepository;
import com.andretk.backend1.repositories.JobRepository;
import com.andretk.backend1.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final UtilityService utilityService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<ApplicationDto> getAllApplications(String sort){
        sort = checkSort(sort);
        var applications = applicationRepository.findAll(Sort.by(sort));

        return applications.stream().map(applicationMapper::toDto).toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','FREELANCER','RECRUITER')")
    @Transactional(readOnly = true)
    public ApplicationDto getApplication(Long id){
        var application = applicationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Application not found")
        );

        var isFreelancer = utilityService.isOwner(application.getFreelancer().getId());
        var isRecruiter = utilityService.isOwner(application.getJob().getRecruiter().getId());
        var isAdmin = utilityService.matchesThisRole(UserRole.ADMIN);

        if (!isFreelancer && !isRecruiter && !isAdmin){
            throw new ForbiddenException("You don't have authorization to execute this action");
        }

        return applicationMapper.toDto(application);
    }

    @PreAuthorize("hasAnyRole('ADMIN','FREELANCER')")
    @Transactional(readOnly = true)
    public List<ApplicationDto> getApplicationsByFreelancerId(
            Long id, String sort
    ){
        userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        utilityService.validateOwnerOrAdmin(id);

        sort = checkSort(sort);
        var freelancerApplications = applicationRepository.findAllByFreelancerId(id, Sort.by(sort));

        return freelancerApplications.stream().map(applicationMapper::toDto).toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER')")
    @Transactional(readOnly = true)
    public List<ApplicationDto> getJobApplications(
            Long id, String sort
    ){
        var job = jobRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );

        utilityService.validateOwnerOrAdmin(job.getRecruiter().getId());

        sort = checkSort(sort);
        var jobApplications = applicationRepository.findAllByJobId(id, Sort.by(sort));

        return jobApplications.stream().map(applicationMapper::toDto).toList();
    }


    @PreAuthorize("hasAnyRole('FREELANCER')")
    @Transactional
    public ApplicationDto createApplication(Long jobId){
        var userId = utilityService.getCurrentUserId();

        if (applicationRepository.existsByFreelancerIdAndJobId(userId, jobId))
            throw new ResourceConflictException("User with id: " + userId + " already applied");

        var job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found, check id")
        );
        var user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        if (!job.getStatus().equals(JobStatus.OPEN)) throw new ResourceConflictException("Job no longer available");

        Application application = Application.builder().freelancer(user).job(job).build();

        applicationRepository.save(application);

        return applicationMapper.toDto(application);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @Transactional
    public ApplicationDto acceptNewApplication(Long id){
        var application = applicationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Application not found")
        );

        if(!utilityService.isOwner(application.getJob().getRecruiter().getId()))
            throw new ForbiddenException("You don't have authorization to execute this action");

        if (!application.getStatus().equals(ApplicationStatus.PENDING))
            throw new ResourceConflictException(
                "Application is not pending, status: " + application.getStatus()
            );
        if (!application.getJob().getStatus().equals(JobStatus.OPEN))
            throw new ResourceConflictException("Job no longer open");

        var otherApplications = applicationRepository.findAllByJobIdAndStatus(application.getJob().getId(),
                ApplicationStatus.PENDING);

        application.setStatus(ApplicationStatus.ACCEPTED);
        application.getJob().setStatus(JobStatus.ON_GOING);
        AcceptedJobs accept = AcceptedJobs.builder().application(application).build();
        application.setAcceptedJob(accept);

        otherApplications.stream()
                .filter(app -> !app.getId().equals(application.getId()))
                .forEach(app -> app.setStatus(ApplicationStatus.REJECTED));

        return applicationMapper.toDto(application);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @Transactional
    public ApplicationDto rejectApplication(Long id){
        var application = applicationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Application not found")
        );

        if(!utilityService.isOwner(application.getJob().getRecruiter().getId()))
            throw new ForbiddenException("You don't have authorization to execute this action");

        if (application.getStatus() == ApplicationStatus.REJECTED)
            throw new ResourceConflictException("Application already rejected");

        if (application.getStatus() == ApplicationStatus.ACCEPTED)
            throw new ResourceConflictException("Cannot reject accepted applications");

        application.setStatus(ApplicationStatus.REJECTED);

        return applicationMapper.toDto(application);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @Transactional
    public void deleteApplication(Long id){
        var application = applicationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Application not found")
        );

        if(!utilityService.isOwner(application.getJob().getRecruiter().getId()))
            throw new ForbiddenException("You don't have authorization to execute this action");

        applicationRepository.delete(application);
    }

    public String checkSort(String sort){
        if (!Set.of("id", "createdAt","status").contains(sort))
            sort = "createdAt";
        return sort;
    }

}
