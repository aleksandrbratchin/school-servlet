package ru.bratchin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private int course;
    private String admissionDate;
    private String dateOfGraduation;
    private UUID facultyId;
}