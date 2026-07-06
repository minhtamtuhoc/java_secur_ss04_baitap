package com.example.ss04.service;

import com.example.ss04.model.Course;
import com.example.ss04.model.Student;
import com.example.ss04.model.StudentEnrollment;
import com.example.ss04.repository.CourseRepository;
import com.example.ss04.repository.StudentEnrollmentRepository;
import com.example.ss04.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentEnrollmentService {

    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public StudentEnrollmentService(StudentEnrollmentRepository studentEnrollmentRepository,
                                    StudentRepository studentRepository,
                                    CourseRepository courseRepository) {
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public StudentEnrollment enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return studentEnrollmentRepository.save(enrollment);
    }
}
