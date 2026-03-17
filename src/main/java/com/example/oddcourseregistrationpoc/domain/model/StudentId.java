package com.example.oddcourseregistrationpoc.domain.model;

import org.springframework.util.Assert;

public record StudentId(
        String value
) {

    public StudentId {
        Assert.notNull(value, "studentId must not be null");
    }
}
