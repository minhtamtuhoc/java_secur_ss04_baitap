package com.example.ss04.repository;

import com.example.ss04.model.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {

    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

    Optional<StudentEnrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);

    @Query("SELECT se FROM StudentEnrollment se WHERE se.course.id = :courseId AND LOWER(se.student.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<StudentEnrollment> searchStudentsInCourse(@Param("courseId") Long courseId, @Param("search") String search);
}
