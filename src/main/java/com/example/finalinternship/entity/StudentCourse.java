package com.example.finalinternship.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "student_course")

public class StudentCourse {
    @EmbeddedId
    private StudentCourseId id;
    @CreationTimestamp
    @Column(updatable = false)
    private Date enrollmentDate;
    private Integer learnStatus;

}
