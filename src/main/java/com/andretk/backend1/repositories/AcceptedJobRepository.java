package com.andretk.backend1.repositories;

import com.andretk.backend1.entities.AcceptedJobs;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcceptedJobRepository extends JpaRepository<AcceptedJobs,Long> {
    @EntityGraph(attributePaths = {"application","application.freelancer",
            "application.job","application.job.recruiter"}
    )
    List<AcceptedJobs> findAll(@NonNull Sort sort);

    @EntityGraph(attributePaths = {"application","application.freelancer",
            "application.job","application.job.recruiter"}
    )
    Optional<AcceptedJobs> findById(@NonNull Long id);

}
