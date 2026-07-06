package com.example.ss04.controller;

import com.example.ss04.dto.ApiResponse;
import com.example.ss04.dto.InstructorCreateRequest;
import com.example.ss04.model.Instructor;
import com.example.ss04.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Instructor>> getInstructorById(@PathVariable Long id) {
        Instructor instructor = instructorService.findInstructorById(id);
        ApiResponse<Instructor> response = ApiResponse.<Instructor>builder()
                .status(HttpStatus.OK.value())
                .message("Get instructor successfully")
                .data(instructor)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Instructor>>> getAllInstructors() {
        List<Instructor> instructors = instructorService.findAllInstructors();
        ApiResponse<List<Instructor>> response = ApiResponse.<List<Instructor>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all instructors successfully")
                .data(instructors)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createInstructor(@RequestBody InstructorCreateRequest req) {
        instructorService.createInstructor(req);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message("Create instructor successfully")
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
