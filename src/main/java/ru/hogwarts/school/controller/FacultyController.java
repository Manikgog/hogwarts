package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.NotAllParametersException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@Tag(name = "Факультеты", description = "Эндпоинты для работы с факультетами")
@RequestMapping("/faculties")
@RestController
public class FacultyController {
    private final FacultyService facultyService;
    private final StudentService studentService;
    public FacultyController(FacultyService facultyService, StudentService studentService) {
        this.facultyService = facultyService;
        this.studentService = studentService;
    }
    @PostMapping
    @Operation(summary = "Создание факультета")
    public Faculty create(@RequestBody Faculty faculty){
        return facultyService.create(faculty);
    }
    @PutMapping("{id}")
    @Operation(summary = "Изменение факультета по идентификатору")
    public Faculty update(@PathVariable long id, @RequestBody Faculty faculty){
        return facultyService.update(id, faculty);
    }
    @DeleteMapping("{id}")
    @Operation(summary = "Удаление факультета по идентификатору")
    public ResponseEntity<Faculty> delete(@PathVariable long id){
        Faculty deleted = facultyService.delete(id);
        return ResponseEntity.ok(deleted);
    }
    @GetMapping("{id}")
    @Operation(summary = "Получение факультета по идентификатору")
    public Faculty get(@PathVariable long id){
        return facultyService.get(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение массива всех факультетов")
    public List<Faculty> getAll(){
        return facultyService.getAll();
    }
    @GetMapping
    @Operation(summary = "Получение массива факультетов с указанным цветом или именем")
    public List<Faculty> findByColor(@RequestParam(required = false) String color, @RequestParam(required = false) String name){
        if(color != null && !color.isBlank()){
            return facultyService.findByColor(color);
        }
        if(name != null && !name.isBlank()){
            return facultyService.findByName(name);
        }
        throw new NotAllParametersException("color или name");
    }

    @GetMapping(path = "/faculty_id")
    @Operation(summary = "Получение массива студентов указанного факультета")
    public List<Student> getStudentsByFaculty(@RequestParam long faculty_id){
        return facultyService.getStudentsOnFaculty(faculty_id, studentService);
    }
}

