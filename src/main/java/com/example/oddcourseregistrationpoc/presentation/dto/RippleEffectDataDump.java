package com.example.oddcourseregistrationpoc.presentation.dto;

import com.example.oddcourseregistrationpoc.domain.model.Course;
import com.example.oddcourseregistrationpoc.domain.model.Student;

import java.util.List;

public record RippleEffectDataDump(List<Student> students, List<Course> courses) {
}
