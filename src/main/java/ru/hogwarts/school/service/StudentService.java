package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }
    public Student create(Student student){
        student.setId(null);
        return studentRepository.save(student);
    }
    public Student update(long id, Student student){
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(student.getName());
                    oldStudent.setAge(student.getAge());
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

}
