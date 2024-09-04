package com.example.finalinternship.mapper;


import com.example.finalinternship.dto.CourseDTO;
import com.example.finalinternship.entity.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO toDto(Course course);
    Course toEntity(CourseDTO courseDTO);
    List<CourseDTO> toListDto(List<Course> courseList);
    List<Course> toListEntity(List<CourseDTO> courseDTOList);
}
