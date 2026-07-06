package com.example.ss04.controller;

import com.example.ss04.dto.InstructorCreateRequest;
import com.example.ss04.model.Instructor;
import com.example.ss04.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Long id) {
        return ResponseEntity.ok(instructorService.findInstructorById(id));
    }

    @GetMapping
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.findAllInstructors());
    }

    @PostMapping
    public ResponseEntity<Instructor> createInstructor(@RequestBody InstructorCreateRequest req) {
        return ResponseEntity.ok(instructorService.createInstructor(req));
    }
}
