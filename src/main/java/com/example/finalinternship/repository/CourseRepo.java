package com.example.finalinternship.repository;

import com.example.finalinternship.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo  extends JpaRepository<Course, Integer> {

//    @Query(value = "select o from Course o left join fetch o.students")
//    List<Course> findAll();

    boolean existsByCourseCode(String courseCode);
}
