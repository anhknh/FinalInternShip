package com.example.finalinternship.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "student")

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String studentCode;
    private String name;
    private String email;
    private String image;
    private Integer status;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
    @OneToMany(mappedBy = "id.student", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<StudentCourse> studentCourses;
}
