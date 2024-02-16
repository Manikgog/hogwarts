package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static ru.hogwarts.school.constants.Constants.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceMockTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    @Test
    public void create_Test(){
        when(studentRepository.save(POTTER)).thenReturn(POTTER);

        Student actualResult = studentService.create(POTTER);
        Assertions.assertEquals(POTTER, actualResult);

        when(studentRepository.save(CHANG)).thenReturn(CHANG);

        actualResult = studentService.create(CHANG);
        Assertions.assertEquals(CHANG, actualResult);
    }
    @Test
    public void positive_update_Test(){
        Optional<Student> op = Optional.of(POTTER);
        when(studentRepository.save(POTTER)).thenReturn(POTTER);
        when(studentRepository.findById(2L)).thenReturn(op);

        Student actual = studentService.update(2, POTTER);
        Assertions.assertEquals(POTTER, actual);

        op = Optional.of(CHANG);
        when(studentRepository.save(CHANG)).thenReturn(CHANG);
        when(studentRepository.findById(3L)).thenReturn(op);

        actual = studentService.update(3, CHANG);
        Assertions.assertEquals(CHANG, actual);
    }

    @Test
    public void negative_update_Test(){
        Optional<Student> empty = Optional.empty();
        when(studentRepository.findById(0L)).thenReturn(empty);

        Assertions.assertThrows(NotFoundException.class, ()->studentService.update(0, POTTER));
        Assertions.assertThrows(NotFoundException.class, ()->studentService.update(-1, CHANG));
    }

    @Test
    public void positive_delete_Test(){
        when(studentRepository.findById(2L)).thenReturn(Optional.of(POTTER));
        Student expected = POTTER;
        Assertions.assertEquals(expected, studentService.delete(2L));

        when(studentRepository.findById(3L)).thenReturn(Optional.of(CHANG));
        expected = CHANG;
        Assertions.assertEquals(expected, studentService.delete(3L));
    }

    @Test
    public void negative_delete_Test(){
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
        when(studentRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> studentService.delete(2L));
        Assertions.assertThrows(NotFoundException.class, () -> studentService.delete(3L));
    }

    @Test
    public void positive_get_Test(){
        when(studentRepository.findById(2L)).thenReturn(Optional.of(POTTER));
        Assertions.assertEquals(POTTER, studentService.get(2L));

        when(studentRepository.findById(3L)).thenReturn(Optional.of(CHANG));
        Assertions.assertEquals(CHANG, studentService.get(3L));
    }

    @Test
    public void negative_get_Test(){
        Assertions.assertThrows(NotFoundException.class, () -> studentService.get(-1));
        Assertions.assertThrows(NotFoundException.class, () -> studentService.get(-2));
    }

    @Test
    public void positive_getAll_Test(){
        List<Student> listOfStudent = List.of(POTTER, CHANG);
        when(studentRepository.findAll()).thenReturn(listOfStudent);

        Assertions.assertEquals(listOfStudent, studentService.getAll());
    }
    @Test
    public void findByAge_Test(){
        when(studentRepository.findByAge(12)).thenReturn(List.of(POTTER, GREINDGER));
        Assertions.assertEquals(List.of(POTTER, GREINDGER), studentService.findByAge(12));

        when(studentRepository.findByAge(13)).thenReturn(List.of(CHANG, DIGGORY, WISLY));
        Assertions.assertEquals(List.of(CHANG, DIGGORY, WISLY), studentService.findByAge(13));

        when(studentRepository.findByAge(14)).thenReturn(List.of());
        Assertions.assertEquals(List.of(), studentService.findByAge(14));
    }
    @Test
    public void findByAgeBetween_Test(){
        when(studentRepository.findByAgeBetween(11, 12)).thenReturn(List.of(POTTER, GREINDGER));
        Assertions.assertEquals(List.of(POTTER, GREINDGER), studentService.findByAgeBetween(11, 12));

        when(studentRepository.findByAgeBetween(13, 14)).thenReturn(List.of(CHANG, DIGGORY, WISLY));
        Assertions.assertEquals(List.of(CHANG, DIGGORY, WISLY), studentService.findByAgeBetween(13, 14));

        when(studentRepository.findByAgeBetween(14, 16)).thenReturn(List.of());
        Assertions.assertEquals(List.of(), studentService.findByAgeBetween(14, 16));
    }

    @Test
    public void positive_getFacultyByStudentId_Test(){
        when(studentRepository.findById(1L)).thenReturn(Optional.of(POTTER));
        Faculty expected = GRIFFINDOR;

        Assertions.assertEquals(expected, studentService.getFacultyByStudentId(1L));

        when(studentRepository.findById(2L)).thenReturn(Optional.of(POLUMNA));
        expected = COGTEVRAN;
         Assertions.assertEquals(expected, studentService.getFacultyByStudentId(2L));
    }
    @Test
    public void negative_getFacultyByStudentId_Test(){
        Assertions.assertThrows(NotFoundException.class, () -> studentService.getFacultyByStudentId(0L));
    }
}
