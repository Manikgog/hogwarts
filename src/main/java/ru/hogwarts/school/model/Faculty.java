package ru.hogwarts.school.model;

import jakarta.persistence.*;
import java.lang.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Faculty {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String color;
    @OneToMany(mappedBy = "faculty")
    private Collection<Student> students;
    public Faculty() {
    }
    public Faculty(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
//    public Collection<Student> getStudents(){ // как только снимаю комментарий так сразу приложение перестаёт
//        return students;                      // выдавать корректные ответы на запросы на получение студентов и факультетов
//    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public void setColor(java.lang.String color) {
        this.color = color;
    }
    public void setStudents(List<Student> students){
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) && Objects.equals(color, faculty.color) && Objects.equals(students, faculty.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, students);
    }
}

