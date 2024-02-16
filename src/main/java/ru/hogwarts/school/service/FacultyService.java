package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    public FacultyService(FacultyRepository facultyRepository){
        this.facultyRepository = facultyRepository;
    }
    public Faculty create(Faculty faculty){
        return facultyRepository.save(faculty);
    }
    public Faculty update(long id, Faculty faculty){
        get(id);
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public Faculty delete(long id){
        Faculty deleted = get(id);
        facultyRepository.deleteById(id);
        return deleted;
    }
    public Faculty get(long id){
        return facultyRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
    public List<Faculty> getAll() {
        return facultyRepository.findAll();
    }
    public List<Faculty> findByColor(String color){
        return facultyRepository.findByColorIgnoreCase(color);
    }
    public List<Faculty> findByName(String name){
        return facultyRepository.findByNameIgnoreCase(name);
    }
    public List<Student> getStudentsOnFaculty(long faculty_id, StudentService studentService){
        List<Student> studentsList = studentService.getAll();
        return studentsList.stream().filter(s -> s.getFaculty().getId() == faculty_id).toList();
    }
}
