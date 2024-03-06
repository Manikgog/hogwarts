package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    public StudentService(StudentRepository studentRepository,
                            FacultyRepository facultyRepository){
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }
    public Student create(Student student){
        student.setId(null);
        fillFaculty(student.getFaculty(), student);
        return studentRepository.save(student);
    }
    public Student update(long id, Student student){
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(student.getName());
                    oldStudent.setAge(student.getAge());
                    fillFaculty(student.getFaculty(), student);
                    oldStudent.setFaculty(student.getFaculty());
                    return studentRepository.save(oldStudent);
                })
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Student delete(long id){
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    studentRepository.delete(oldStudent);
                    return oldStudent;
                })
                .orElseThrow(() -> new NotFoundException(id));
    }
    public Student get(long id){
        return studentRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
    public List<Student> findByAge(int age){
        return studentRepository.findByAge(age);
    }
    public List<Student> findByAgeBetween(int min, int max){
        return studentRepository.findByAgeBetween(min, max);
    }
    public Faculty getFacultyByStudentId(long student_id){
        return get(student_id).getFaculty();
    }
    public void fillFaculty(Faculty faculty, Student student){
        if(faculty != null && faculty.getId() != null){
            Faculty facultyFromDb = facultyRepository.findById(faculty.getId())
                    .orElseThrow(() -> new NotFoundException(faculty.getId()));
            student.setFaculty(facultyFromDb);
        }
    }

}
