package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();

    private long lastId = 0;


    public Student createStudent(Student student) {
        student.setId(++lastId);
        students.put(lastId, student);
        return student;
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            student.setId(++lastId);
            students.put(lastId, student);
            return student;
        }
        return null;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }


    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public List<Student> findStudentsByAge(int age) {
        return students.values().stream().filter(student -> student.getAge() == age).collect(toList());
    }
}
