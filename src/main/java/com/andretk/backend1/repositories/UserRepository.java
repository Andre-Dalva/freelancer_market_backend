package com.andretk.backend1.repositories;

import com.andretk.backend1.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @EntityGraph(attributePaths = {"myJobs"})
    List<User> findAll(Sort sort);


    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
