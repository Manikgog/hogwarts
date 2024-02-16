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

    @Test
    public void create_Test(){
        Faculty griffindor = new Faculty(1L, "Гриффиндор", "красный");
        Faculty puffenduy = new Faculty(2L, "Пуффендуй", "жёлтый");

        when(facultyRepository.save(griffindor)).thenReturn(griffindor);

        Faculty actualResult = facultyService.create(griffindor);
        Assertions.assertEquals(griffindor, actualResult);

        when(facultyRepository.save(puffenduy)).thenReturn(puffenduy);

        actualResult = facultyService.create(puffenduy);
        Assertions.assertEquals(puffenduy, actualResult);
    }
    @Test
    public void positive_update_Test(){
        Faculty griffindor = new Faculty(1L, "Гриффиндор", "красный");
        Faculty puffenduy = new Faculty(2L, "Пуффендуй", "жёлтый");
        Optional<Faculty> op = Optional.of(griffindor);
        when(facultyRepository.save(griffindor)).thenReturn(griffindor);
        when(facultyRepository.findById(1L)).thenReturn(op);

        Faculty actual = facultyService.update(1L, griffindor);
        Assertions.assertEquals(griffindor, actual);

        op = Optional.of(puffenduy);
        when(facultyRepository.save(puffenduy)).thenReturn(puffenduy);
        when(facultyRepository.findById(2L)).thenReturn(op);

        actual = facultyService.update(2, puffenduy);
        Assertions.assertEquals(puffenduy, actual);
    }
    @Test
    public void negative_update_Test(){
        Faculty griffindor = new Faculty(1L, "Гриффиндор", "красный");
        Faculty puffenduy = new Faculty(2L, "Пуффендуй", "жёлтый");
        Optional<Faculty> empty = Optional.empty();
        when(facultyRepository.findById(0L)).thenReturn(empty);

        Assertions.assertThrows(NotFoundException.class, ()->facultyService.update(0, griffindor));
        Assertions.assertThrows(NotFoundException.class, ()->facultyService.update(-1, puffenduy));
    }
    @Test
    public void positive_delete_Test(){
        Faculty griffindor = new Faculty(1L, "Гриффиндор", "красный");
        Faculty puffenduy = new Faculty(2L, "Пуффендуй", "жёлтый");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(griffindor));
        Faculty expected = griffindor;
        Assertions.assertEquals(expected, facultyService.delete(1L));

        when(facultyRepository.findById(2L)).thenReturn(Optional.of(puffenduy));
        expected = puffenduy;
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
        Faculty griffindor = new Faculty(1L, "Гриффиндор", "красный");
        Faculty puffenduy = new Faculty(2L, "Пуффендуй", "жёлтый");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(griffindor));
        Assertions.assertEquals(griffindor, facultyService.get(1L));

        when(facultyRepository.findById(2L)).thenReturn(Optional.of(puffenduy));
        Assertions.assertEquals(puffenduy, facultyService.get(2L));
    }
    @Test
    public void negative_get_Test(){
        Assertions.assertThrows(NotFoundException.class, () -> facultyService.get(-1));
        Assertions.assertThrows(NotFoundException.class, () -> facultyService.get(-2));
    }
    @Test
    public void positive_getAll_Test(){
        Faculty griffindor = new Faculty(1L, "Гриффиндор", "красный");
        Faculty puffenduy = new Faculty(2L, "Пуффендуй", "жёлтый");
        Faculty slizerin = new Faculty(3L, "Слизерин", "зеленый");
        Faculty kogtevran = new Faculty(4L, "Когтевран", "синий");
        List<Faculty> listOfFaculties = List.of(griffindor, puffenduy, slizerin, kogtevran);
        when(facultyRepository.findAll()).thenReturn(listOfFaculties);

        Assertions.assertEquals(listOfFaculties, facultyService.getAll());
    }
    @Test
    public void findByColor_Test(){
        Faculty griffindor = new Faculty(1L, "Гриффиндор", "красный");
        Faculty puffenduy = new Faculty(2L, "Пуффендуй", "красный");
        Faculty slizerin = new Faculty(3L, "Слизерин", "зеленый");
        Faculty kogtevran = new Faculty(4L, "Когтевран", "зеленый");
        when(facultyRepository.findByColorIgnoreCase("зеленый")).thenReturn(List.of(slizerin, kogtevran));
        Assertions.assertEquals(List.of(slizerin, kogtevran), facultyService.findByColor("зеленый"));

        when(facultyRepository.findByColorIgnoreCase("красный")).thenReturn(List.of(griffindor, puffenduy));
        Assertions.assertEquals(List.of(griffindor, puffenduy), facultyService.findByColor("красный"));

        when(facultyRepository.findByColorIgnoreCase("белый")).thenReturn(List.of());
        Assertions.assertEquals(List.of(), facultyService.findByColor("белый"));
    }
    @Test
    public void findByName_Test(){
        Faculty griffindor = new Faculty(1L, "Гриффиндор", "красный");
        Faculty puffenduy = new Faculty(2L, "Пуффендуй", "красный");
        Faculty slizerin = new Faculty(3L, "Слизерин", "зеленый");
        Faculty kogtevran = new Faculty(4L, "Когтевран", "зеленый");
        when(facultyRepository.findByNameIgnoreCase("когтевран")).thenReturn(List.of(kogtevran));
        Assertions.assertEquals(List.of(kogtevran), facultyService.findByName("когтевран"));

        when(facultyRepository.findByNameIgnoreCase("пуффендуй")).thenReturn(List.of(puffenduy));
        Assertions.assertEquals(List.of(puffenduy), facultyService.findByName("пуффендуй"));

        when(facultyRepository.findByNameIgnoreCase("слизеринд")).thenReturn(List.of());
        Assertions.assertEquals(List.of(), facultyService.findByName("слизеринд"));
    }

    @Test
    public void getStudentsOnFaculty_Test(){
        when(studentRepository.findAll()).thenReturn(List.of(POTTER, POLUMNA, CHANG, DIGGORY, GREINDGER, WISLY));
        List<Student> studentsOfGriffindorf_expected =  List.of(POTTER, CHANG);
        Assertions.assertEquals(studentsOfGriffindorf_expected, facultyService.getStudentsOnFaculty(1L, studentService));
    }
}
