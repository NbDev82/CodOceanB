package com.example.codoceanb.courses.service;

import com.example.codoceanb.courses.dto.LessonDTO;

import java.util.UUID;

public interface LessonService {
    LessonDTO getLessonById(UUID id);

    LessonDTO createLesson(LessonDTO lessonDTO);

    LessonDTO updateLesson(LessonDTO lessonDTO);

    void deleteLesson(UUID id);
}
