package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Объект " + Faculty.class.getSimpleName() + " с id " + id + " не найден"));
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllStudents() {
        return facultyRepository.findAll();
    }

    public List<Faculty> findFacultyByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public List<Faculty> findFacultiesByNameOrColor(String color, String name) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public String longestFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }

    //Ответ в принципе выводит неверный, изменение на long не помогло
    public static long originalSum() {
        long startTime = System.nanoTime();
        long sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        long endTime = System.nanoTime();
        logger.info("Finished method: originalSum | Time taken: {} ns", (endTime - startTime));

        return sum;
    }


    public static long optimizedSumOne() {
        long startTime = System.nanoTime();
        long n = 1_000_000;
        long sumOne = (n * (n + 1)) / 2;
        long endTime = System.nanoTime();
        logger.info("Finished method: optimizedSumOne  | Time taken: {} ns", (endTime - startTime));
        return sumOne;
    }

    public static long optimizedSumTwo() {
        long startTime = System.nanoTime();
        long sum = LongStream.rangeClosed(1, 1000000)
                .parallel()
                .sum();
        long endTime = System.nanoTime();
        logger.info("Finished method: optimizedSumTwo | Time taken: {} ns", (endTime - startTime));

        return sum;
    }
}
