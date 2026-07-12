package com.example.ss04.repository;

import com.example.ss04.model.Course;
import com.example.ss04.model.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByIdAndStatus(Long id, CourseStatus status);
}
