package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    Logger logger = LoggerFactory.getLogger(AvatarService.class);
    public StudentService(StudentRepository studentRepository,
                            FacultyRepository facultyRepository){
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }
    public Student create(Student student){
        logger.info("The create(Student student) method is called.");
        student.setId(null);
        fillFaculty(student.getFaculty(), student);
        return studentRepository.save(student);
    }
    public Optional<Student> update(long id, Student student){
        logger.info("The update(long id, Student student) method is called.");
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(student.getName());
                    oldStudent.setAge(student.getAge());
                    fillFaculty(student.getFaculty(), student);
                    oldStudent.setFaculty(student.getFaculty());
                    return studentRepository.save(oldStudent);
                })
                .or(() -> {
            logger.error("Student with id=" + id + " not found.");
            throw new NotFoundException(id);
        });
    }

    public Optional<Student> delete(long id){
        logger.info("The delete(long id) method is called.");
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    studentRepository.delete(oldStudent);
                    return oldStudent;
                })
                .or(() -> {
                    logger.error("Student with id=" + id + " not found.");
                    throw new NotFoundException(id);
                });

    }
    public Optional<Student> get(long id){
        logger.info("The get(long id) method is called.");
        return studentRepository.findById(id).or(() -> {
            logger.error("Student with id=" + id + " not found.");
            throw new NotFoundException(id);
        });
    }
    public List<Student> findByAge(int age){
        logger.info("The findByAge(int age) method is called.");
        return studentRepository.findByAge(age);
    }
    public List<Student> findByAgeBetween(int min, int max){
        logger.info("The findByAgeBetween(int min, int max) method is called.");
        return studentRepository.findByAgeBetween(min, max);
    }
    public Faculty getFacultyByStudentId(long student_id){
        logger.info("The getFacultyByStudentId(long student_id) method is called.");
        return get(student_id).get().getFaculty();
    }
    public void fillFaculty(Faculty faculty, Student student){
        logger.info("The fillFaculty(Faculty faculty, Student student) method is called.");
        if(faculty != null && faculty.getId() != null){
            Faculty facultyFromDb = facultyRepository.findById(faculty.getId())
                    .or(() -> {
                        logger.error("Faculty with id=" + faculty.getId() + " not found.");
                        throw new NotFoundException(faculty.getId());
                    }).get();
            student.setFaculty(facultyFromDb);
        }
    }
    public int getStudentsAmount(){
        logger.info("The getStudentsAmount() method is called.");
        return studentRepository.getStudentsAmount();
    }

    public List<Student> get5LastStudents() {
        logger.info("The get5LastStudents() method is called.");
        return studentRepository.get5LastStudents();
    }

    public List<Student> getLastNStudents(int count) {
        logger.info("The getLastNStudents(int count) method is called.");
        return studentRepository.getLastNStudents(count);
    }
    public List<String> getNameIsStartsWithA(Character letter) {
        if(letter == null){
            letter = 'A';
        }
        Character finalLetter = letter;
        return studentRepository
                .findAll()
                .stream()
                .map(Student::getName)
                .filter(name -> name.toUpperCase().charAt(0) == finalLetter)
                .sorted()
                .toList();
    }

    public float getAverageAge(){
        logger.info("The getAverageAge() method is called.");
        return (float)studentRepository
                .findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElseThrow(RuntimeException::new);
    }

    public String getLongestName() {
        Integer maxLength = studentRepository.findAll().stream().map(Student::getName).map(String::length).reduce(0, (max, size) -> size > max ? size : max);
        return studentRepository.findAll().stream().map(Student::getName).filter(s -> s.length() == maxLength).limit(1).findAny().get();
    }

    public long getCalculating() {
        return LongStream.rangeClosed(1, 1000000).sum();
    }

    public void printStudentsInParallel() {
        List<Student> listOfStudents = studentRepository.findAll().stream().limit(6).toList();
        System.out.println("Основной поток: " + listOfStudents.get(0).getName());
        System.out.println("Основной поток: " + listOfStudents.get(1).getName());
        new Thread(() -> {
            System.out.println("Первый параллельный поток: " + listOfStudents.get(2).getName());
            System.out.println("Первый параллельный поток: " + listOfStudents.get(3).getName());
        }).start();
        new Thread(() -> {
            System.out.println("Второй параллельный поток: " + listOfStudents.get(4).getName());
            System.out.println("Второй параллельный поток: " + listOfStudents.get(5).getName());
        }).start();
    }

    public void printStudentsInParallelSynchronized() {
        List<Student> listOfStudents = studentRepository.findAll().stream().limit(6).toList();
        printStudents("Основной поток: ", List.of(listOfStudents.get(0), listOfStudents.get(1)));
        new Thread(() -> {
            printStudents("Первый поток: ", List.of(listOfStudents.get(2), listOfStudents.get(3)));
        }).start();
        new Thread(() -> {
            printStudents("Второй поток: ", List.of(listOfStudents.get(4), listOfStudents.get(5)));
        }).start();
    }

    private synchronized void printStudents(String threadNumber, List<Student> list){
        list.forEach((s) -> {
            System.out.println(threadNumber + s.getName());
        });
    }
}
