package com.example.codoceanb.courses.service;

import com.example.codoceanb.courses.constants.CourseConstants;
import com.example.codoceanb.courses.dto.CourseDTO;
import com.example.codoceanb.courses.exception.CourseNotFoundException;
import com.example.codoceanb.courses.exception.CourseNotPublicException;
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

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseDTO getCourseById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(CourseConstants.COURSE_NOT_FOUND, HttpStatus.NOT_FOUND));
        return course.toDTO();
    }

    @Override
    public CourseDTO getPublicCourseById(UUID id) {
        CourseDTO courseDTO = getCourseById(id);
        if(!courseDTO.isPublic())
            throw new CourseNotPublicException(CourseConstants.COURSE_NOT_PUBLIC_YET, HttpStatus.BAD_REQUEST);
        return null;
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(Course::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getAllPublicCourses() {
        List<Course> publicCourses = courseRepository.findByPublic(true);
        return publicCourses.stream().map(Course::toDTO).collect(Collectors.toList());
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseDTO.toEntity();
        Course savedCourse = courseRepository.save(course);
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
            throw new CourseNotFoundException(CourseConstants.COURSE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        courseRepository.deleteById(id);
    }

    @Override
    public void publicCourse(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(CourseConstants.COURSE_NOT_FOUND, HttpStatus.NOT_FOUND));
        course.setPublic(true);
        courseRepository.save(course);
    }
}
