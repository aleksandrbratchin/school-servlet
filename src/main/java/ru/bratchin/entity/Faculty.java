package ru.bratchin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {

    private UUID id;

    private String name;

    private String description;

    private List<Student> students = new ArrayList<>();

}
