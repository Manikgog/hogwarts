package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static ru.hogwarts.school.constants.Constants.*;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerWebMVCTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private FacultyService facultyService;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentRepository studentRepository;
    @Test
    public void createTest() throws Exception {
        when(facultyRepository.save(any())).thenReturn(GRIFFINDOR);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(GRIFFINDOR))
        ) .andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Faculty responseFaculty = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Faculty.class);
            assertThat(responseFaculty).usingRecursiveComparison().isEqualTo(GRIFFINDOR);
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        });
    }
    @Test
    public void updatePositiveTest() throws Exception {
        long id = 1L;
        String newName = "Тестовое имя";
        String newColor = "Тестовый цвет";

        Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName("Слизерин");
        oldFaculty.setColor("зеленый");

        Faculty newFaculty = new Faculty();
        newFaculty.setId(id);
        newFaculty.setName(newName);
        newFaculty.setColor(newColor);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(oldFaculty));
        when(facultyRepository.save(any())).thenReturn(newFaculty);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/faculties/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newFaculty))
        ) .andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Faculty responseFaculty = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Faculty.class);
            assertThat(responseFaculty).usingRecursiveComparison().isEqualTo(newFaculty);
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        });

    }
    @Test
    public void updateNegativeTest() throws Exception {
        long id = -1L;
        String newName = "Тестовое имя";
        String newColor = "Тестовый цвет";

        Faculty newFaculty = new Faculty();
        newFaculty.setId(id);
        newFaculty.setName(newName);
        newFaculty.setColor(newColor);
        String error = new String("Ресурс с id = " + id + " не найден!");
        when(facultyRepository.findById(id)).thenThrow(new NotFoundException(id));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/faculties/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(newFaculty))
        ) .andExpect(result -> content().string(error));
    }
    @Test
    public void getPositiveTest() throws Exception {
        long id = 1L;

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName("Слизерин");
        faculty.setColor("зеленый");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/faculties/{id}", id)
        ) .andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Faculty responseFaculty = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Faculty.class);
            assertThat(responseFaculty).usingRecursiveComparison().isEqualTo(faculty);
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        });

    }
    @Test
    public void getNegativeTest() throws Exception {
        long id = -1L;

        String error = "Ресурс с id = " + id + " не найден!";
        when(facultyRepository.findById(id)).thenThrow(new NotFoundException(id));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/faculties/{id}", id)
        ) .andExpect(result -> content().string(error));
    }
    @Test
    public void deletePositiveTest() throws Exception {
        long id = 1L;

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName("Слизерин");
        faculty.setColor("зеленый");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/faculties/{id}", id)
        ) .andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Faculty responseFaculty = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Faculty.class);
            assertThat(responseFaculty).usingRecursiveComparison().isEqualTo(faculty);
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        });
    }
    @Test
    public void deleteNegativeTest() throws Exception {
        long id = -1L;

        String error = "Ресурс с id = " + id + " не найден!";
        when(facultyRepository.findById(id)).thenThrow(new NotFoundException(id));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/faculties/{id}", id)
        ) .andExpect(result -> content().string(error));
    }
    @Test
    public void findByColorOrNameTest() throws Exception {
        String nameOrColor = "красный";
        Faculty griffindor = new Faculty();
        griffindor.setId(1L);
        griffindor.setName("Гриффиндор");
        griffindor.setColor("красный");
        ArrayList<Faculty> list = new ArrayList<>();
        list.add(griffindor);

         when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(any(String.class), any(String.class))).thenReturn(list);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/faculties?nameOrColor=" + nameOrColor)
        ).andExpect(
                result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ArrayList<Faculty> responseList = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ArrayList.class);
                    assertThat(objectMapper.writeValueAsString(responseList)).isEqualTo(objectMapper.writeValueAsString(list));
                }
        );

        nameOrColor = "Гриффиндор";

        mockMvc.perform(
                MockMvcRequestBuilders.get("/faculties?nameOrColor=" + nameOrColor)
        ).andExpect(
                result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ArrayList<Faculty> responseList = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ArrayList.class);
                    assertThat(objectMapper.writeValueAsString(responseList)).isEqualTo(objectMapper.writeValueAsString(list));
                }
        );
    }

    @Test
    public void getStudentsByFacultyTest() throws Exception {
        long id = 1;
        ArrayList<Student> list = new ArrayList<>();
        list.add(POTTER);
        list.add(GREINDGER);
        list.add(WISLY);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(GRIFFINDOR));
        when(studentRepository.findByFacultyId(any(Long.class))).thenReturn(list);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/faculties/" + id + "/students")
        ).andExpect(
                result -> {
                    MockHttpServletResponse response = result.getResponse();
                    ArrayList<Student> responseList = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ArrayList.class);
                    assertThat(objectMapper.writeValueAsString(responseList)).isEqualTo(objectMapper.writeValueAsString(list));
                }
        );
    }
}
