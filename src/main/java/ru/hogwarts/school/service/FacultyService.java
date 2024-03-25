package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    Logger logger = LoggerFactory.getLogger(AvatarService.class);
    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository){
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }
    public Faculty create(Faculty faculty){
        logger.info("The create(Faculty faculty) method was called");
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }
    public Optional<Faculty> update(long id, Faculty faculty){
        logger.info("The update(long id, Faculty faculty) method was called");
        return facultyRepository.findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setColor(faculty.getColor());
                    oldFaculty.setName(faculty.getName());
                    return facultyRepository.save(oldFaculty);})
                .or(() -> {
                    logger.error("Faculty with id=" + id + " not found.");
                    throw new NotFoundException(id);
                });
    }
    public Optional<Faculty> delete(long id){
        logger.info("The delete(long id) method was called");
        return facultyRepository.findById(id)
                .map(faculty -> {
                    facultyRepository.delete(faculty);
                    return faculty;
                })
                .or(() -> {
                    logger.error("Faculty with id=" + id + " not found.");
                    throw new NotFoundException(id);
                });
    }
    public Optional<Faculty> get(long id){
        logger.info("The get(long id) method was called");
        return facultyRepository.findById(id).or(() -> {
            logger.error("Faculty with id=" + id + " not found.");
            throw new NotFoundException(id);
        });
    }
    public List<Faculty> findByColorOrName(String nameOrColor){
        logger.info("The findByColorOrName(String nameOrColor) method was called");
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(nameOrColor, nameOrColor);
    }
    public List<Student> getStudentsOnFaculty(long faculty_id){
        logger.info("The getStudentsOnFaculty(long faculty_id) method was called");
        return studentRepository.findByFacultyId(get(faculty_id).get().getId());
    }

    public String getLongestName() {
        Integer maxLength = facultyRepository.findAll().stream().map(Faculty::getName).map(String::length).reduce(0, (max, size) -> size > max ? size : max);
        return facultyRepository.findAll().stream().map(Faculty::getName).filter(s -> s.length() == maxLength).limit(1).findAny().get();
    }
}
