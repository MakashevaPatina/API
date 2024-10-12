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
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StudentController.class)
@ContextConfiguration(classes = StudentController.class)
public class StudentControllerWebMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createStudentTest() throws Exception {
        Student student = new Student(1L, "Harry", 11);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Harry"))
                .andExpect(jsonPath("$.age").value(11));

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    public void getStudentTest() throws Exception {
        Student student = new Student(1L, "John", 20);
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(20));

        verify(studentRepository).findById(1L);
    }

    @Test
    public void editStudentTest() throws Exception {
        Student student = new Student(1L, "Ron", 12);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ron"))
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Student student = new Student(1L, "Hermione", 13);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).deleteById(1L);

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());

        verify(studentRepository).deleteById(1L);
    }

    @Test
    public void findStudentsByAgeTest() throws Exception {
        Student student1 = new Student(1L, "Neville", 15);
        Student student2 = new Student(2L, "Luna", 15);

        when(studentRepository.findByAge(15)).thenReturn(Arrays.asList(student1, student2));
        mockMvc.perform(get("/student/filter/age/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Neville"))
                .andExpect(jsonPath("$[0].age").value(15))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Luna"))
                .andExpect(jsonPath("$[1].age").value(15));

        verify(studentRepository).findByAge(15);
    }

    @Test
    public void findStudentsByAgeBetweenTest() throws Exception {
        Student student1 = new Student(1L, "Neville", 15);
        Student student2 = new Student(2L, "Luna", 12);

        when(studentRepository.findByAgeBetween(11, 16)).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/student/filter/age-range")
                        .param("minAge", "11")
                        .param("maxAge", "16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Neville"))
                .andExpect(jsonPath("$[0].age").value(15))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Luna"))
                .andExpect(jsonPath("$[1].age").value(12));

        verify(studentRepository).findByAgeBetween(11, 16);
    }

    @Test
    public void testGetFacultyOfStudent() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        Student student = new Student(1L, "Harry", 11);
        student.setFaculty(faculty);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));

        verify(studentRepository).findById(1L);
    }
}
