package ru.hogwarts.school.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
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
public class StudentControllerTestRestTemplateTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
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

    @Test
    public void createStudentWithoutFacultyPositive() throws JsonProcessingException {
        Student student = new Student();
        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());

        createStudent(student);
    }
    @Test
    public void createStudentWithFacultyPositive() throws JsonProcessingException {
        Student student = new Student();
        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());
        Faculty randomFacultyFromDb = facultyRepository
                .findAll(PageRequest.of(faker.random().nextInt(0,1)/*случайный номер страницы от 0 до 1, т.к. всего 2 факультета создано в базе данных*/, 1))
                .getContent()
                .get(0);

        student.setFaculty(randomFacultyFromDb);

        createStudent(student);
    }

    private void createStudent(Student student){
        ResponseEntity<Student> responseEntity = testRestTemplate.postForEntity(
                buildUri("/students"),
                student,
                Student.class
        );
        Student created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).isNotNull();
        assertThat(created).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
        assertThat(created.getId()).isNotNull();

        Optional<Student> fromDb = studentRepository.findById(created.getId());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(created);
    }
    @Test
    public void createStudentWithFacultyWhichNotExist(){
        Student student = new Student();
        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());
        Faculty faculty = new Faculty();
        faculty.setId(-1L);
        student.setFaculty(faculty);
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(
                buildUri("/students"),
                student,
                String.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ресурс с id = %d не найден!".formatted(student.getFaculty().getId()));
    }
    @Test
    public void findByAgeBetweenTest(){
        int min = faker.random().nextInt(11, 18);
        int max = faker.random().nextInt(min, 18);
        List<Student> expected = students.stream()
                .filter(student -> student.getAge() >= min && student.getAge() <= max)
                .toList();

        ResponseEntity<List<Student>> responseEntity = testRestTemplate.exchange(
                buildUri("/students" + "?min=" + min + "&max=" + max),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){},
                Map.of("min", min,"max", max)
        );
        List<Student> actual = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }
    @Test
    public void getFacultyByIdPositiveTest(){

        Student student = students.get(faker.random().nextInt(students.size()));
        long id = student.getId();
        ResponseEntity<Faculty> responseEntity = testRestTemplate.getForEntity(
                buildUri("/students/" + id + "/faculty"),
                Faculty.class,
                Map.of("id", student.getId())
        );
        Faculty actual = responseEntity.getBody();
        Faculty expected = student.getFaculty();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
    @Test
    public void getFacultyByIdNegativeTest(){
        long id = -1;
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(
                buildUri("/students/" + id + "/faculty"),
                String.class,
                Map.of("id", id)
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ресурс с id = %d не найден!".formatted(id));
    }

    @Test
    public void findByAgeTest(){
        int age = students.get(faker.random().nextInt(students.size())).getAge();
        List<Student> expected = students.stream()
                .filter(student -> student.getAge() == age)
                .toList();

        ResponseEntity<List<Student>> responseEntity = testRestTemplate.exchange(
                buildUri("/students?age=" + age),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){},
                Map.of("age", age)
        );
        List<Student> actual = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }
    @Test
    public void updatePositiveTest(){
        Student student = students.get(faker.random().nextInt(students.size()));

        Student expected = new Student();
        expected.setId(student.getId());
        expected.setName("test");
        expected.setAge(100);
        expected.setFaculty(student.getFaculty());

        ResponseEntity<Student> responseEntity = testRestTemplate.exchange(
                buildUri("/students/" + student.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(expected),
                new ParameterizedTypeReference<>(){},
                Map.of("id", expected.getId())
        );
        Student created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).usingRecursiveComparison().isEqualTo(expected);
    }
    @Test
    public void updateNegativeFakeFacultyTest(){
        Student student = students.get(faker.random().nextInt(students.size()));
        Faculty fakeFaculty = new Faculty();
        long fakeId = -1L;
        fakeFaculty.setId(fakeId);
        Student expected = new Student();
        expected.setId(student.getId());
        expected.setName("test");
        expected.setAge(100);
        expected.setFaculty(fakeFaculty);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                buildUri("/students/" + student.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(expected),
                new ParameterizedTypeReference<>(){},
                Map.of("id", expected.getId())
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ресурс с id = %d не найден!".formatted(fakeId));
    }

    /**
     * метод для проверки правильности реакции программы на ввод неправильного идентификатора студента, при
     * правильном идентификаторе факультета
     */
    @Test
    public void updateNegativeFakeStudentIdTest(){
        long fakeStudentId = -1L;
        Faculty faculty = new Faculty();
        long id = students.get(faker.random().nextInt(students.size())).getFaculty().getId();
        faculty.setId(id);
        Student student = new Student();
        student.setId(fakeStudentId);
        student.setName("test");
        student.setAge(0);
        student.setFaculty(faculty);


        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                buildUri("/students/" + fakeStudentId),
                HttpMethod.PUT,
                new HttpEntity<>(student),
                new ParameterizedTypeReference<>(){},
                Map.of("id", fakeStudentId)
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ресурс с id = %d не найден!".formatted(fakeStudentId));
    }
    @Test
    public void deletePositiveTest(){
        Student studentExpected = students.get(faker.random().nextInt(students.size()));

        ResponseEntity<Student> responseEntity = testRestTemplate.exchange(
                buildUri("/students/" + studentExpected.getId()),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>(){},
                Map.of("id", studentExpected.getId())
        );
        Student created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).usingRecursiveComparison().isEqualTo(studentExpected);

        assertThat(studentRepository.findById(studentExpected.getId())).isEmpty();
    }
    @Test
    public void deleteNegativeTest(){
        long fakeId = -1L;
        Student fakeStudent = new Student();
        fakeStudent.setId(fakeId);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                buildUri("/students/" + fakeId),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<>(){},
                Map.of("id", fakeId)
        );
        String created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ресурс с id = %d не найден!".formatted(fakeId));
        assertThat(studentRepository.findById(fakeId)).isEmpty();
    }
    @Test
    public void getPositiveTest(){
        Student studentExpected = students.get(faker.random().nextInt(students.size()));

        ResponseEntity<Student> responseEntity = testRestTemplate.getForEntity(
                buildUri("/students/" + studentExpected.getId()),
                Student.class,
                studentExpected
        );
        Student studentFromResponse = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentFromResponse).usingRecursiveComparison().isEqualTo(studentExpected);

        assertThat(studentRepository.findById(studentExpected.getId())).usingRecursiveComparison().isEqualTo(Optional.of(studentFromResponse));
    }
    @Test
    public void getNegativeTest(){
        long fakeId = -1L;

        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(
                buildUri("/students/" + fakeId),
                String.class,
                new Student()
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Ресурс с id = %d не найден!".formatted(fakeId));
    }
}
