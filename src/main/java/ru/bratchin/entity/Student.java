package ru.bratchin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private UUID id;
    private String firstName;
    private String lastName;
    private int course;
    private LocalDate admissionDate;
    private LocalDate dateOfGraduation;
    private UUID facultyId;
}
