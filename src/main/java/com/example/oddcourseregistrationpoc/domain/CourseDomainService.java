package com.example.oddcourseregistrationpoc.domain;

import com.example.oddcourseregistrationpoc.domain.api.CourseUseCase;
import com.example.oddcourseregistrationpoc.domain.enums.CourseStatus;
import com.example.oddcourseregistrationpoc.domain.model.Course;
import com.example.oddcourseregistrationpoc.domain.model.CourseCancelResult;
import com.example.oddcourseregistrationpoc.domain.model.EnrollmentResult;
import com.example.oddcourseregistrationpoc.domain.model.Student;
import com.example.oddcourseregistrationpoc.domain.spi.CourseCommandPort;
import org.springframework.stereotype.Service;

@Service
public class CourseDomainService implements CourseUseCase {

    private final CourseCommandPort commandPort;

    public CourseDomainService(CourseCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    @Override
    public EnrollmentResult enroll(Student student, Course course, int currentEnrolledCount) {
        // 1. 순수 도메인 룰 평가 (Ontology 기반)
        EnrollmentResult evaluation = evaluateRules(student, course, currentEnrolledCount);

        // 2. Switch Pattern Matching을 활용한 Command(저장) 분기 처리
        return switch (evaluation) {
            case EnrollmentResult.Success s -> {
                // UML 규칙: Domain Service가 Command Port를 호출한다
                commandPort.saveEnrollment(s.studentId(), s.courseId());
                yield s;
            }
            case EnrollmentResult.MissingPrerequisite m -> m;
            case EnrollmentResult.CourseCanceled c -> c;
            case EnrollmentResult.CapacityExceeded e -> e;
        };
    }

    @Override
    public CourseCancelResult cancelCourse(Course course) {
        if (course.status() == CourseStatus.CANCELED) {
            return new CourseCancelResult.AlreadyCanceled(course.id());
        }

        // 상태 변경 Command 수행
        commandPort.updateCourseStatus(course.id(), CourseStatus.CANCELED);
        return new CourseCancelResult.Success(course.id());
    }

    // 내부 도메인 룰 검증 로직
    private EnrollmentResult evaluateRules(Student student, Course course, int enrolledCount) {
        if (course.status() == CourseStatus.CANCELED) {
            return new EnrollmentResult.CourseCanceled(course.id());
        }
        if (enrolledCount >= course.allowsCapacity()) {
            return new EnrollmentResult.CapacityExceeded(course.allowsCapacity());
        }
        if (course.requires().isPresent() && !student.completed().contains(course.requires().get())) {
            return new EnrollmentResult.MissingPrerequisite(course.requires().get());
        }
        return new EnrollmentResult.Success(student.id(), course.id());
    }
}
