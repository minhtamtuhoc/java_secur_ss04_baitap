package com.example.ss04.service;

import com.example.ss04.dto.CourseEnrollmentRequest;
import com.example.ss04.dto.CourseEnrollmentResponse;
import com.example.ss04.dto.StudentResponse;
import com.example.ss04.model.Course;
import com.example.ss04.model.CourseStatus;
import com.example.ss04.model.Student;
import com.example.ss04.model.StudentEnrollment;
import com.example.ss04.repository.CourseRepository;
import com.example.ss04.repository.StudentEnrollmentRepository;
import com.example.ss04.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        enrollment.setEnrollAt(LocalDateTime.now());

        return studentEnrollmentRepository.save(enrollment);
    }

    public CourseEnrollmentResponse enrollStudentInCourse(Long courseId, CourseEnrollmentRequest req) {
        boolean courseIsActive = courseRepository.existsByIdAndStatus(courseId, CourseStatus.ACTIVE);
        if (!courseIsActive) {
            throw new RuntimeException("Course not found or is not ACTIVE");
        }

        Student student = studentRepository.findById(req.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + req.getStudentId()));

        boolean alreadyEnrolled = studentEnrollmentRepository.existsByCourseIdAndStudentId(courseId, req.getStudentId());
        if (alreadyEnrolled) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        Course course = courseRepository.findById(courseId).get();

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollAt(LocalDateTime.now());

        StudentEnrollment saved = studentEnrollmentRepository.save(enrollment);

        return CourseEnrollmentResponse.builder()
                .studentId(saved.getStudent().getId())
                .courseId(saved.getCourse().getId())
                .enrolledAt(saved.getEnrollAt())
                .build();
    }

    public void dropoutStudentFromCourse(Long courseId, Long studentId) {
        StudentEnrollment enrollment = studentEnrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found for student " + studentId + " in course " + courseId));

        studentEnrollmentRepository.delete(enrollment);
    }

    public List<StudentResponse> searchStudentsInCourse(Long courseId, String search) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found with id: " + courseId);
        }

        List<StudentEnrollment> enrollments = studentEnrollmentRepository.searchStudentsInCourse(courseId, search);

        return enrollments.stream()
                .map(se -> StudentResponse.builder()
                        .id(se.getStudent().getId())
                        .name(se.getStudent().getName())
                        .email(se.getStudent().getEmail())
                        .build())
                .collect(Collectors.toList());
    }
}
