package com.example.finalinternship.repository;

import com.example.finalinternship.entity.Course;
import com.example.finalinternship.entity.Student;
import com.example.finalinternship.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentCourseRepo extends JpaRepository<StudentCourse, Integer> {

    List<StudentCourse> findById_Student(Student student);
    StudentCourse findById_StudentAndId_Course(Student student, Course course);
}
