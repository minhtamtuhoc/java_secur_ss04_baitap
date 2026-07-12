package com.example.ss04.service;

import com.example.ss04.dto.CourseCreateRequest;
import com.example.ss04.dto.CourseInstructorResponse;
import com.example.ss04.dto.CourseResponse;
import com.example.ss04.dto.CourseResponseV2;
import com.example.ss04.dto.CourseUpdateRequest;
import com.example.ss04.dto.PageResponse;
import com.example.ss04.model.Course;
import com.example.ss04.model.CourseStatus;
import com.example.ss04.model.Instructor;
import com.example.ss04.repository.CourseRepository;
import com.example.ss04.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    public Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    public List<CourseResponse> findAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToCourseResponse)
                .collect(Collectors.toList());
    }

    public CourseResponse getCourseResponseById(Long id) {
        Course course = findCourseById(id);
        return convertToCourseResponse(course);
    }

    public PageResponse<CourseResponse> getPagedCourses(int page, int size, String sortBy, Sort.Direction direction) {
        if (page < 0) {
            page = 0;
        }

        String sortField = (sortBy == null || sortBy.trim().isEmpty()) ? "id" : sortBy;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Course> coursePage = courseRepository.findAll(pageable);
        Page<CourseResponse> mappedPage = coursePage.map(this::convertToCourseResponse);

        return PageResponse.<CourseResponse>builder()
                .items(mappedPage.getContent())
                .page(mappedPage.getNumber())
                .size(mappedPage.getSize())
                .totalItems(mappedPage.getTotalElements())
                .totalPages(mappedPage.getTotalPages())
                .isLast(mappedPage.isLast())
                .build();
    }

    public PageResponse<CourseResponseV2> getPagedCoursesByStatus(int page, int size, String sortBy, Sort.Direction direction, CourseStatus status) {
        if (page < 0) {
            page = 0;
        }

        String sortField = (sortBy == null || sortBy.trim().isEmpty()) ? "id" : sortBy;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<CourseResponseV2> coursePage = courseRepository.findAllProjectedByStatus(status, pageable);

        return PageResponse.<CourseResponseV2>builder()
                .items(coursePage.getContent())
                .page(coursePage.getNumber())
                .size(coursePage.getSize())
                .totalItems(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .isLast(coursePage.isLast())
                .build();
    }

    public Course createCourse(CourseCreateRequest req) {
        Instructor instructor = instructorRepository.findById(req.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + req.getInstructorId()));

        Course course = new Course();
        course.setTitle(req.getTitle());
        course.setStatus(req.getStatus());
        course.setInstructor(instructor);
        course.setEnrollments(new ArrayList<>());

        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseUpdateRequest req) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        Instructor instructor = instructorRepository.findById(req.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + req.getInstructorId()));

        course.setTitle(req.getTitle());
        course.setStatus(req.getStatus());
        course.setInstructor(instructor);

        return courseRepository.save(course);
    }

    private CourseResponse convertToCourseResponse(Course course) {
        CourseInstructorResponse instructorResponse = null;
        if (course.getInstructor() != null) {
            instructorResponse = CourseInstructorResponse.builder()
                    .id(course.getInstructor().getId())
                    .name(course.getInstructor().getName())
                    .build();
        }

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .status(course.getStatus())
                .instructor(instructorResponse)
                .build();
    }
}
