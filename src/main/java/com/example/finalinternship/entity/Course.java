package com.example.finalinternship.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String courseCode;
    private String title;
    private String description;
    private String image;
    private Integer status;
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
    @OneToMany(mappedBy = "id.course", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<StudentCourse> studentCourses;
}
