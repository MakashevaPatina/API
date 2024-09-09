package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @PostMapping
    public Faculty createStudent(@RequestBody Faculty faculty) {
        return facultyService.createStudent(faculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getStudent(@PathVariable Long id) {
        Faculty student = facultyService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> editStudent(@RequestBody Faculty student) {
        Faculty foundStudent = facultyService.editFaculty(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    public Faculty deliteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping("/filter/color/{color}")
    public List<Faculty> findFacultuByColor(@PathVariable String color) {
        return facultyService.findFacultyByColor(color);
    }
}
