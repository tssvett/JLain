package dev.tssvett.schedule_bot.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "lesson")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "teacher", nullable = false)
    private String teacher;

    @Column(name = "subgroup", nullable = false)
    private String subgroup;

    @Column(name = "time", nullable = false)
    private String time;

    @Column(name = "date_day", nullable = false)
    private String dateDay;

    @Column(name = "date_number", nullable = false)
    private String dateNumber;

    public boolean isExist() {
        return name != null && !name.isEmpty();
    }
}
