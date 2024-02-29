package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.hogwarts.school.constants.Constants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplate {
    @LocalServerPort
    private int port;
    @Autowired
    private StudentController studentController;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AvatarController avatarController;
    @Autowired
    private AvatarService avatarService;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    private static boolean test(Faculty f) {
        return f.getName().equals(FACULTY_1.getName()) &&
                f.getColor().equals(FACULTY_1.getColor());
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
        Assertions.assertThat(studentService).isNotNull();
        Assertions.assertThat(studentRepository).isNotNull();

        Assertions.assertThat(facultyController).isNotNull();
        Assertions.assertThat(facultyService).isNotNull();
        Assertions.assertThat(facultyRepository).isNotNull();

        Assertions.assertThat(avatarController).isNotNull();
        Assertions.assertThat(avatarService).isNotNull();
        Assertions.assertThat(avatarRepository).isNotNull();
    }
    @Test
    public void test_create_faculty() {

        Assertions.assertThat(this
                        .restTemplate
                        .postForObject("http://localhost:" + port + "/faculties", FACULTY_1, Faculty.class))
                .matches(faculty -> faculty.getName().equals(FACULTY_1.getName()) &&
                        faculty.getColor().equals(FACULTY_1.getColor()), "данные факультетов равны");

        Faculty fac = facultyRepository.findAll().stream().filter(faculty ->    // получение загруженного в базу данных объекта для его последующего удаления
                        faculty.getName().equals(FACULTY_1.getName()) &&
                        faculty.getColor().equals(FACULTY_1.getColor())
        ).findFirst().get();

        // удаление загруженного в БД объекта для тестирования
        this.restTemplate
                .delete("http://localhost:" + port + "/faculties/" + fac.getId(), Student.class);

    }

    @Test
    public void test_update(){
        facultyRepository.save(FACULTY_1);

        Faculty faculty = facultyRepository.findAll().stream().filter(f ->
                        f.getName().equals(FACULTY_1.getName()) &&
                        f.getColor().equals(FACULTY_1.getColor())
        ).findFirst().get();

        FACULTY_1.setId(faculty.getId());

        this.restTemplate
                .put("http://localhost:" + port + "/faculties/" + FACULTY_1.getId(), FACULTY_1);

        org.junit.jupiter.api.Assertions.assertEquals(FACULTY_1.getId(), facultyRepository.findById(FACULTY_1.getId()).get().getId());
        org.junit.jupiter.api.Assertions.assertEquals(FACULTY_1.getName(), facultyRepository.findById(FACULTY_1.getId()).get().getName());
        org.junit.jupiter.api.Assertions.assertEquals(FACULTY_1.getColor(), facultyRepository.findById(FACULTY_1.getId()).get().getColor());

        facultyRepository.delete(FACULTY_1);
    }
    @Test
    public void test_delete(){
        facultyRepository.save(FACULTY_1);

        Faculty faculty = facultyRepository.findAll().stream().filter(f ->
                        f.getName().equals(FACULTY_1.getName()) &&
                        f.getColor().equals(FACULTY_1.getColor())
        ).findFirst().get();

        FACULTY_1.setId(faculty.getId());

        this.restTemplate
                .delete("http://localhost:" + port + "/faculties/" + FACULTY_1.getId(), Faculty.class);

        org.junit.jupiter.api.Assertions.assertEquals(Optional.empty(), facultyRepository.findById(FACULTY_1.getId()));
    }
    @Test
    public void test_get(){
        facultyRepository.save(FACULTY_1);

        Faculty faculty = facultyRepository.findAll().stream().filter(FacultyControllerTestRestTemplate::test
        ).findFirst().get();

        FACULTY_1.setId(faculty.getId());

        Assertions.assertThat(this
                        .restTemplate
                        .getForObject("http://localhost:" + port + "/faculties/" + FACULTY_1.getId(), Faculty.class))
                .matches(f -> f.getId().equals(FACULTY_1.getId()) &&
                        f.getName().equals(FACULTY_1.getName()) &&
                        f.getColor().equals(FACULTY_1.getColor()), "данные факультетов равны");

        facultyRepository.delete(FACULTY_1);
    }
    @Test
    public void test_findByColor(){
        facultyRepository.save(FACULTY_1);
        facultyRepository.save(FACULTY_2);
        facultyRepository.save(FACULTY_3);
        facultyRepository.save(FACULTY_4);

        List<Faculty> redList = new ArrayList<>();
        redList.add(FACULTY_1);

        Assertions.assertThat(this
                        .restTemplate
                        .getForEntity("http://localhost:" + port + "/faculties?nameOrColor=" + FACULTY_1.getColor(), List.class))
                .matches(e -> {
                    var actualList = Objects.requireNonNull(e
                                    .getBody())
                            .stream()
                            .toList();
                    return actualList.size() == redList.size();
                });

        Assertions.assertThat(this
                        .restTemplate
                        .getForEntity("http://localhost:" + port + "/faculties?nameOrColor=" + FACULTY_1.getName(), List.class))
                .matches(e -> {
                    var actualList = Objects.requireNonNull(e
                                    .getBody())
                            .stream()
                            .toList();
                    return actualList.size() == redList.size();
                });

        facultyRepository.delete(FACULTY_1);
        facultyRepository.delete(FACULTY_2);
        facultyRepository.delete(FACULTY_3);
        facultyRepository.delete(FACULTY_4);
    }
    @Test
    public void test_getStudentsByFaculty(){
        facultyRepository.save(FACULTY_1);
        facultyRepository.save(FACULTY_2);
        facultyRepository.save(FACULTY_3);
        facultyRepository.save(FACULTY_4);

        long firstFacultyId = getFacultyId(FACULTY_1);
        long secondFacultyId = getFacultyId(FACULTY_2);
        long thirdFacultyId = getFacultyId(FACULTY_3);
        long fourthFacultyId = getFacultyId(FACULTY_4);

        FACULTY_1.setId(firstFacultyId);
        FACULTY_2.setId(secondFacultyId);
        FACULTY_3.setId(thirdFacultyId);
        FACULTY_4.setId(fourthFacultyId);

        BING.setFaculty(FACULTY_1);
        JOE.setFaculty(FACULTY_1);
        RACHEL.setFaculty(FACULTY_2);
        ROSS.setFaculty(FACULTY_3);
        MONICA.setFaculty(FACULTY_4);
        FUIBY.setFaculty(FACULTY_3);

        studentRepository.save(BING);
        studentRepository.save(JOE);
        studentRepository.save(RACHEL);
        studentRepository.save(ROSS);
        studentRepository.save(MONICA);
        studentRepository.save(FUIBY);


        List<Student> faculty_1_Students = new ArrayList<>();
        faculty_1_Students.add(BING);
        faculty_1_Students.add(JOE);


        Assertions.assertThat(this
                        .restTemplate
                        .getForEntity("http://localhost:" + port + "/faculties/" + BING.getFaculty().getId() + "/students", List.class))
                .matches(e -> {
                    var actualList = Objects.requireNonNull(e
                                    .getBody())
                            .stream()
                            .toList();
                    return actualList.size() == faculty_1_Students.size();
                });

        studentRepository.delete(BING);
        studentRepository.delete(JOE);
        studentRepository.delete(RACHEL);
        studentRepository.delete(ROSS);
        studentRepository.delete(MONICA);
        studentRepository.delete(FUIBY);

        facultyRepository.delete(FACULTY_1);
        facultyRepository.delete(FACULTY_2);
        facultyRepository.delete(FACULTY_3);
        facultyRepository.delete(FACULTY_4);
    }

    /**
     * Метод для очистки базы данных после тестирования
     */
    @AfterEach
    public void clearDB(){
        // очистка факультетов
        List<Faculty> listFaculties = facultyRepository.findAll();
        List<Faculty> facultiesToDelete = List.of(
                FACULTY_1, FACULTY_2, FACULTY_3, FACULTY_4
        );
        List<Long> idFaculties = new ArrayList<>();
        for (Faculty faculty : facultiesToDelete) {  // получение идентификаторов факультетов для их удаления из БД
            listFaculties.stream().filter(f -> f.getName()
                    .equals(faculty.getName()) && f.getColor().equals(faculty.getColor()))
                    .mapToLong(Faculty::getId).forEach(idFaculties::add);
        }

        for (Long id : idFaculties){    // удаление по идентификатору
            facultyRepository.deleteById(id);
        }

        // очистка студентов
        List<Student> listStudents = studentRepository.findAll();
        List<Student> studentsToDelete = List.of(
                BING, JOE, ROSS, RACHEL, FUIBY, MONICA
        );
        List<Long> idStudents = new ArrayList<>();
        for (Student student : studentsToDelete) {  // получение идентификаторов студентов для их удаления из БД
            listStudents.stream().filter(s -> s.getAge() == student.getAge() &&
                    s.getName().equals(student.getName()))
                    .mapToLong(Student::getId).forEach(idStudents::add);
        }

        for (Long id : idStudents){    // удаление по идентификатору
            studentRepository.deleteById(id);
        }
    }

    private long getFacultyId(Faculty faculty){
        return facultyRepository.findAll().stream().filter(s -> {
                    return s.getName().equals(faculty.getName()) &&
                            s.getColor().equals(faculty.getColor());
                }
        ).findFirst().get().getId();
    }
}
