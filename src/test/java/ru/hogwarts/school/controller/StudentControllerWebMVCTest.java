package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.constants.Constants.*;
import static ru.hogwarts.school.constants.Constants.BING;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerWebMVCTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private StudentService studentService;

    @Test
    public void test_create() throws Exception {
        when(facultyRepository.findById(POTTER.getFaculty().getId())).thenReturn(Optional.of(POTTER.getFaculty()));
        when(studentRepository.save(any())).thenReturn(POTTER);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(POTTER))
        ) .andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Student responseStudent = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Student.class);
            assertThat(responseStudent).usingRecursiveComparison().isEqualTo(POTTER);
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        });
    }

    @Test
    public void positive_test_update() throws Exception {
        long id = 1L;
        String newName = "Тестовое имя";
        int newAge = 13;

        Student oldStudent = new Student();
        oldStudent.setId(id);
        oldStudent.setName("Gary Potter");
        oldStudent.setAge(14);
        oldStudent.setFaculty(GRIFFINDOR);

        Student newStudent = new Student();
        newStudent.setId(id);
        newStudent.setName(newName);
        newStudent.setAge(newAge);
        newStudent.setFaculty(GRIFFINDOR);

        when(studentRepository.findById(any())).thenReturn(Optional.of(oldStudent));
        when(facultyRepository.findById(POTTER.getFaculty().getId())).thenReturn(Optional.of(POTTER.getFaculty()));
        when(studentRepository.save(any())).thenReturn(newStudent);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newStudent))
        ) .andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Student responseStudent = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Student.class);
            assertThat(responseStudent).usingRecursiveComparison().isEqualTo(newStudent);
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        });
    }
    @Test
    public void negative_test_update() throws Exception {
        long id = -1L;
        String newName = "Тестовое имя";
        int newAge = 12;

        Student newStudent = new Student();
        newStudent.setId(id);
        newStudent.setName(newName);
        newStudent.setAge(newAge);
        newStudent.setFaculty(GRIFFINDOR);
        String error = new String("Ресурс с id = " + id + " не найден!");
        when(studentRepository.findById(id)).thenThrow(new NotFoundException(id));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newStudent))
        ) .andExpect(result -> content().string(error));
    }

    @Test
    public void positive_test_delete() throws Exception {
        long id = 1L;

        when(studentRepository.findById(any())).thenReturn(Optional.of(POTTER));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/students/{id}", id)
        ) .andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Student responseStudent = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Student.class);
            assertThat(responseStudent).usingRecursiveComparison().isEqualTo(POTTER);
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        });
    }
    @Test
    public void negative_delete_test() throws Exception {
        long id = -1L;

        String error = "Ресурс с id = " + id + " не найден!";
        when(studentRepository.findById(id)).thenThrow(new NotFoundException(id));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/students/{id}", id)
        ) .andExpect(result -> content().string(error));
    }

    @Test
    public void positive_test_get() throws Exception {
        long id = 1L;

        when(studentRepository.findById(any())).thenReturn(Optional.of(POTTER));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/students/{id}", id)
        ) .andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Student responseStudent = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Student.class);
            assertThat(responseStudent).usingRecursiveComparison().isEqualTo(POTTER);
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        });
    }
    @Test
    public void negative_test_get() throws Exception {
        long id = -1L;

        String error = "Ресурс с id = " + id + " не найден!";
        when(studentRepository.findById(id)).thenThrow(new NotFoundException(id));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/students/{id}", id)
        ) .andExpect(result -> content().string(error));
    }

    @Test
    public void test_findByAge() throws Exception {
        int age = 13;
        List<Student> expected = List.of(POTTER, POLUMNA, WISLY, GREINDGER);

        when(studentRepository.findByAge(any(Integer.class))).thenReturn(expected);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/students?age=" + age)
        ).andExpect(
                result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ArrayList<Student> responseList = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ArrayList.class);
                    assertThat(objectMapper.writeValueAsString(responseList)).isEqualTo(objectMapper.writeValueAsString(expected));
                }
        );
    }
    @Test
    public void test_findByAgeBetween() throws Exception {
        int ageMin = 10;
        int ageMax = 15;
        List<Student> expected = List.of(POTTER, POLUMNA, WISLY, GREINDGER);

        when(studentRepository.findByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn(expected);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/students?min=" + ageMin + "&max=" + ageMax)
        ).andExpect(
                result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ArrayList<Student> responseList = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ArrayList.class);
                    assertThat(objectMapper.writeValueAsString(responseList)).isEqualTo(objectMapper.writeValueAsString(expected));
                }
        );
    }
    @Test
    public void positive_test_getFacultyById() throws Exception {
        int id = 1;
        when(studentRepository.findById(any())).thenReturn(Optional.of(POTTER));
        mockMvc.perform(MockMvcRequestBuilders.get("/students/" + id + "/faculty"))
                .andExpect(
                        result -> {
                            MockHttpServletResponse response = result.getResponse();
                            Faculty responseFaculty = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Faculty.class);
                            assertThat(objectMapper.writeValueAsString(responseFaculty)).isEqualTo(objectMapper.writeValueAsString(POTTER.getFaculty()));
                        }
                );
    }
    @Test
    public void negative_test_getFacultyById() throws Exception {
        Long id = -1L;

        String error = "Ресурс с id = " + id + " не найден!";
        when(studentRepository.findById(any())).thenThrow(new NotFoundException(id));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/students/" + id + "/faculty")
        ) .andExpect(result -> content().string(error));
    }

}