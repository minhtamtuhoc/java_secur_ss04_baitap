package com.example.ss04.dto;

import com.example.ss04.model.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateRequest {
    private String title;
    private CourseStatus status;
    private Long instructorId;
}
