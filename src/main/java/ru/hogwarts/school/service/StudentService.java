package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }
    public Student create(Student student){
        return studentRepository.save(student);
    }
    public Student update(long id, Student student){
        get(id);
        student.setId(id);
        return studentRepository.save(student);
    }

    public Student delete(long id){
        Student deleted = get(id);
        studentRepository.deleteById(id);
        return deleted;
    }
    public Student get(long id){
        return studentRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
    public List<Student> getAll(){
        return studentRepository.findAll();
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
