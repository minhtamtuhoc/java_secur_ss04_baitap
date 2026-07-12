package com.example.ss04.service;

import com.example.ss04.dto.InstructorCreateRequest;
import com.example.ss04.dto.InstructorResponse;
import com.example.ss04.model.Instructor;
import com.example.ss04.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<InstructorResponse> findAllInstructorsResponses() {
        return instructorRepository.findAll().stream()
                .map(this::convertToInstructorResponse)
                .collect(Collectors.toList());
    }

    public InstructorResponse getInstructorResponseById(Long id) {
        Instructor instructor = findInstructorById(id);
        return convertToInstructorResponse(instructor);
    }

    public Instructor createInstructor(InstructorCreateRequest req) {
        Instructor instructor = new Instructor();
        instructor.setName(req.getName());
        instructor.setEmail(req.getEmail());
        instructor.setCourses(new ArrayList<>());
        return instructorRepository.save(instructor);
    }

    private InstructorResponse convertToInstructorResponse(Instructor instructor) {
        return InstructorResponse.builder()
                .id(instructor.getId())
                .name(instructor.getName())
                .email(instructor.getEmail())
                .build();
    }
}
