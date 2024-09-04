package com.example.finalinternship.repository;

import com.example.finalinternship.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepo  extends JpaRepository<Student, Integer> {
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.studentCourses sc" +
            " LEFT JOIN FETCH sc.id.course")
    List<Student> findAll();

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.studentCourses sc " +
            "LEFT JOIN FETCH sc.id.course WHERE s.id = :id")
    Optional<Student> findById(Integer id);

    boolean existsByStudentCode(String studentCode);

    @EntityGraph(attributePaths = {"studentCourses", "studentCourses.id.course"})
    @Query("SELECT s FROM Student s " +
            "LEFT JOIN s.studentCourses sc " +
            "LEFT JOIN sc.id.course c " +
            "WHERE (:studentCode IS NULL OR s.studentCode LIKE %:studentCode%) " +
            "AND (:name IS NULL OR s.name LIKE %:name%) " +
            "AND (:email IS NULL OR s.email LIKE %:email%) " +
            "AND (:courseCode IS NULL OR c.courseCode LIKE %:courseCode%) " +
            "AND s.status = 1 " +
            "AND (:startDate IS NULL OR s.createdDate >= :startDate) " +
            "AND (:endDate IS NULL OR s.createdDate <= :endDate)")
    Page<Student> searchStudents(String studentCode, String name, String email,
                                 Date startDate, Date endDate, String courseCode, Pageable pageable);

}
