package com.example.oddcourseregistrationpoc.domain.api;

import com.example.oddcourseregistrationpoc.domain.model.Course;
import com.example.oddcourseregistrationpoc.domain.model.CourseCancelResult;
import com.example.oddcourseregistrationpoc.domain.model.EnrollmentResult;
import com.example.oddcourseregistrationpoc.domain.model.Student;

public interface CourseUseCase {
    EnrollmentResult enroll(Student student, Course course, int currentEnrolledCount);
    CourseCancelResult cancelCourse(Course course);
}
