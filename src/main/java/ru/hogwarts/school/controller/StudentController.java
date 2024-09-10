package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter/age/{age}")
    public List<Student> findStudentsByAge(@PathVariable int age) {
        return studentService.findStudentsByAge(age);
    }

    @GetMapping("/filter/age-range")
    public List<Student> findStudentsByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.findStudentsByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{studentId}/faculty")
    public Faculty getFacultyOfStudent(@PathVariable Long studentId) {
        Student student = studentService.findStudent(studentId);
        return student.getFaculty();
    }
}
