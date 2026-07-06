package com.example.ss04.service;

import com.example.ss04.dto.InstructorCreateRequest;
import com.example.ss04.model.Instructor;
import com.example.ss04.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public Instructor findInstructorById(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
    }

    public List<Instructor> findAllInstructors() {
        return instructorRepository.findAll();
    }

    public Instructor createInstructor(InstructorCreateRequest req) {
        Instructor instructor = new Instructor();
        instructor.setName(req.getName());
        instructor.setEmail(req.getEmail());
        instructor.setCourses(new ArrayList<>());
        return instructorRepository.save(instructor);
    }
}
