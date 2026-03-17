package com.example.oddcourseregistrationpoc.domain.model;

import org.springframework.util.Assert;

public record CourseId(
        String value
) {

    public CourseId {
        Assert.notNull(value, "courseId must not be null");
    }
}
