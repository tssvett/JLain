package dev.tssvett.schedule_bot.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "faculty")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Faculty {
    @Id
    @Column(name = "faculty_id", nullable = false)
    private Long facultyId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Group> groups;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Student> students;
}
