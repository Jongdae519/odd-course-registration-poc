package com.example.oddcourseregistrationpoc.domain.model;

import org.springframework.util.Assert;

public record Department(
        String id,
        String name
) {

    public Department {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(name, "name must not be null");
    }
}
