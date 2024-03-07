package ru.hogwarts.school;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.constants.Constants.*;

@WebMvcTest
public class StudentControllerWebMVCTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private AvatarRepository avatarRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private FacultyService facultyService;
    @SpyBean
    private StudentService studentService;
    @SpyBean
    private AvatarService avatarService;

    @Test
    public void test_create() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", BING.getFaculty().getId());
        facultyObject.put("name", BING.getFaculty().getName());
        facultyObject.put("color", BING.getFaculty().getColor());

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", BING.getId());
        studentObject.put("name", BING.getName());
        studentObject.put("age", BING.getAge());
        studentObject.put("faculty", facultyObject);

        when(studentRepository.save(any(Student.class))).thenReturn(BING);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BING.getId()))
                .andExpect(jsonPath("$.name").value(BING.getName()))
                .andExpect(jsonPath("$.age").value(BING.getAge()))
                .andExpect(jsonPath("$.faculty").value(BING.getFaculty()));
    }

    @Test
    public void test_update() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", BING.getFaculty().getId());
        facultyObject.put("name", BING.getFaculty().getName());
        facultyObject.put("color", BING.getFaculty().getColor());

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", BING.getId());
        studentObject.put("name", BING.getName());
        studentObject.put("age", BING.getAge());
        studentObject.put("faculty", facultyObject);

        JSONObject faculty1Object = new JSONObject();
        faculty1Object.put("id", JOE.getFaculty().getId());
        faculty1Object.put("name", JOE.getFaculty().getName());
        faculty1Object.put("color", JOE.getFaculty().getColor());

        JSONObject student1Object = new JSONObject();
        student1Object.put("id", JOE.getId());
        student1Object.put("name", JOE.getName());
        student1Object.put("age", JOE.getAge());
        student1Object.put("faculty", faculty1Object);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(BING));
        when(studentRepository.save(any(Student.class))).thenReturn(JOE);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students/" + BING.getId())
                        .content(student1Object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(JOE.getId()))
                .andExpect(jsonPath("$.name").value(JOE.getName()))
                .andExpect(jsonPath("$.age").value(JOE.getAge()))
                .andExpect(jsonPath("$.faculty").value(JOE.getFaculty()));
    }

    @Test
    public void test_delete() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", BING.getFaculty().getId());
        facultyObject.put("name", BING.getFaculty().getName());
        facultyObject.put("color", BING.getFaculty().getColor());

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", BING.getId());
        studentObject.put("name", BING.getName());
        studentObject.put("age", BING.getAge());
        studentObject.put("faculty", facultyObject);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(BING));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/" + BING.getId())
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BING.getId()))
                .andExpect(jsonPath("$.name").value(BING.getName()))
                .andExpect(jsonPath("$.age").value(BING.getAge()))
                .andExpect(jsonPath("$.faculty").value(BING.getFaculty()));
    }

    @Test
    public void test_get() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", BING.getFaculty().getId());
        facultyObject.put("name", BING.getFaculty().getName());
        facultyObject.put("color", BING.getFaculty().getColor());

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", BING.getId());
        studentObject.put("name", BING.getName());
        studentObject.put("age", BING.getAge());
        studentObject.put("faculty", facultyObject);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(BING));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/" + BING.getId())
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BING.getId()))
                .andExpect(jsonPath("$.name").value(BING.getName()))
                .andExpect(jsonPath("$.age").value(BING.getAge()))
                .andExpect(jsonPath("$.faculty").value(BING.getFaculty()));
    }

    @Test
    public void test_findByAge() throws Exception {
        List<Student> listStudent = new ArrayList<>();
        listStudent.add(MONICA);

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", MONICA.getFaculty().getId());
        facultyObject.put("name", MONICA.getFaculty().getName());
        facultyObject.put("color", MONICA.getFaculty().getColor());

        JSONObject monica = new JSONObject();
        monica.put("id", 0);
        monica.put("name", MONICA.getName());
        monica.put("age", MONICA.getAge());
        monica.put("faculty", facultyObject);

        JSONArray arrayStudents = new JSONArray();
        arrayStudents.put(monica);
        when(studentRepository.findByAge(any(Integer.class))).thenReturn(listStudent);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students?age=" + MONICA.getAge())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":0,\"name\":\"Monica Bing\",\"age\":24,\"faculty\":{\"id\":4,\"name\":\"Ð\u009FÑ\u0083Ñ\u0084Ñ\u0084ÐµÐ½Ð´Ñ\u0083Ð¹\",\"color\":\"Ð¶ÐµÐ»Ñ\u0082Ñ\u008BÐ¹\"}}]"));

    }

    @Test
    public void test_getFacultyById() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", BING.getFaculty().getId());
        facultyObject.put("name", BING.getFaculty().getName());
        facultyObject.put("color", BING.getFaculty().getColor());

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", BING.getId());
        studentObject.put("name", BING.getName());
        studentObject.put("age", BING.getAge());
        studentObject.put("faculty", facultyObject);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(BING));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/" + BING.getId() + "/faculty")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"name\":\"Ð\u0093Ñ\u0080Ð¸Ñ\u0084Ñ\u0084Ð¸Ð½Ð´Ð¾Ñ\u0080\",\"color\":\"ÐºÑ\u0080Ð°Ñ\u0081Ð½Ñ\u008BÐ¹\"}"));
    }

}