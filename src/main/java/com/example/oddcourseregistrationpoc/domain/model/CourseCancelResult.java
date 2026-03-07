package com.example.oddcourseregistrationpoc.domain.model;

public sealed interface CourseCancelResult
        permits CourseCancelResult.Success, CourseCancelResult.AlreadyCanceled {
    record Success(CourseId courseId) implements CourseCancelResult {
    }

    record AlreadyCanceled(CourseId courseId) implements CourseCancelResult {
    }
}
