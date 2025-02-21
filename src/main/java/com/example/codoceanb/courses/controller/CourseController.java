package com.example.codoceanb.courses.controller;

import com.example.codoceanb.courses.dto.CourseDTO;
import com.example.codoceanb.courses.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CourseDTO> getCourseDetail(@PathVariable UUID id) {
        CourseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable UUID id, @RequestBody CourseDTO courseDTO) {
        courseDTO.setId(id);
        CourseDTO updatedCourse = courseService.updateCourse(courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
