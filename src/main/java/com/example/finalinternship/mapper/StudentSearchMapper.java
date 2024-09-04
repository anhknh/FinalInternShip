package com.example.finalinternship.mapper;


import com.example.finalinternship.dto.CourseDTO;
import com.example.finalinternship.dto.StudentCourseDTO;
import com.example.finalinternship.dto.StudentDTO;
import com.example.finalinternship.dto.StudentSearchDTO;
import com.example.finalinternship.entity.Student;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentCourseMapper.class, CourseMapper.class})
public interface StudentSearchMapper {
    @Mapping(source = "studentCourses", target = "studentCourseDTOS")
    StudentSearchDTO toStudentSearchDTO(Student student);

    @Mapping(source = "studentCourseDTOS", target = "studentCourses")
    Student toStudent(StudentSearchDTO studentSearchDTO);

    List<StudentSearchDTO> toListStudentSearchDTO(List<Student> students);
    List<Student> toListStudent(List<StudentSearchDTO> studentDTOs);

    @AfterMapping
    default void afterMapDTO(@MappingTarget StudentSearchDTO studentSearchDTO, Student course ) {
        List<CourseDTO> list = studentSearchDTO.getStudentCourseDTOS().stream().map(StudentCourseDTO::getCourse).toList();
        List<String> listCode = list.stream().map(CourseDTO::getCourseCode).toList();
        studentSearchDTO.setCourse(String.join(", ", listCode));
    }
}
