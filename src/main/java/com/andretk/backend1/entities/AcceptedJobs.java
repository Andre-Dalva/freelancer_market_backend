package com.andretk.backend1.entities;

import com.andretk.backend1.enums.AcceptedApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "accepted_jobs")
@Getter
@Setter
@Builder
@ToString(exclude = "application")
@NoArgsConstructor
@AllArgsConstructor
public class AcceptedJobs {

    @Id
    private Long id;

    @Column(name = "status", nullable = false, length = 80)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AcceptedApplicationStatus status = AcceptedApplicationStatus.ACTIVE;

    @Column(name = "optional_link1")
    private String optionalLink1;

    @Column(name = "optional_link2")
    private String optionalLink2;

    @Column(name = "comments")
    private String comments;

    @Column(name = "assigned_at", nullable = false)
    @Builder.Default
    private Instant assignedAt = Instant.now();

    @Column(name = "finished_at")
    private Instant finishedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "application_id")
    @MapsId
    private Application application;
}
