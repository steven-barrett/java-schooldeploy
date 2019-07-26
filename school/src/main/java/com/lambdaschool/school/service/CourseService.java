package com.lambdaschool.school.service;

import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.view.CountStudentsInCourses;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

public interface CourseService
{
    ArrayList<Course> findAll(Pageable pageable);

    Course findCourseById(long courseid);

    ArrayList<CountStudentsInCourses> getCountStudentsInCourse();

    Course save(Course course);

    void delete(long id);
}
