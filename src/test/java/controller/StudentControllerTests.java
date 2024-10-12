package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StudentService studentService;

    @Test
    public void testCreateStudent() {
        Student student = new Student();
        student.setName("Harry Potter");
        student.setAge(17);


        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/student", student, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
        assertThat(response.getBody().getAge()).isEqualTo(17);
    }

    @Test
    public void testGetStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(17);


        when(studentService.findStudent(anyLong())).thenReturn(student);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/1", Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
    }

    @Test
    public void testEditStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Hermione Granger");
        student.setAge(18);


        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/1",
                HttpMethod.PUT,
                new HttpEntity<>(student),
                Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Hermione Granger");
        assertThat(response.getBody().getAge()).isEqualTo(18);
    }

    @Test
    public void testDeleteStudent() {

        Mockito.doNothing().when(studentService).deleteStudent(anyLong());

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/1",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testFindStudentsByAge() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Ron Weasley");
        student.setAge(17);


        when(studentService.findStudentsByAge(anyInt())).thenReturn(Collections.singletonList(student));

        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/filter/age/17", Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Ron Weasley");
    }

    @Test
    public void testFindStudentsByAgeBetween() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Harry Potter");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Hermione Granger");
        student2.setAge(18);


        when(studentService.findStudentsByAgeBetween(anyInt(), anyInt()))
                .thenReturn(Arrays.asList(student1, student2));

        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/filter/age-range?minAge=17&maxAge=18", Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(2);
        assertThat(response.getBody()[0].getName()).isEqualTo("Harry Potter");
        assertThat(response.getBody()[1].getName()).isEqualTo("Hermione Granger");
    }

    @Test
    public void testGetFacultyOfStudent() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");

        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setFaculty(faculty);


        when(studentService.findStudent(anyLong())).thenReturn(student);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/1/faculty", Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
    }
}
