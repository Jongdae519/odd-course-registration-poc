package com.example.oddcourseregistrationpoc.domain.model;

import com.example.oddcourseregistrationpoc.domain.enums.CourseStatus;
import com.example.oddcourseregistrationpoc.domain.enums.Timeslot;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.Set;

public record Course(
        CourseId id,
        String name,
        Set<Timeslot> timeslots,
        CourseStatus status,
        int allowsCapacity,
        Optional<CourseId> requires
) {

    public Course {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(name, "name must not be null");
        Assert.notNull(timeslots, "timeslots must not be null");
        timeslots = Set.copyOf(timeslots);
        Assert.notNull(status, "status must not be null");
        Assert.isTrue(allowsCapacity > 0, "allowsCapacity must be greater than 0");
    }
}
