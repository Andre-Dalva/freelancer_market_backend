package com.andretk.backend1.repositories;

import com.andretk.backend1.entities.Application;
import com.andretk.backend1.enums.ApplicationStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    boolean existsByFreelancerIdAndJobId(Long freelancerId, Long jobId);

    @EntityGraph(attributePaths = {"job","freelancer","job.recruiter","acceptedJob"})
    List<Application> findAll(Sort sort);

    @EntityGraph(attributePaths = {"job","freelancer","job.recruiter","acceptedJob"})
    Optional<Application> findById(Long id);

    @EntityGraph(attributePaths = {"job","freelancer","job.recruiter", "acceptedJob"})
    List<Application> findAllByFreelancerId(Long freelancerId, Sort sort);

    @EntityGraph(attributePaths = {"job","freelancer","job.recruiter", "acceptedJob"})
    List<Application> findAllByJobId(Long jobId, Sort sort);

    @EntityGraph(attributePaths = {"job","freelancer","job.recruiter", "acceptedJob"})
    List<Application> findAllByJobIdAndStatus(Long id, ApplicationStatus status);
}
