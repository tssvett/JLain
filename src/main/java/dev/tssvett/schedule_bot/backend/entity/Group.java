package dev.tssvett.schedule_bot.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "\"group\"")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "course", nullable = false)
    private Long course;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BotUser> botUser;
}
