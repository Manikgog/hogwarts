package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AvatarControllerTestRestTemplateTest {
    @LocalServerPort
    private int port;
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;
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
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        Faculty faculty1 = createFaculty();
        Faculty faculty2 = createFaculty();

        createStudents(faculty1, faculty2);
        createAvatars();
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
    private void createAvatars() throws IOException {
        Avatar avatar = new Avatar();
        FileInputStream fis = new FileInputStream("src/test/picture_source/1.jpg");
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] bytesArray = bis.readAllBytes();
        avatar.setData(bytesArray);
        avatar.setMediaType(MediaType.MULTIPART_FORM_DATA_VALUE);
        Student student = students.get(0);
        avatar.setFilePath("src/test/picture_destination/1.jpg");
        avatar.setStudent(student);
        avatar.setFileSize(bytesArray.length);
        avatar.setId(1L);
        avatarRepository.save(avatar);

    }
    private String buildUri(String uriStartsWithSlash){
        return "http://localhost:%d%s".formatted(port, uriStartsWithSlash);
    }
    @Test
    public void uploadAvatar_positive_test(){
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

        parameters.add("avatar", new FileSystemResource(Path.of("src/test/picture_source/1.jpg")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(parameters, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(buildUri("/avatars/1/student"), HttpMethod.POST, entity, String.class, "");

        // Expect Ok
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    public void uploadAvatar_negative_test(){

    }
    @Test
    public void getPreview_positive_test(){
        Avatar avatar = avatarRepository.findById(1L).get();            // получение объекта класса Avatar из базы данных
        Long studentId = avatar.getStudent().getId();                           // получение идентификатора студента из объекта avatar
        ResponseEntity<byte[]> responseEntity = testRestTemplate.getForEntity(  // отправка запроса и получение ответа от AvatarController
                buildUri("/avatars/" + studentId + "/avatar/preview"),
                byte[].class,
                Map.of("id", studentId)
        );
        byte[] byteArrayFromResponse = responseEntity.getBody();                // получение тела запроса
        assertThat(byteArrayFromResponse).isNotNull();                          // подтверждение, что тело запроса не равно null
        assertThat(byteArrayFromResponse).isEqualTo(avatar.getData());          // сравнение байтовых массивов - изображений ожидаемого и полученного
    }
    @Test
    public void getPreview_negative_test(){

    }
    @Test
    public void getAvatar_positive_test(){

    }
    @Test
    public void getAvatar_negative_test(){

    }
    @Test
    public void getAvatars_positive_test(){

    }
    @Test
    public void getAvatars_negative_test(){
        
    }
}
