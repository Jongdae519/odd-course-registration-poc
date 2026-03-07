package com.example.oddcourseregistrationpoc.domain.model;

public sealed interface EnrollmentResult
        permits EnrollmentResult.Success, EnrollmentResult.MissingPrerequisite, EnrollmentResult.CourseCanceled, EnrollmentResult.CapacityExceeded {
    record Success(StudentId studentId, CourseId courseId) implements EnrollmentResult {
    }

    record MissingPrerequisite(CourseId requiredCourseId) implements EnrollmentResult {
    }

    record CourseCanceled(CourseId courseId) implements EnrollmentResult {
    }

    record CapacityExceeded(int maxCapacity) implements EnrollmentResult {
    }
}
