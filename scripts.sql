SELECT * FROM student WHERE age > 11 AND age < 13

SELECT s.name FROM student as s;

SELECT s.name FROM student as s WHERE s.name LIKE '%е%';

SELECT * FROM student as s WHERE s.age < s.id;

SELECT * FROM student as s ORDER BY s.age;