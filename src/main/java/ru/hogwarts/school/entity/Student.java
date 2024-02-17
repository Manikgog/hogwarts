package ru.hogwarts.school.entity;

import jakarta.persistence.*;
import java.lang.*;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    public Faculty getFaculty(){
        return faculty;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public void setFaculty(Faculty faculty){
        this.faculty = faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age && Objects.equals(id, student.id) && Objects.equals(name, student.name) && Objects.equals(faculty, student.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, faculty);
    }
}
