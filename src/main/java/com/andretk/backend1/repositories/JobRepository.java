package com.andretk.backend1.repositories;

import com.andretk.backend1.entities.Job;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {

    @EntityGraph(attributePaths = {"recruiter"})
    List<Job> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"recruiter"})
    Optional<Job> findById(Long id);

    @EntityGraph(attributePaths = {"recruiter"})
    List<Job> findAllByRecruiterId( Long id);
}
