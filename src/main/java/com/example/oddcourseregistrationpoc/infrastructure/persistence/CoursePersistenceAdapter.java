package com.example.oddcourseregistrationpoc.infrastructure.persistence;

import com.example.oddcourseregistrationpoc.domain.enums.CourseStatus;
import com.example.oddcourseregistrationpoc.domain.enums.Timeslot;
import com.example.oddcourseregistrationpoc.domain.model.Course;
import com.example.oddcourseregistrationpoc.domain.model.CourseId;
import com.example.oddcourseregistrationpoc.domain.model.Department;
import com.example.oddcourseregistrationpoc.domain.model.Student;
import com.example.oddcourseregistrationpoc.domain.model.StudentId;
import com.example.oddcourseregistrationpoc.domain.spi.CourseCommandPort;
import com.example.oddcourseregistrationpoc.domain.spi.CourseQueryPort;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class CoursePersistenceAdapter implements CourseQueryPort, CourseCommandPort {

    private final JdbcClient jdbcClient;

    public CoursePersistenceAdapter(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Student findStudentById(StudentId studentId) {
        String sql = """
                SELECT s.id, s.name, d.id AS dept_id, d.name AS dept_name
                FROM student s
                JOIN department d ON s.department_id = d.id
                WHERE s.id = :id
                """;

        return jdbcClient.sql(sql)
                .param("id", studentId.value())
                .query((rs, rowNum) -> new Student(
                        new StudentId(rs.getString("id")),
                        rs.getString("name"),
                        new Department(rs.getString("dept_id"), rs.getString("dept_name")),
                        findCompletedCourses(studentId)
                ))
                .single();
    }

    @Override
    public Course findCourseById(CourseId courseId) {
        String sql = """
            SELECT id, name, status, allows_capacity, requires_course_id
            FROM course
            WHERE id = :id
            """;

        return jdbcClient.sql(sql)
                .param("id", courseId.value())
                .query((rs, rowNum) -> {
                    String requiresId = rs.getString("requires_course_id");
                    return new Course(
                            new CourseId(rs.getString("id")),
                            rs.getString("name"),
                            findTimeslots(courseId), // 1:N 컬렉션 조립
                            CourseStatus.valueOf(rs.getString("status")),
                            rs.getInt("allows_capacity"),
                            Optional.ofNullable(requiresId).map(CourseId::new) // Nullable FK 처리
                    );
                })
                .single();
    }

    @Override
    public int countEnrollments(CourseId courseId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE course_id = :id";

        return jdbcClient.sql(sql)
                .param("id", courseId.value())
                .query(Integer.class)
                .single();
    }

    @Override
    public List<Student> findAllStudents() {
        String sql = """
            SELECT s.id, s.name, d.id AS dept_id, d.name AS dept_name
            FROM student s
            JOIN department d ON s.department_id = d.id
            """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    StudentId id = new StudentId(rs.getString("id"));
                    return new Student(
                            id,
                            rs.getString("name"),
                            new Department(rs.getString("dept_id"), rs.getString("dept_name")),
                            findCompletedCourses(id)
                    );
                })
                .list();
    }

    @Override
    public List<Course> findAllCourses() {
        String sql = """
            SELECT id, name, status, allows_capacity, requires_course_id
            FROM course
            """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    CourseId id = new CourseId(rs.getString("id"));
                    String requiresId = rs.getString("requires_course_id");
                    return new Course(
                            id,
                            rs.getString("name"),
                            findTimeslots(id),
                            CourseStatus.valueOf(rs.getString("status")),
                            rs.getInt("allows_capacity"),
                            Optional.ofNullable(requiresId).map(CourseId::new)
                    );
                })
                .list();
    }

    // ==========================================
    // Command Port Implementation (상태 변경 및 사이드 이펙트 발생)
    // ==========================================

    @Override
    public void saveEnrollment(StudentId studentId, CourseId courseId) {
        String sql = "INSERT INTO enrollment (student_id, course_id) VALUES (:studentId, :courseId)";

        jdbcClient.sql(sql)
                .param("studentId", studentId.value())
                .param("courseId", courseId.value())
                .update();
    }

    @Override
    public void updateCourseStatus(CourseId courseId, CourseStatus status) {
        String sql = "UPDATE course SET status = :status WHERE id = :courseId";

        jdbcClient.sql(sql)
                .param("status", status.name())
                .param("courseId", courseId.value())
                .update();
    }

    // ==========================================
    // Helper Methods (도메인 애그리거트 하위 컬렉션 매핑용)
    // ==========================================

    private Set<CourseId> findCompletedCourses(StudentId studentId) {
        String sql = "SELECT course_id FROM completed_course WHERE student_id = :id";

        List<CourseId> courses = jdbcClient.sql(sql)
                .param("id", studentId.value())
                .query((rs, rowNum) -> new CourseId(rs.getString("course_id")))
                .list();

        return Set.copyOf(courses); // 불변 Set 반환
    }

    private Set<Timeslot> findTimeslots(CourseId courseId) {
        String sql = "SELECT timeslot FROM course_timeslot WHERE course_id = :id";

        List<Timeslot> timeslots = jdbcClient.sql(sql)
                .param("id", courseId.value())
                .query((rs, rowNum) -> Timeslot.valueOf(rs.getString("timeslot")))
                .list();

        return Set.copyOf(timeslots); // 불변 Set 반환
    }
}
