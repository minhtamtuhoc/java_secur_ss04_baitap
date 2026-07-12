package com.example.ss04.controller;

import com.example.ss04.dto.ApiResponse;
import com.example.ss04.dto.CourseCreateRequest;
import com.example.ss04.dto.CourseEnrollmentRequest;
import com.example.ss04.dto.CourseEnrollmentResponse;
import com.example.ss04.dto.CourseResponse;
import com.example.ss04.dto.CourseUpdateRequest;
import com.example.ss04.dto.PageResponse;
import com.example.ss04.dto.StudentResponse;
import com.example.ss04.model.CourseStatus;
import com.example.ss04.service.CourseService;
import com.example.ss04.service.StudentEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final StudentEnrollmentService studentEnrollmentService;

    @Autowired
    public CourseController(CourseService courseService, StudentEnrollmentService studentEnrollmentService) {
        this.courseService = courseService;
        this.studentEnrollmentService = studentEnrollmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseResponse>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "ACTIVE") CourseStatus status) {

        PageResponse<CourseResponse> pagedCourses = courseService.getPagedCoursesByStatus(page, size, sortBy, direction, status);
        ApiResponse<PageResponse<CourseResponse>> response = ApiResponse.<PageResponse<CourseResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get paged courses successfully")
                .data(pagedCourses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Long id) {
        CourseResponse course = courseService.getCourseResponseById(id);
        ApiResponse<CourseResponse> response = ApiResponse.<CourseResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get course successfully")
                .data(course)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCourse(@RequestBody CourseCreateRequest req) {
        courseService.createCourse(req);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message("Create course successfully")
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateRequest req) {
        courseService.updateCourse(id, req);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Update course successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{courseId}/enrollments")
    public ResponseEntity<ApiResponse<CourseEnrollmentResponse>> enrollStudentInCourse(
            @PathVariable Long courseId,
            @RequestBody CourseEnrollmentRequest req) {
        CourseEnrollmentResponse data = studentEnrollmentService.enrollStudentInCourse(courseId, req);
        ApiResponse<CourseEnrollmentResponse> response = ApiResponse.<CourseEnrollmentResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Enroll student in course successfully")
                .data(data)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{courseId}/enrollments/students/{studentId}")
    public ResponseEntity<ApiResponse<Void>> dropoutStudentFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        studentEnrollmentService.dropoutStudentFromCourse(courseId, studentId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Dropout student from course successfully")
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{courseId}/enrollments/students")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> searchStudentsInCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false, defaultValue = "") String search) {
        List<StudentResponse> data = studentEnrollmentService.searchStudentsInCourse(courseId, search);
        ApiResponse<List<StudentResponse>> response = ApiResponse.<List<StudentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Search students in course successfully")
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }
}
