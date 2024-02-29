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
class StudentControllerTestRestTemplate {
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
	public void test_create_student() {
			Student student = BING;
			Assertions.assertThat(this
							.restTemplate
							.postForObject("http://localhost:" + port + "/students", student, Student.class))
					.matches(s -> {
                        return s.getAge() == student.getAge() &&
                                s.getName().equals(student.getName()) &&
                                s.getFaculty().getName().equals(student.getFaculty().getName()) &&
                                s.getFaculty().getColor().equals(student.getFaculty().getColor());

                    }, "данные студентов равны");

			Student st = studentRepository.findAll().stream().filter(s ->
					{
                        return s.getAge() == student.getAge() &&
                                s.getName().equals(student.getName()) &&
                                s.getFaculty().equals(student.getFaculty());
					}
			).findFirst().get();


			this.restTemplate
					.delete("http://localhost:" + port + "/students/" + st.getId(), Student.class);

	}

	@Test
	public void test_delete_student(){
		Student student = BING;
		studentRepository.save(student);

		Student st = studentRepository.findAll().stream().filter(s ->
				{
                    return s.getAge() == student.getAge() &&
                            s.getName().equals(student.getName()) &&
                            s.getFaculty().equals(student.getFaculty());
                }
		).findFirst().get();

		this.restTemplate
				.delete("http://localhost:" + port + "/students/" + st.getId(), Student.class);

		org.junit.jupiter.api.Assertions.assertEquals(Optional.empty(), studentRepository.findById(st.getId()));
	}

	@Test
	public void test_update_student(){
		Student student = BING;
		studentRepository.save(student);

		Student st = studentRepository.findAll().stream().filter(s ->
				{
                    return s.getAge() == student.getAge() &&
                            s.getName().equals(student.getName()) &&
                            s.getFaculty().equals(student.getFaculty());
                }
		).findFirst().get();

		student.setId(st.getId());

		this.restTemplate
						.put("http://localhost:" + port + "/students/" + student.getId(), student);

		org.junit.jupiter.api.Assertions.assertEquals(student.getAge(), studentRepository.findById(student.getId()).get().getAge());
		org.junit.jupiter.api.Assertions.assertEquals(student.getName(), studentRepository.findById(student.getId()).get().getName());
		org.junit.jupiter.api.Assertions.assertEquals(student.getId(), studentRepository.findById(student.getId()).get().getId());
		org.junit.jupiter.api.Assertions.assertEquals(student.getFaculty().getId(), studentRepository.findById(student.getId()).get().getFaculty().getId());
		org.junit.jupiter.api.Assertions.assertEquals(student.getFaculty().getColor(), studentRepository.findById(student.getId()).get().getFaculty().getColor());
		org.junit.jupiter.api.Assertions.assertEquals(student.getFaculty().getName(), studentRepository.findById(student.getId()).get().getFaculty().getName());

		studentRepository.delete(student);
	}

	@Test
	public void test_get_student(){
		Student student = BING;
		studentRepository.save(student);

		Student st = studentRepository.findAll().stream().filter(s ->
				{
                    return s.getAge() == student.getAge() &&
                            s.getName().equals(student.getName()) &&
                            s.getFaculty().equals(student.getFaculty());
                }
		).findFirst().get();

		student.setId(st.getId());

		Assertions.assertThat(this
						.restTemplate
						.getForObject("http://localhost:" + port + "/students/" + student.getId(), Student.class))
				.matches(s -> {
                    return s.getId().equals(student.getId()) &&
                            s.getName().equals(student.getName()) &&
                            s.getAge() == student.getAge() &&
                            s.getFaculty().getId().equals(student.getFaculty().getId()) &&
                            s.getFaculty().getName().equals(student.getFaculty().getName()) &&
                            s.getFaculty().getColor().equals(student.getFaculty().getColor());
                }, "данные студентов равны");

		studentRepository.delete(student);
	}

	@Test
	public void test_findByAge(){
		studentRepository.save(BING);
		studentRepository.save(JOE);
		studentRepository.save(RACHEL);
		studentRepository.save(ROSS);
		studentRepository.save(MONICA);
		studentRepository.save(FUIBY);

		List<Student> list = new ArrayList<>();
		list.add(BING);
		list.add(ROSS);

		Assertions.assertThat(this
				.restTemplate
                .getForEntity("http://localhost:" + port + "/students?age=25", List.class))
                .matches(e -> {
                    var actualList = Objects.requireNonNull(e
                                    .getBody())
                            .stream()
                            .toList();
                    return actualList.size() == list.size();
                });

		studentRepository.delete(BING);
		studentRepository.delete(JOE);
		studentRepository.delete(RACHEL);
		studentRepository.delete(ROSS);
		studentRepository.delete(MONICA);
		studentRepository.delete(FUIBY);
	}

	@Test
	public void test_findByAgeBetween(){
		studentRepository.save(BING);
		studentRepository.save(JOE);
		studentRepository.save(RACHEL);
		studentRepository.save(ROSS);
		studentRepository.save(MONICA);
		studentRepository.save(FUIBY);

		List<Student> list = new ArrayList<>();
		list.add(BING);
		list.add(ROSS);
		list.add(RACHEL);

		Assertions.assertThat(this
				.restTemplate
				.getForEntity("http://localhost:" + port + "/students?min=25&max=27", List.class))
                .matches(e -> {
                    var actualList = Objects.requireNonNull(e
                                    .getBody())
                            .stream()
                            .toList();
                    return actualList.size() == list.size();
                });


		studentRepository.delete(BING);
		studentRepository.delete(JOE);
		studentRepository.delete(RACHEL);
		studentRepository.delete(ROSS);
		studentRepository.delete(MONICA);
		studentRepository.delete(FUIBY);
	}

	@Test
	public void test_getFacultyById(){
        Student testStudent = BING;
		studentRepository.save(testStudent);

		Student st = studentRepository.findAll().stream().filter(s -> {
                    return s.getAge() == testStudent.getAge() &&
                            s.getName().equals(testStudent.getName()) &&
                            s.getFaculty().equals(testStudent.getFaculty());
                }
		).findFirst().get();

		testStudent.setId(st.getId());

		Assertions.assertThat(this
				.restTemplate
				.getForEntity("http://localhost:" + port + "/students/" + testStudent.getId() + "/faculty", Faculty.class))
                .matches(s -> {
                    return Objects.requireNonNull(s.getBody()).getColor().equals(BING.getFaculty().getColor()) &&
                            s.getBody().getName().equals(BING.getFaculty().getName()) &&
                            Objects.equals(s.getBody().getId(), BING.getFaculty().getId());
                });

		studentRepository.delete(testStudent);
	}

    /**
     * Метод для очистки базы данных после тестирования
     */
	@AfterEach
	public void clearDB(){
		List<Student> list = studentRepository.findAll();
		List<Student> studentsToDelete = List.of(
			BING, JOE, ROSS, RACHEL, FUIBY, MONICA
		);
		List<Long> ids = new ArrayList<>();
		for (Student student : studentsToDelete) {  // получение идентификаторов студентов для их удаления из БД
			list.stream().filter(s -> {
                return s.getAge() == student.getAge() && s.getName().equals(student.getName());
			}).mapToLong(Student::getId).forEach(ids::add);
		}

		for (Long id : ids){    // удаление по идентификатору
			studentRepository.deleteById(id);
		}
	}
}
