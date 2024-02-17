package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entity.Faculty;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColorIgnoreCase(String color);
    List<Faculty> findByNameIgnoreCase(String name);
    List<Faculty> findByColorIgnoreCaseOrNameIgnoreCase(String color, String name);
}