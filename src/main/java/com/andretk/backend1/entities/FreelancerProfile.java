package com.andretk.backend1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "freelancer_profile")
@Getter
@Setter
@Builder
@ToString(exclude = "freelancer")
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfile {
    @Id
    private Long id;

    @Column(name = "skills", nullable = false)
    private String skills;

    @Column(name = "languages", nullable = false)
    private String languages;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn( name = "id")
    @MapsId
    private User freelancer;
}
