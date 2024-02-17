package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository){
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }
    public Faculty create(Faculty faculty){
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }
    public Faculty update(long id, Faculty faculty){
        return facultyRepository.findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setColor(faculty.getColor());
                    oldFaculty.setName(faculty.getName());
                    return facultyRepository.save(oldFaculty);})
                .orElseThrow(() -> new NotFoundException(id));
    }
    public Faculty delete(long id){
        return facultyRepository.findById(id)
                .map(faculty -> {
                    facultyRepository.delete(faculty);
                    return faculty;
                })
                .orElseThrow(() -> new NotFoundException(id));
    }
    public Faculty get(long id){
        return facultyRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
    public List<Faculty> findByColorOrName(String nameOrColor){
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(nameOrColor, nameOrColor);
    }
    public List<Student> getStudentsOnFaculty(long faculty_id){
        return studentRepository.findByFacultyId(get(faculty_id).getId());
    }
}
