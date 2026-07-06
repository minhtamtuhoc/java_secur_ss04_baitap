package com.example.ss04.controller;

import com.example.ss04.dto.ApiResponse;
import com.example.ss04.dto.StudentEnrollmentRequest;
import com.example.ss04.service.StudentEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students-enrollments")
public class StudentEnrollmentController {

    private final StudentEnrollmentService studentEnrollmentService;

    @Autowired
    public StudentEnrollmentController(StudentEnrollmentService studentEnrollmentService) {
        this.studentEnrollmentService = studentEnrollmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> enrollStudent(@RequestBody StudentEnrollmentRequest req) {
        studentEnrollmentService.enrollStudent(req.getStudentId(), req.getCourseId());
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message("Enroll student to course successfully")
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
