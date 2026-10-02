package com.andretk.backend1.entities;

import com.andretk.backend1.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@Builder
@ToString(exclude = {"recruiter", "applications"})
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "requirements", nullable = false)
    private String requirements;

    @Column(name = "deadlines")
    private String deadlines;

    @Column(name = "about", nullable = false)
    private String about;

    @Column(name = "benefits_and_payments")
    private String benefitsAndPayments;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JobStatus status = JobStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    public void addRecruiter(User recruiter){
        this.recruiter = recruiter;
    }

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Application> applications = new HashSet<>();

    public void addApplications(Application application){
        this.applications.add(application);
        application.setJob(this);
    }

    public void removeApplication(Application application){
        this.applications.remove(application);
        application.setJob(null);
    }
}
