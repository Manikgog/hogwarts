package ru.hogwarts.school.controller;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplateTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    private final Faker faker = new Faker();
    private List<Faculty> faculties = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    @AfterEach
    public void afterEach(){
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }
    @BeforeEach
    public void beforeEach(){
        faculties.clear();
        students.clear();
        for (int i = 0; i < 10; i++) {
            faculties.add(createFaculty());
        }
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
    private void createStudents(List<Faculty> faculties) {
        int numberOfStudents = 5;
        int minAge = 10;
        int maxAge = 20;
        faculties.forEach(faculty -> {
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

    /**
     * метод для проверки работы программы при создании факультета с
     * помощью метода create(@RequestBody Faculty faculty) класса FacultyController
     */
    @Test
    public void createTest(){

        Faculty faculty = new Faculty();
        faculty.setName("test");
        faculty.setColor("test");
        ResponseEntity<Faculty> responseEntity = testRestTemplate.postForEntity(
                buildUri("/faculties"),
                faculty,
                Faculty.class
        );
        Faculty facultyFromDb = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyFromDb).isNotNull();
        assertThat(facultyFromDb)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);

        faculty.setId(facultyFromDb.getId());
        // проверка наличия добавленного факультета в базе данных
        assertThat(facultyRepository.findById(facultyFromDb.getId()))
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(faculty));
    }
    /**
     * метод для проверки работы программы при вызове метода
     * update(@PathVariable long id, @RequestBody Faculty faculty)
     * класса FacultyController
     * с существующим в базе данных идентификатором факультета идентификатором факультета
     */
    @Test
    public void updatePositiveTest(){
        Faculty faculty = new Faculty();
        long id = faculties.get(faker.random().nextInt(faculties.size())).getId();
        faculty.setId(id);
        faculty.setName("TestName");
        faculty.setColor("testColor");

        ResponseEntity<Faculty> responseEntity = testRestTemplate.exchange(
                buildUri("/faculties/" + faculty.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(faculty),
                new ParameterizedTypeReference<>(){},
                Map.of("id", faculty.getId())
        );
        Faculty facultyFromDb = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyFromDb).usingRecursiveComparison().isEqualTo(faculty);

        // проверка наличия добавленного факультета в базе данных
        assertThat(facultyRepository.findById(faculty.getId()))
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(faculty));
    }

    /**
     * метод для проверки работы программы при вызове метода
     * update(@PathVariable long id, @RequestBody Faculty faculty)
     * класса FacultyController
     * с неправильным идентификатором факультета
     */
    @Test
    public void updateNegativeTest(){
        long id = -1L;
        Faculty faculty = new Faculty();
        faculty.setId(id);

        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(
                buildUri("/faculties/" + id),
                String.class,
                Map.of("id", id)
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ресурс с id = %d не найден!".formatted(id));
    }
    @Test
    public void deletePositiveTest(){
        long id = faculties.get(faker.random().nextInt(faculties.size())).getId();
        Faculty expected = facultyRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        ResponseEntity<Faculty> responseEntity = testRestTemplate.exchange(
                buildUri("/faculties/" + id),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>(){},
                Map.of("id", id)
        );
        Faculty actual = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
        // проверка отсутствия объекта в базе данных
        Optional<Faculty> noFaculty = facultyRepository.findById(id);
        assertThat(noFaculty).isEmpty();

    }
    @Test
    public void deleteNegativeTest(){
        long id = -1L;
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                buildUri("/faculties/" + id),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>(){},
                Map.of("id", id)
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ресурс с id = %d не найден!".formatted(id));
    }
    @Test
    public void getPositiveTest(){
        long id = faculties.get(faker.random().nextInt(faculties.size())).getId();
        Faculty expectedFaculty = facultyRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        ResponseEntity<Faculty> responseEntity = testRestTemplate.getForEntity(
                buildUri("/faculties/" + id),
                Faculty.class,
                Map.of("id", id)
        );
        Faculty facultyFromResponse = responseEntity.getBody();
        assertThat(facultyFromResponse).isNotNull();
        assertThat(facultyFromResponse).usingRecursiveComparison().isEqualTo(expectedFaculty);
    }
    @Test
    public void getNegativeTest(){
        long id = -1L;
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(
                buildUri("/faculties/" + id),
                String.class,
                Map.of("id", id)
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ресурс с id = %d не найден!".formatted(id));
    }
    @Test
    public void findByColorOrNameTest(){
        String name = faculties.get(faker.random().nextInt(faculties.size())).getName();
        List<Faculty> expectedName = faculties.stream().filter(faculty -> faculty.getName().equals(name)).toList();
        colorOrName(name, expectedName);

        String color = faculties.get(faker.random().nextInt(faculties.size())).getColor();
        List<Faculty> expectedColor = faculties.stream().filter(faculty -> faculty.getColor().equals(color)).toList();
        colorOrName(color, expectedColor);
    }

    private void colorOrName(String str, List<Faculty> expected){
        ResponseEntity<List<Faculty>> responseEntity = testRestTemplate.exchange(
                buildUri("/faculties?nameOrColor=" + str),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){},
                Map.of("nameOrColor", str)
        );
        List<Faculty> actual = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }
    @Test
    public void getStudentsByFacultyPositiveTest(){
        long id = faculties.get(faker.random().nextInt(faculties.size())).getId();
        createStudents(faculties);
        List<Student> expectedStudents = studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getFaculty().getId() == id)
                .toList();
        ResponseEntity<List<Student>> responseEntity = testRestTemplate.exchange(
                buildUri("/faculties/" + id + "/students"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){},
                Map.of("id", id)
        );
        List<Student> actual = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedStudents);
    }
    @Test
    public void getStudentsByFacultyNegativeTest(){
        long id = -1L;
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                buildUri("/faculties/" + id + "/students"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){},
                Map.of("id", id)
        );
        String actual = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual).isEqualTo("Ресурс с id = %d не найден!".formatted(id));
    }
}
