package ru.hogwarts.school;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.constants.Constants.*;

@WebMvcTest
public class FacultyControllerWebMVCTest {
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
    @InjectMocks
    private StudentController studentController;
    @InjectMocks
    private AvatarController avatarController;
    @InjectMocks
    private FacultyController facultyController;
    @Test
    public void test_create() throws Exception {
        Faculty faculty = FACULTY_1;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", faculty.getId());
        facultyObject.put("name", faculty.getName());
        facultyObject.put("color", faculty.getColor());

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculties")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }
    @Test
    public void test_update() throws Exception {
        Faculty faculty = FACULTY_1;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", faculty.getId());
        facultyObject.put("name", faculty.getName());
        facultyObject.put("color", faculty.getColor());

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculties/" + faculty.getId())
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }
    @Test
    public void test_delete() throws Exception {
        Faculty faculty = FACULTY_1;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", faculty.getId());
        facultyObject.put("name", faculty.getName());
        facultyObject.put("color", faculty.getColor());

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculties/" + faculty.getId())
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }
    @Test
    public void test_get() throws Exception {
        Faculty faculty = FACULTY_1;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", faculty.getId());
        facultyObject.put("name", faculty.getName());
        facultyObject.put("color", faculty.getColor());

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/" + faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }
    @Test
    public void test_findByColorOrName() throws Exception {
        Faculty faculty1 = FACULTY_1;

        ArrayList<Faculty> facultyList = new ArrayList<>();
        facultyList.add(faculty1);

        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(any(String.class), any(String.class))).thenReturn(facultyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties?nameOrColor=" + faculty1.getColor())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":0,\"name\":\"First faculty\",\"color\":\"black\"}]"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties?nameOrColor=" + faculty1.getName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":0,\"name\":\"First faculty\",\"color\":\"black\"}]"));
    }
    @Test
    public void test_getStudentsOnFaculty() throws Exception {
        Faculty faculty1 = FACULTY_1;
        BING.setFaculty(FACULTY_1);
        JOE.setFaculty(FACULTY_1);
        ArrayList<Student> studentsList = new ArrayList<>();
        studentsList.add(BING);
        studentsList.add(JOE);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty1));
        when(studentRepository.findByFacultyId(any(Long.class))).thenReturn(studentsList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/" + faculty1.getId() + "/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":0,\"name\":\"Chendler Bing\",\"age\":25,\"faculty\":{\"id\":0,\"name\":\"First faculty\",\"color\":\"black\"}},{\"id\":0,\"name\":\"Joe Tribiany\",\"age\":24,\"faculty\":{\"id\":0,\"name\":\"First faculty\",\"color\":\"black\"}}]"));
    }
}
