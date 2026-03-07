package com.example.oddcourseregistrationpoc.presentation;

import com.example.oddcourseregistrationpoc.application.CourseApplicationService;
import com.example.oddcourseregistrationpoc.presentation.dto.RippleEffectDataDump;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CourseController {

    private final CourseApplicationService appService;

    public CourseController(CourseApplicationService appService) {
        this.appService = appService;
    }

    // 1. 수강신청 시도
    public record EnrollmentRequest(String studentId, String courseId) {
    }

    @PostMapping("/enrollments")
    public ResponseEntity<String> enroll(@RequestBody EnrollmentRequest request) {
        String message = appService.processEnrollment(request.studentId(), request.courseId());
        return ResponseEntity.ok(message);
    }

    // 2. 나비효과 트리거 (과목 폐강)
    @PatchMapping("/courses/{courseId}/cancel")
    public ResponseEntity<String> cancelCourse(@PathVariable String courseId) {
        String message = appService.cancelCourse(courseId);
        return ResponseEntity.ok(message);
    }

    // 3. AI 파급효과 분석용 원시 데이터 추출
    @GetMapping("/analysis/ripple-effect-data")
    public ResponseEntity<RippleEffectDataDump> getRippleEffectData() {
        return ResponseEntity.ok(appService.getDataForAiAnalysis());
    }
}
