package com.example.finalinternship.dto;

import com.example.finalinternship.exception.CreateGroup;
import com.example.finalinternship.exception.CustomValidation.Status;
import com.example.finalinternship.exception.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentSearchDTO {
    private Integer id;
    private String studentCode;
    private String name;
    private String email;
    private String image;
    private Integer status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date updatedDate;
    @JsonIgnore
    private List<StudentCourseDTO> studentCourseDTOS;
    private String Course;
}
