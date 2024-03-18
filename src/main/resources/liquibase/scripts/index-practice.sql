-- liquibase formatted sql

-- changeset mgogolin:1
CREATE INDEX students_name_index ON students(name);

-- changeset mgogolin:2
CREATE INDEX faculty_name_color_index ON faculties(name, color);