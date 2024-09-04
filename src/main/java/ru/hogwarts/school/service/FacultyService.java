package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FacultyService {

    private final Map<Long, Faculty> faculties = new HashMap<>();

    private long lastId = 0;


    public Faculty createStudent(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(lastId, faculty);
        return faculty;
    }

    public Faculty findStudent(long id) {
        return faculties.get(id);
    }

    public Faculty editBook(Faculty faculty) {
        if (faculties.containsKey(faculty.getId())) {
            faculty.setId(++lastId);
            faculties.put(lastId, faculty);
            return faculty;
        }
        return null;
    }

    public Faculty deleteBook(long id) {
        return faculties.remove(id);
    }


    public Collection<Faculty> getAllStudents() {
        return faculties.values();
    }
}
