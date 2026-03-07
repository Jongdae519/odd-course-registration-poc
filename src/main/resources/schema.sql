-- schema.sql
CREATE TABLE department (
                            id VARCHAR(50) PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);

CREATE TABLE student (
                         id VARCHAR(50) PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         department_id VARCHAR(50) NOT NULL,
                         FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE TABLE course (
                        id VARCHAR(50) PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        allows_capacity INT NOT NULL,
                        requires_course_id VARCHAR(50),
                        FOREIGN KEY (requires_course_id) REFERENCES course(id)
);

-- timeslot을 위한 별도의 컬렉션 테이블 생성
CREATE TABLE course_timeslot (
                                 course_id VARCHAR(50) NOT NULL,
                                 timeslot VARCHAR(20) NOT NULL, -- 예: 'MON_AM', 'WED_AM'
                                 PRIMARY KEY (course_id, timeslot),
                                 FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE completed_course (
                                  student_id VARCHAR(50) NOT NULL,
                                  course_id VARCHAR(50) NOT NULL,
                                  PRIMARY KEY (student_id, course_id),
                                  FOREIGN KEY (student_id) REFERENCES student(id),
                                  FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE enrollment (
                            student_id VARCHAR(50) NOT NULL,
                            course_id VARCHAR(50) NOT NULL,
                            PRIMARY KEY (student_id, course_id),
                            FOREIGN KEY (student_id) REFERENCES student(id),
                            FOREIGN KEY (course_id) REFERENCES course(id)
);