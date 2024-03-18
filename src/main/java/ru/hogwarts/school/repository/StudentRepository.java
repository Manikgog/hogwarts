package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int min, int max);
    List<Student> findByFacultyId(long faculty_id);
    @Query(value = "SELECT COUNT(*) FROM students", nativeQuery = true)
    int getStudentsAmount();
    @Query(value = "SELECT AVG(age) FROM students", nativeQuery = true)
    float getAverageAge();
    @Query(value = "SELECT * FROM public.students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> get5LastStudents();
    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT :count", nativeQuery = true)
    List<Student> getLastNStudents(@Param("count") int count);
}
