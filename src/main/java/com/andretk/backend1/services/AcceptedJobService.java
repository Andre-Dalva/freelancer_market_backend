package com.andretk.backend1.services;

import com.andretk.backend1.dtos.AcceptJobRequest;
import com.andretk.backend1.dtos.AcceptedJobDto;
import com.andretk.backend1.enums.AcceptedApplicationStatus;
import com.andretk.backend1.enums.JobStatus;
import com.andretk.backend1.exceptions.ResourceNotFoundException;
import com.andretk.backend1.mappers.AcceptedJobsMapper;
import com.andretk.backend1.repositories.AcceptedJobRepository;
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

    @Transactional(readOnly = true)
    public List<AcceptedJobDto> getAllAcceptedJobs(String sort){
        sort = checkSort(sort);
        var acceptedJob = acceptedRepository.findAll(Sort.by(sort));

        return acceptedJob.stream().map(acceptedMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public AcceptedJobDto getAcceptedJob(Long id){
        var job = acceptedRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job not accepted or doesn't exist")
        );

        return acceptedMapper.toDto(job);
    }

    @Transactional
    public AcceptedJobDto updateAcceptedApplication(Long id, AcceptJobRequest request){
        var acceptedApplication = acceptedRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Application not found")
        );

        acceptedMapper.update(request, acceptedApplication);

        return acceptedMapper.toDto(acceptedApplication);
    }

    @Transactional
    public AcceptedJobDto closeContract(Long id){
        var acceptedApplication = acceptedRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contract not found")
        );

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
