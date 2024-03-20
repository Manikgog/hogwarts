package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AvatarControllerTestRestTemplateTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private final Faker faker = new Faker();
    private List<Student> students = new ArrayList<>(10);
    @AfterEach
    public void afterEach(){
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @BeforeEach
    public void beforeEach(){
        Faculty faculty1 = createFaculty();
        Faculty faculty2 = createFaculty();

        createStudents(faculty1, faculty2);

    }
    private Faculty createFaculty(){
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return facultyRepository.save(faculty);
    }

    /**
     * метод для заполнения базы данных студентами со случайными именами
     * и интервалом возрастов, которые генерируются объектом класса Faker faker
     * @param faculties
     */
    private void createStudents(Faculty... faculties) {
        int numberOfStudents = 5;
        int minAge = 11;
        int maxAge = 18;
        students.clear();
        Stream.of(faculties).forEach(faculty -> {
                    students.addAll(
                            studentRepository.saveAll(
                                    Stream.generate(() -> {
                                                Student student = new Student();
                                                student.setFaculty(faculty);
                                                student.setName(faker.harryPotter().character());
                                                student.setAge(faker.random().nextInt(minAge, maxAge));
                                                return student;
                                            })
                                            .limit(numberOfStudents)
                                            .collect(Collectors.toList())
                            ));
                }
        );
    }
    private String buildUri(String uriStartsWithSlash){
        return "http://localhost:%d%s".formatted(port, uriStartsWithSlash);
    }

}
