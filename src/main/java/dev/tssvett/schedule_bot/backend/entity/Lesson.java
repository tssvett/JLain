package dev.tssvett.schedule_bot.backend.entity;

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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "place")
    private String place;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "subgroup")
    private String subgroup;

    @Column(name = "time")
    private String time;

    @Column(name = "date_day")
    private String dateDay;

    @Column(name = "date_number")
    private String dateNumber;

    public boolean isExist() {
        return name != null && !name.isEmpty();
    }
}
