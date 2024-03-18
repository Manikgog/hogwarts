-- Шаг 3

-- Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах (достаточно получить только имя и
-- возраст студента) школы Хогвартс вместе с названиями факультетов.

-- Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.

SELECT students.name, students.age, faculties.name
FROM students
INNER JOIN faculties ON students.faculty_id = faculties.id

SELECT students.name, students.age, avatar.data, avatar.file_path, avatar.file_size, avatar.media_type
FROM students
INNER JOIN avatar ON students.id = student_id

