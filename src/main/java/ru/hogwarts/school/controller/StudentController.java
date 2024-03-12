package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.StudentService;
import java.util.List;

@Tag(name = "Студенты", description = "Эндпоинты для работы со студентами")
@RequestMapping("/students")
@RestController
public class StudentController {
    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping
    @Operation(summary = "Добавление студента")
    public Student create(@RequestBody Student student){
        return studentService.create(student);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Изменение студента")
    public Student update(@PathVariable long id, @RequestBody Student student){
        return studentService.update(id, student);
    }
    @DeleteMapping("/{id}")  //http://localhost:8080/students/1
    @Operation(summary = "Удаление студента по идентификатору")
    public ResponseEntity<Student> delete(@PathVariable long id){
        Student deleted = studentService.delete(id);
        return ResponseEntity.ok(deleted);
    }
    @GetMapping("/{id}") //http://localhost:8080/students/1
    @Operation(summary = "Получение студента по идентификатору")
    public Student get(@PathVariable long id){
        return studentService.get(id);
    }

    @GetMapping(params = "age")
    @Operation(summary = "Получение массива студентов с указанным возрастом")
    public ResponseEntity<List<Student>> findByAge(@RequestParam int age){
        return ResponseEntity.ok(studentService.findByAge(age));
    }
    @GetMapping(params = {"min", "max"})
    @Operation(summary = "Получение массива студентов в промежутке указанных возрастов")
    public ResponseEntity<List<Student>> findByAgeBetween(@RequestParam int min, @RequestParam int max){
        return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
    }
    @GetMapping("/{id}/faculty")
    @Operation(summary = "Получение факультета студента по идентификатору студента")
    public Faculty getFacultyById(@PathVariable long id){
        return studentService.getFacultyByStudentId(id);
    }
    @GetMapping("/amount")
    @Operation(summary = "Получение общего количества студентов")
    public int getStudentsAmount(){
        return studentService.getStudentsAmount();
    }
    @GetMapping("/averageAge")
    @Operation(summary = "Получение среднего возраста студентов")
    public float getAverageAge(){
        return studentService.getAverageAge();
    }
    @GetMapping("/5last")
    @Operation(summary = "Получение списка пяти последних студентов")
    public List<Student> get5LastStudents(){
        return studentService.get5LastStudents();
    }

}
