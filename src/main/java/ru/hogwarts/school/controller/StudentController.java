package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.NotAllParametersException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
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
    @PutMapping("{id}")
    @Operation(summary = "Изменение студента")
    public Student update(@PathVariable long id, @RequestBody Student student){
        return studentService.update(id, student);
    }
    @DeleteMapping("{id}")  //http://localhost:8080/students/1
    @Operation(summary = "Удаление студента по идентификатору")
    public ResponseEntity<Student> delete(@PathVariable long id){
        Student deleted = studentService.delete(id);
        return ResponseEntity.ok(deleted);
    }
    @GetMapping("{id}") //http://localhost:8080/students/1
    @Operation(summary = "Получение студента по идентификатору")
    public Student get(@PathVariable long id){
        return studentService.get(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение массива всех студентов")
    public List<Student> getAll(){
        return studentService.getAll();
    }
    @GetMapping
    @Operation(summary = "Получение массива студентов с указанным возрастом или в промежутке возрастов")
    public ResponseEntity<List<Student>> findByAge(@RequestParam(required = false) Integer age, @RequestParam(required = false) Integer min, @RequestParam(required = false) Integer max){
        if(age != null){
            return ResponseEntity.ok(studentService.findByAge(age));
        }else if(min != null && max != null){
            return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
        }
        StringBuilder stringOfParamName = new StringBuilder();
        if (age == null){
            stringOfParamName.append("age ");
        }
        if(min == null){
            stringOfParamName.append("min ");
        }
        if(max == null){
            stringOfParamName.append("max");
        }
        throw new NotAllParametersException(stringOfParamName.toString());
    }
    @GetMapping(path = "/student_id")
    @Operation(summary = "Получение факультета студента по идентификатору студента")
    public Faculty getFacultyById(@RequestParam long student_id){
        return studentService.getFacultyByStudentId(student_id);
    }

}
