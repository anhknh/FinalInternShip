package com.example.finalinternship.dto;


import com.example.finalinternship.exception.CreateGroup;
import com.example.finalinternship.exception.CustomValidation.Status;
import com.example.finalinternship.exception.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Integer id;
    @NotBlank(message = "NotBlank", groups = {CreateGroup.class})
    @Length(min = 1, max = 8, message = "Length", groups = {CreateGroup.class})
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Pattern", groups = {CreateGroup.class})
    private String courseCode;
    @NotBlank(message = "NotBlank", groups = {CreateGroup.class, UpdateGroup.class})
    @Length(min = 1, max = 255, message = "Length", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Pattern", groups = {CreateGroup.class, UpdateGroup.class})
    private String title;
    private String description;
    private String image;
    @Status(message = "Status", groups = {UpdateGroup.class})
    private Integer status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date updatedDate;
}
