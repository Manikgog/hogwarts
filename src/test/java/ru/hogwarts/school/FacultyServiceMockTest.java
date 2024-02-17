package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static ru.hogwarts.school.constants.Constants.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceMockTest {
    @Mock
    private FacultyRepository facultyRepository;
    @InjectMocks
    private FacultyService facultyService;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    @BeforeEach
    public void init(){
        GRIFFINDOR.setId(1L);
        GRIFFINDOR.setName("Гриффиндор");
        GRIFFINDOR.setColor("красный");
        COGTEVRAN.setId(2L);
        COGTEVRAN.setName("Когтевран");
        COGTEVRAN.setColor("синий");
        PUFFENDUY.setId(3L);
        PUFFENDUY.setName("Пуффендуй");
        PUFFENDUY.setColor("желтый");
        SLIZERIN.setId(4L);
        SLIZERIN.setName("Слизерин");
        SLIZERIN.setColor("зеленый");

        POTTER.setId(1L);
        POTTER.setName("Гарри Поттер");
        POTTER.setAge(13);
        POLUMNA.setId(2L);
        POLUMNA.setName("Полумна Лавгуд");
        POLUMNA.setAge(12);
        CHANG.setId(3L);
        CHANG.setName("Чжоу Чанг");
        CHANG.setAge(13);
        WISLY.setId(4L);
        WISLY.setName("Рон Уизли");
        WISLY.setAge(13);
    }
    @Test
    public void create_Test(){


        when(facultyRepository.save(GRIFFINDOR)).thenReturn(GRIFFINDOR);

        Faculty actualResult = facultyService.create(GRIFFINDOR);
        Assertions.assertEquals(GRIFFINDOR, actualResult);

        when(facultyRepository.save(PUFFENDUY)).thenReturn(PUFFENDUY);

        actualResult = facultyService.create(PUFFENDUY);
        Assertions.assertEquals(PUFFENDUY, actualResult);
    }
    @Test
    public void positive_update_Test(){

        Optional<Faculty> op = Optional.of(GRIFFINDOR);
        when(facultyRepository.save(GRIFFINDOR)).thenReturn(GRIFFINDOR);
        when(facultyRepository.findById(1L)).thenReturn(op);

        Faculty actual = facultyService.update(1L, GRIFFINDOR);
        Assertions.assertEquals(GRIFFINDOR, actual);

        op = Optional.of(PUFFENDUY);
        when(facultyRepository.save(PUFFENDUY)).thenReturn(PUFFENDUY);
        when(facultyRepository.findById(2L)).thenReturn(op);

        actual = facultyService.update(2, PUFFENDUY);
        Assertions.assertEquals(PUFFENDUY, actual);
    }
    @Test
    public void negative_update_Test(){

        Optional<Faculty> empty = Optional.empty();
        when(facultyRepository.findById(0L)).thenReturn(empty);

        Assertions.assertThrows(NotFoundException.class, ()->facultyService.update(0, GRIFFINDOR));
        Assertions.assertThrows(NotFoundException.class, ()->facultyService.update(-1, PUFFENDUY));
    }
    @Test
    public void positive_delete_Test(){
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(GRIFFINDOR));
        Faculty expected = GRIFFINDOR;
        Assertions.assertEquals(expected, facultyService.delete(1L));

        when(facultyRepository.findById(2L)).thenReturn(Optional.of(PUFFENDUY));
        expected = PUFFENDUY;
        Assertions.assertEquals(expected, facultyService.delete(2L));
    }
    @Test
    public void negative_delete_Test(){
        when(facultyRepository.findById(1L)).thenReturn(Optional.empty());
        when(facultyRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> facultyService.delete(1L));
        Assertions.assertThrows(NotFoundException.class, () -> facultyService.delete(2L));
    }
    @Test
    public void positive_get_Test(){
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(GRIFFINDOR));
        Assertions.assertEquals(GRIFFINDOR, facultyService.get(1L));

        when(facultyRepository.findById(2L)).thenReturn(Optional.of(PUFFENDUY));
        Assertions.assertEquals(PUFFENDUY, facultyService.get(2L));
    }
    @Test
    public void negative_get_Test(){
        Assertions.assertThrows(NotFoundException.class, () -> facultyService.get(-1));
        Assertions.assertThrows(NotFoundException.class, () -> facultyService.get(-2));
    }

    @Test
    public void findByNameOfColor_Test(){
        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase("когтевран", "когтевран")).thenReturn(List.of(COGTEVRAN));
        Assertions.assertEquals(List.of(COGTEVRAN), facultyService.findByColorOrName("когтевран"));

        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase("пуффендуй", "пуффендуй")).thenReturn(List.of(PUFFENDUY));
        Assertions.assertEquals(List.of(PUFFENDUY), facultyService.findByColorOrName("пуффендуй"));

        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase("желтый", "желтый")).thenReturn(List.of(PUFFENDUY));
        Assertions.assertEquals(List.of(PUFFENDUY), facultyService.findByColorOrName("желтый"));

        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase("красный", "красный")).thenReturn(List.of(GRIFFINDOR));
        Assertions.assertEquals(List.of(GRIFFINDOR), facultyService.findByColorOrName("красный"));

        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase("слизеринд", "слизеринд")).thenReturn(List.of());
        Assertions.assertEquals(List.of(), facultyService.findByColorOrName("слизеринд"));
    }

    @Test
    public void getStudentsOnFaculty_Test(){
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(GRIFFINDOR));
        when(studentRepository.findByFacultyId(1L)).thenReturn(List.of(POTTER, POLUMNA, WISLY));
        List<Student> studentsOfGriffindorf_expected =  List.of(POTTER, POLUMNA, WISLY);
        Assertions.assertEquals(studentsOfGriffindorf_expected, facultyService.getStudentsOnFaculty(1L));
    }
}
