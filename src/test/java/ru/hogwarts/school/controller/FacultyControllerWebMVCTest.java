package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
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
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
    public void updatePositiveTest() throws Exception {
        long id = 1;
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
}
