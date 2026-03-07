package com.example.oddcourseregistrationpoc.domain.model;

import java.util.Set;

public record Student(
        StudentId id,
        String name,
        Department belongsTo,
        Set<CourseId> completed
) {
}
