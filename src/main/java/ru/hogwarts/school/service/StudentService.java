package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class StudentService {
    private final StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method to create student: {}", student.getName());
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method to find student with id = {}", id);
        return studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Объект " + Student.class.getSimpleName() + " с id " + id + " не найден"));
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method to edit student = {}", student.getName());
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method to delete student with id = {}", id);
        try {
            studentRepository.deleteById(id);
            logger.info("Student with id = {} was successfully deleted", id);
        } catch (Exception e) {
            logger.error("Error occurred while trying to delete student with id = {}", id, e);
            throw e;
        }
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method to get all students");
        return studentRepository.findAll();
    }

    public List<Student> findStudentsByAge(int age) {
        logger.info("Was invoked method to find students by age = {}", age);
        return studentRepository.findByAge(age);
    }

    public List<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method to find students with age between {} and {}", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }


    public long getTotalNumberOfStudents() {
        logger.info("Was invoked method to get the total number of students");
        long totalNumber = studentRepository.countStudents();
        logger.debug("Total number of students is {}", totalNumber);
        return totalNumber;
    }

    public double getAverageAgeOfStudents() {
        logger.info("Was invoked method to get the average age of students");
        double averageAge = studentRepository.averageAge();
        logger.debug("Average age of students is {}", averageAge);
        return averageAge;
    }

    public Collection<Student> getLastFiveStudents() {
        logger.info("Was invoked method to get the last five students");
        return studentRepository.getLastFiveStudents();
    }

    public List<String> getNamesStartingWithA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("А"))
                .map(String::toUpperCase)
                .sorted()
                .toList();
    }

    public double getMiddleAge() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    public synchronized void printSynchronizedHello(String name) {
        System.out.println("Hello " + name);
    }

    public void printStudentsParallel(Collection<Student> studentsCollection) {
        List<Student> students = studentsCollection.stream()
                .limit(6)
                .toList();

        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        }).start();

    }

    public void printStudentsSynchronized(Collection<Student> studentsCollection) {
        List<Student> students = studentsCollection.stream()
                .limit(6)
                .toList();

        printSynchronizedHello(students.get(0).getName());
        printSynchronizedHello(students.get(1).getName());

        new Thread(() -> {
            printSynchronizedHello(students.get(2).getName());
            printSynchronizedHello(students.get(3).getName());
        }).start();

        new Thread(() -> {
            printSynchronizedHello(students.get(4).getName());
            printSynchronizedHello(students.get(5).getName());
        }).start();
    }
}
