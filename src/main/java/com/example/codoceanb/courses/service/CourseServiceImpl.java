package com.example.codoceanb.courses.service;

import com.example.codoceanb.courses.dto.CourseDTO;
import com.example.codoceanb.courses.dto.LessonDTO;
import com.example.codoceanb.courses.entity.Lesson;
import com.example.codoceanb.courses.exception.CourseNotFoundException;
import com.example.codoceanb.courses.repository.LessonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.codoceanb.courses.entity.Course;
import com.example.codoceanb.courses.repository.CourseRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public CourseDTO getCourseById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id, HttpStatus.NOT_FOUND));
        return course.toDTO();
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseDTO.toEntity();
        Course savedCourse = courseRepository.save(course);
        
        if (courseDTO.getLessons() != null && !courseDTO.getLessons().isEmpty()) {
            List<Lesson> lessons = courseDTO.getLessons().stream()
                .map(lessonDTO -> {
                    Lesson lesson;
                    if(lessonDTO.getLessonType().equals(LessonDTO.EType.THEORY)) {
                        lesson = lessonDTO.toTheoryEntity();
                    } else {
                        lesson = lessonDTO.toPracticeEntity();
                    }
                    lesson.setCourse(savedCourse);
                    return lesson;
                })
                .collect(Collectors.toList());
            lessonRepository.saveAll(lessons);
            savedCourse.setLessons(lessons);
        }
        
        return savedCourse.toDTO();
    }

    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        courseRepository.save(courseDTO.toEntity());
        return courseDTO;
    }

    @Override
    public void deleteCourse(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        courseRepository.deleteById(id);
    }
}
