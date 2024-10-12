package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
@ContextConfiguration(classes = FacultyController.class)
public class FacultyControllerWebMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void createFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));

        verify(facultyRepository).save(any(Faculty.class));
    }

    @Test
    public void getFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");

        when(facultyRepository.findById(1L)).thenReturn(java.util.Optional.of(faculty));

        mockMvc.perform(get("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));

        verify(facultyRepository).findById(1l);
    }

    @Test
    public void editFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Reddd");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Reddd"));
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        Faculty faculty = new Faculty(1L, "Wampus", "Red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        doNothing().when(facultyRepository).deleteById(1L);

        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());

        verify(facultyRepository).deleteById(1L);
    }

    @Test
    public void findFacultuByColorTest() throws Exception {
        Faculty faculty1 = new Faculty(1L, "Gryffindor", "Red");
        Faculty faculty2 = new Faculty(2L, "Wampus", "Red");

        when(facultyRepository.findByColorIgnoreCase("Red")).thenReturn(Arrays.asList(faculty1, faculty2));
        mockMvc.perform(get("/faculty/filter/color/Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Wampus"))
                .andExpect(jsonPath("$[1].color").value("Red"));

        verify(facultyRepository).findByColorIgnoreCase("Red");
    }

    @Test
    public void testGetStudentsOfFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Ravenclaw", "Blue");
        Student student1 = new Student(1L, "Neville", 15);
        Student student2 = new Student(2L, "Luna", 15);

        faculty.setStudents(Arrays.asList(student1, student2));

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Neville"))
                .andExpect(jsonPath("$[1].name").value("Luna"));

        verify(facultyRepository, times(1)).findById(1L);
    }
}
