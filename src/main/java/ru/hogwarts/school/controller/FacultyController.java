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
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty student = facultyService.findFaculty(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty student) {
        Faculty foundStudent = facultyService.editFaculty(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter/color/{color}")
    public List<Faculty> findFacultyByColor(@PathVariable String color) {
        return facultyService.findFacultyByColor(color);
    }

    @GetMapping("/filter/search")
    public List<Faculty> findFacultyByColorOrName(@RequestParam String query) {
        return facultyService.findFacultiesByNameOrColor(query, query);
    }

    @GetMapping("/{facultyId}/students")
    public List<Student> getStudentsOfFaculty(@PathVariable Long facultyId) {
        Faculty faculty = facultyService.findFaculty(facultyId);
        return faculty.getStudents();
    }

    @GetMapping("/longest-Faculty-Name")
    public String longestFacultyName() {
        return facultyService.longestFacultyName();
    }

    @GetMapping("/original-Sum")
    public Long originalSum() {
        return facultyService.originalSum();
    }

    @GetMapping("/optimized-Sum-One")
    public Long optimizedSumOne() {
        return facultyService.optimizedSumOne();
    }

    @GetMapping("/optimized-Sum-Two")
    public Long optimizedSumTwo() {
        return facultyService.optimizedSumTwo();
    }
}
