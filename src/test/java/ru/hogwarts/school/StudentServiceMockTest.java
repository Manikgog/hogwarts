package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceMockTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    @Test
    public void create_Test(){
        Student potter = new Student(0L, "Гарри Поттер", 12);
        Student chang = new Student(0L, "Чжоу Чанг", 13);
        when(studentRepository.save(potter)).thenReturn(potter);

        Student actualResult = studentService.create(potter);
        Assertions.assertEquals(potter, actualResult);

        when(studentRepository.save(chang)).thenReturn(chang);

        actualResult = studentService.create(chang);
        Assertions.assertEquals(chang, actualResult);
    }
    @Test
    public void positive_update_Test(){
        Student potter = new Student(2L, "Гарри Поттер", 12);
        Student chang = new Student(3L, "Чжоу Чанг", 13);
        Optional<Student> op = Optional.of(potter);
        when(studentRepository.save(potter)).thenReturn(potter);
        when(studentRepository.findById(2L)).thenReturn(op);

        Student actual = studentService.update(2, potter);
        Assertions.assertEquals(potter, actual);

        op = Optional.of(chang);
        when(studentRepository.save(chang)).thenReturn(chang);
        when(studentRepository.findById(3L)).thenReturn(op);

        actual = studentService.update(3, chang);
        Assertions.assertEquals(chang, actual);
    }

    @Test
    public void negative_update_Test(){
        Student potter = new Student(2L, "Гарри Поттер", 12);
        Student chang = new Student(3L, "Чжоу Чанг", 13);
        Optional<Student> empty = Optional.empty();
        when(studentRepository.findById(0L)).thenReturn(empty);

        Assertions.assertThrows(NotFoundException.class, ()->studentService.update(0, potter));
        Assertions.assertThrows(NotFoundException.class, ()->studentService.update(-1, chang));
    }

    @Test
    public void positive_delete_Test(){
        Student potter = new Student(2L, "Гарри Поттер", 12);
        Student chang = new Student(3L, "Чжоу Чанг", 13);
        when(studentRepository.findById(2L)).thenReturn(Optional.of(potter));
        Student expected = potter;
        Assertions.assertEquals(expected, studentService.delete(2L));

        when(studentRepository.findById(3L)).thenReturn(Optional.of(chang));
        expected = chang;
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
        Student potter = new Student(2L, "Гарри Поттер", 12);
        Student chang = new Student(3L, "Чжоу Чанг", 13);
        when(studentRepository.findById(2L)).thenReturn(Optional.of(potter));
        Assertions.assertEquals(potter, studentService.get(2L));

        when(studentRepository.findById(3L)).thenReturn(Optional.of(chang));
        Assertions.assertEquals(chang, studentService.get(3L));
    }

    @Test
    public void negative_get_Test(){
        Assertions.assertThrows(NotFoundException.class, () -> studentService.get(-1));
        Assertions.assertThrows(NotFoundException.class, () -> studentService.get(-2));
    }

    @Test
    public void positive_getAll_Test(){
        Student potter = new Student(2L, "Гарри Поттер", 12);
        Student chang = new Student(3L, "Чжоу Чанг", 13);
        List<Student> listOfStudent = List.of(potter, chang);
        when(studentRepository.findAll()).thenReturn(listOfStudent);

        Assertions.assertEquals(listOfStudent, studentService.getAll());
    }
    @Test
    public void findByAge_Test(){
        Student potter = new Student(2L, "Гарри Поттер", 12);
        Student chang = new Student(3L, "Чжоу Чанг", 13);
        Student grandger = new Student(4L, "Гермиона Грейнджер", 12);
        Student diggory = new Student(5L, "Седрик Диггори", 13);
        Student wisly = new Student(6L, "Рон Уизли", 13);
        when(studentRepository.findByAge(12)).thenReturn(List.of(potter, grandger));
        Assertions.assertEquals(List.of(potter, grandger), studentService.findByAge(12));

        when(studentRepository.findByAge(13)).thenReturn(List.of(chang, diggory, wisly));
        Assertions.assertEquals(List.of(chang, diggory, wisly), studentService.findByAge(13));

        when(studentRepository.findByAge(14)).thenReturn(List.of());
        Assertions.assertEquals(List.of(), studentService.findByAge(14));
    }
    @Test
    public void findByAgeBetween_Test(){
        Student potter = new Student(2L, "Гарри Поттер", 12);
        Student chang = new Student(3L, "Чжоу Чанг", 13);
        Student grandger = new Student(4L, "Гермиона Грейнджер", 12);
        Student diggory = new Student(5L, "Седрик Диггори", 13);
        Student wisly = new Student(6L, "Рон Уизли", 13);
        when(studentRepository.findByAgeBetween(11, 12)).thenReturn(List.of(potter, grandger));
        Assertions.assertEquals(List.of(potter, grandger), studentService.findByAgeBetween(11, 12));

        when(studentRepository.findByAgeBetween(13, 14)).thenReturn(List.of(chang, diggory, wisly));
        Assertions.assertEquals(List.of(chang, diggory, wisly), studentService.findByAgeBetween(13, 14));

        when(studentRepository.findByAgeBetween(14, 16)).thenReturn(List.of());
        Assertions.assertEquals(List.of(), studentService.findByAgeBetween(14, 16));
    }
}
