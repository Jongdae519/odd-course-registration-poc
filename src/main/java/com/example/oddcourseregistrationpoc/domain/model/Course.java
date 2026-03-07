package com.example.oddcourseregistrationpoc.domain.model;

import com.example.oddcourseregistrationpoc.domain.enums.CourseStatus;
import com.example.oddcourseregistrationpoc.domain.enums.Timeslot;

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
}
