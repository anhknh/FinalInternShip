package com.example.finalinternship.dto;


import com.example.finalinternship.entity.Course;
import com.example.finalinternship.exception.CreateGroup;
import com.example.finalinternship.exception.CustomValidation.Status;
import com.example.finalinternship.exception.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

//@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private Integer id;
    @NotBlank(message = "NotBlank", groups = {CreateGroup.class})
    @Length(min = 1, max = 8,message = "Length", groups = {CreateGroup.class})
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Pattern", groups = {CreateGroup.class})
    private String studentCode;
    @NotBlank(message = "NotBlank", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Pattern", groups = {CreateGroup.class, UpdateGroup.class})
    @Length(min = 1, max = 255, message = "Length", groups = {CreateGroup.class, UpdateGroup.class})
    private String name;
    @NotBlank(message = "NotBlank", groups = {CreateGroup.class, UpdateGroup.class})
    @Email(message = "email", groups = {CreateGroup.class, UpdateGroup.class})
    @Length(max = 255, message = "Length", groups = {CreateGroup.class, UpdateGroup.class})
    private String email;
    private String image;
    @Status(message = "Status", groups = {UpdateGroup.class})
    private Integer status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date updatedDate;
    @JsonIgnore
    private List<StudentCourseDTO> studentCourseDTOS;
    private List<CourseDTO> courseDTOS;
}
