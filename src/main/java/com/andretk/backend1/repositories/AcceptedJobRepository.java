package com.andretk.backend1.repositories;

import com.andretk.backend1.entities.AcceptedJobs;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcceptedJobRepository extends JpaRepository<AcceptedJobs,Long> {
    @EntityGraph(attributePaths = {"application"})
    List<AcceptedJobs> findAll(Sort sort);


}
