package com.example.finalinternship.mapper;

import com.example.finalinternship.dto.StudentCourseDTO;
import com.example.finalinternship.entity.StudentCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface StudentCourseMapper {
    @Mapping(source = "id.course", target = "course")
    StudentCourseDTO toStudentCourseDTO(StudentCourse studentCourse);
    @Mapping(source = "course", target = "id.course")
    StudentCourse toStudentCourse(StudentCourseDTO studentCourseDTO);
    List<StudentCourseDTO> toListStudentCourseDTO(List<StudentCourse> studentCourseList);
    List<StudentCourse> toListStudentCourse(List<StudentCourseDTO> studentCourseDTOs);
}
