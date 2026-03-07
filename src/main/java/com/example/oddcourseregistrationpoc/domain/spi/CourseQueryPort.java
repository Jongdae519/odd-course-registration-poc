package com.example.oddcourseregistrationpoc.domain.spi;

import com.example.oddcourseregistrationpoc.domain.model.Course;
import com.example.oddcourseregistrationpoc.domain.model.CourseId;
import com.example.oddcourseregistrationpoc.domain.model.Student;
import com.example.oddcourseregistrationpoc.domain.model.StudentId;

import java.util.List;

public interface CourseQueryPort {
    Student findStudentById(StudentId studentId);
    Course findCourseById(CourseId courseId);
    int countEnrollments(CourseId courseId);

    List<Student> findAllStudents();
    List<Course> findAllCourses();
}
