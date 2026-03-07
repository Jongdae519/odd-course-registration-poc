package com.example.oddcourseregistrationpoc.domain.spi;

import com.example.oddcourseregistrationpoc.domain.enums.CourseStatus;
import com.example.oddcourseregistrationpoc.domain.model.CourseId;
import com.example.oddcourseregistrationpoc.domain.model.StudentId;

public interface CourseCommandPort {
    void saveEnrollment(StudentId studentId, CourseId courseId);
    void updateCourseStatus(CourseId courseId, CourseStatus status);
}
