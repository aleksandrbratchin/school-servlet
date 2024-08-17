package ru.bratchin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDto {
    private UUID id;
    private String name;
    private String description;
    private List<StudentDto> students;
}
