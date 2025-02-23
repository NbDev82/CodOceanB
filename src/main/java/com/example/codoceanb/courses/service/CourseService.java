package com.example.codoceanb.courses.service;

import com.example.codoceanb.courses.dto.CourseDTO;
import com.example.codoceanb.courses.dto.LessonDTO;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    CourseDTO getCourseById(UUID id);

    CourseDTO createCourse(CourseDTO request);

    CourseDTO updateCourse(CourseDTO request);

    void deleteCourse(UUID id);

    void publicCourse(UUID id);

    CourseDTO getPublicCourseById(UUID id);
}
