package dev.tssvett.schedule_bot.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Long facultyId;
    private String name;
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Group> groups;
}
