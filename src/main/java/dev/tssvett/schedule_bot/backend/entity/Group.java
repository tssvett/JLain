package dev.tssvett.schedule_bot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "\"group\"")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    private Long groupId;
    private String name;
    private Long course;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
}
