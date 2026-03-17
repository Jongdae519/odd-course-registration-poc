package com.example.oddcourseregistrationpoc.domain.model;

import org.springframework.util.Assert;

import java.util.Set;

public record Student(
        StudentId id,
        String name,
        Department belongsTo,
        Set<CourseId> completed
) {

    public Student {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(name, "name must not be null");
        Assert.notNull(belongsTo, "belongsTo must not be null");
        Assert.notNull(completed, "completed must not be null");
        completed = Set.copyOf(completed);
    }
}
