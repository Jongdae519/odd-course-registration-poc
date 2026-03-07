package com.example.oddcourseregistrationpoc.application;

import com.example.oddcourseregistrationpoc.domain.api.CourseUseCase;
import com.example.oddcourseregistrationpoc.domain.model.Course;
import com.example.oddcourseregistrationpoc.domain.model.CourseCancelResult;
import com.example.oddcourseregistrationpoc.domain.model.CourseId;
import com.example.oddcourseregistrationpoc.domain.model.EnrollmentResult;
import com.example.oddcourseregistrationpoc.domain.model.Student;
import com.example.oddcourseregistrationpoc.domain.model.StudentId;
import com.example.oddcourseregistrationpoc.domain.spi.CourseQueryPort;
import com.example.oddcourseregistrationpoc.presentation.dto.RippleEffectDataDump;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseApplicationService {

    private final CourseUseCase courseUseCase;
    private final CourseQueryPort courseQueryPort;

    public CourseApplicationService(CourseUseCase courseUseCase, CourseQueryPort courseQueryPort) {
        this.courseUseCase = courseUseCase;
        this.courseQueryPort = courseQueryPort;
    }

    @Transactional
    public String processEnrollment(String studentIdStr, String courseIdStr) {
        StudentId studentId = new StudentId(studentIdStr);
        CourseId courseId = new CourseId(courseIdStr);

        // UML 규칙: App Service가 Query Port를 사용하여 데이터 조합
        Student student = courseQueryPort.findStudentById(studentId);
        Course course = courseQueryPort.findCourseById(courseId);
        int enrolledCount = courseQueryPort.countEnrollments(courseId);

        // 도메인 유스케이스 실행
        EnrollmentResult result = courseUseCase.enroll(student, course, enrolledCount);

        // 처리 결과 반환 매핑 (Web Layer로 나갈 응답)
        return switch (result) {
            case EnrollmentResult.Success s -> "Success: Enrolled in " + s.courseId().value();
            case EnrollmentResult.MissingPrerequisite m -> "Failed: Requires " + m.requiredCourseId().value();
            case EnrollmentResult.CourseCanceled c -> "Failed: Course " + c.courseId().value() + " is canceled.";
            case EnrollmentResult.CapacityExceeded e -> "Failed: Over capacity " + e.maxCapacity();
        };
    }

    @Transactional
    public String cancelCourse(String courseIdStr) {
        Course course = courseQueryPort.findCourseById(new CourseId(courseIdStr));
        CourseCancelResult result = courseUseCase.cancelCourse(course);

        return switch (result) {
            case CourseCancelResult.Success s -> "Success: Course " + s.courseId().value() + " canceled.";
            case CourseCancelResult.AlreadyCanceled a -> "Info: Already canceled.";
        };
    }

    // AI 파급효과 분석을 위한 Raw Data(Fact) DTO 레코드 조립
    public RippleEffectDataDump getDataForAiAnalysis() {
        List<Student> allStudents = courseQueryPort.findAllStudents();
        List<Course> allCourses = courseQueryPort.findAllCourses();
        return new RippleEffectDataDump(allStudents, allCourses);
    }

}
