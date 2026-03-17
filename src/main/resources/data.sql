-- ==========================================
-- 1. 학과 (Department) 데이터 삽입
-- ==========================================
INSERT INTO department (id, name) VALUES ('DEPT_CS', 'Computer Science');
INSERT INTO department (id, name) VALUES ('DEPT_MATH', 'Mathematics');

-- ==========================================
-- 2. 학생 (Student) 데이터 삽입
-- ==========================================
INSERT INTO student (id, name, department_id) VALUES ('S1', 'Alice', 'DEPT_CS');
INSERT INTO student (id, name, department_id) VALUES ('S2', 'Bob', 'DEPT_CS');
INSERT INTO student (id, name, department_id) VALUES ('S3', 'Charlie', 'DEPT_MATH');
INSERT INTO student (id, name, department_id) VALUES ('S4', 'David', 'DEPT_CS');

-- ==========================================
-- 3. 과목 (Course) 데이터 삽입 (뎁스 4단계 체인 구성)
-- ==========================================
-- [Depth 1] 최하위 기초 과목 (선수과목 없음)
INSERT INTO course (id, name, status, allows_capacity, requires_course_id)
VALUES ('C-LANG', 'C Programming', 'ACTIVE', 30, NULL);

INSERT INTO course (id, name, status, allows_capacity, requires_course_id)
VALUES ('COMP-THINKING', '컴퓨팅 사고', 'ACTIVE', 50, NULL);

-- [Depth 2] 기초 과목 ('컴퓨팅 사고' 요구)
INSERT INTO course (id, name, status, allows_capacity, requires_course_id)
VALUES ('BASIC-JAVA', '기초 Java', 'ACTIVE', 30, 'COMP-THINKING');

-- [Depth 3] 고급 과목 ('기초 Java' 요구)
INSERT INTO course (id, name, status, allows_capacity, requires_course_id)
VALUES ('ADV-JAVA', '고급 Java', 'ACTIVE', 2, 'BASIC-JAVA');

-- [Depth 4] 프레임워크 과목 ('고급 Java' 요구)
INSERT INTO course (id, name, status, allows_capacity, requires_course_id)
VALUES ('SPRING-BOOT', '스프링 부트', 'ACTIVE', 30, 'ADV-JAVA');

-- [Depth 5] 최상위 졸업 프로젝트 ('스프링 부트' 요구)
INSERT INTO course (id, name, status, allows_capacity, requires_course_id)
VALUES ('MSA-PROJECT', 'MSA 실무 프로젝트', 'ACTIVE', 20, 'SPRING-BOOT');

-- ==========================================
-- 4. 과목별 강의 시간 (Course Timeslot) 추가
-- ==========================================
INSERT INTO course_timeslot (course_id, timeslot) VALUES ('C-LANG', 'FRI_AM');
INSERT INTO course_timeslot (course_id, timeslot) VALUES ('COMP-THINKING', 'MON_AM');
INSERT INTO course_timeslot (course_id, timeslot) VALUES ('BASIC-JAVA', 'MON_PM');
INSERT INTO course_timeslot (course_id, timeslot) VALUES ('BASIC-JAVA', 'WED_AM');
INSERT INTO course_timeslot (course_id, timeslot) VALUES ('ADV-JAVA', 'TUE_PM');
INSERT INTO course_timeslot (course_id, timeslot) VALUES ('SPRING-BOOT', 'THU_AM');
INSERT INTO course_timeslot (course_id, timeslot) VALUES ('MSA-PROJECT', 'FRI_PM');

-- (나머지 학과, 학생, 기이수 과목 데이터는 기존과 동일하게 유지합니다.)
-- ==========================================
-- 5. 기이수 과목 (Completed Course) 삽입 (학생의 과거 기록)
-- ==========================================
-- Alice: C언어만 이수함 (고급 Java 수강 자격 미달)
INSERT INTO completed_course (student_id, course_id) VALUES ('S1', 'C-LANG');

-- Bob: 기초 Java 이수함 (고급 Java 수강 자격 충족)
INSERT INTO completed_course (student_id, course_id) VALUES ('S2', 'BASIC-JAVA');

-- Charlie: 타 학과지만 기초 Java 이수함 (고급 Java 수강 자격 충족)
INSERT INTO completed_course (student_id, course_id) VALUES ('S3', 'BASIC-JAVA');
INSERT INTO completed_course (student_id, course_id) VALUES ('S3', 'C-LANG');

-- David: 신입생 (아무것도 이수하지 않음)