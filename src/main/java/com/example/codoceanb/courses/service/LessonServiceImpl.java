package com.example.codoceanb.courses.service;

import com.example.codoceanb.courses.constants.CourseConstants;
import com.example.codoceanb.courses.constants.LessonConstants;
import com.example.codoceanb.courses.dto.LessonDTO;
import com.example.codoceanb.courses.entity.Course;
import com.example.codoceanb.courses.entity.Lesson;
import com.example.codoceanb.courses.exception.CourseNotFoundException;
import com.example.codoceanb.courses.exception.CourseNotPublicException;
import com.example.codoceanb.courses.exception.LessonNotFoundException;
import com.example.codoceanb.courses.repository.CourseRepository;
import com.example.codoceanb.courses.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService{
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public LessonDTO getLessonById(UUID id) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        return lessonOptional.map(Lesson::toDTO).orElse(null);
    }

    @Override
    public LessonDTO getPublicLessonById(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException(LessonConstants.LESSON_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (!lesson.getCourse().isPublic()) {
            throw new CourseNotPublicException(CourseConstants.COURSE_NOT_PUBLIC_YET, HttpStatus.BAD_REQUEST);
        }
        return lesson.toDTO();
    }

    @Override
    public LessonDTO createLesson(LessonDTO lessonDTO) {
        Lesson lesson;
        if (lessonDTO.getLessonType().equals(LessonDTO.EType.THEORY)) {
            lesson = lessonDTO.toTheoryEntity();
        } else {
            lesson = lessonDTO.toPracticeEntity();
        }
        
        Course course = courseRepository.findById(lessonDTO.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException(CourseConstants.COURSE_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (!course.isPublic()) {
            throw new CourseNotPublicException(CourseConstants.COURSE_NOT_PUBLIC_YET, HttpStatus.BAD_REQUEST);
        }
        lesson.setCourse(course);

        Lesson savedLesson = lessonRepository.save(lesson);
        return savedLesson.toDTO();
    }

    @Override
    public LessonDTO updateLesson(LessonDTO lessonDTO) {
        return createLesson(lessonDTO);
    }

    @Override
    public void deleteLesson(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException(LessonConstants.LESSON_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (!lesson.getCourse().isPublic()) {
            throw new CourseNotPublicException(CourseConstants.COURSE_NOT_PUBLIC_YET, HttpStatus.BAD_REQUEST);
        }
        lessonRepository.deleteById(id);
    }
}
