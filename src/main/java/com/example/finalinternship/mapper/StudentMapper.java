package com.example.finalinternship.mapper;

import com.example.finalinternship.dto.CourseDTO;
import com.example.finalinternship.dto.StudentCourseDTO;
import com.example.finalinternship.dto.StudentDTO;
import com.example.finalinternship.entity.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentCourseMapper.class, CourseMapper.class})
public interface StudentMapper {

    @Mapping(source = "studentCourses", target = "studentCourseDTOS")
    StudentDTO toStudentDTO(Student student);

    @Mapping(source = "studentCourseDTOS", target = "studentCourses")
    Student toStudent(StudentDTO studentDTO);

    List<StudentDTO> toListStudentDTO(List<Student> students);
    List<Student> toListStudent(List<StudentDTO> studentDTOs);

    @AfterMapping
    default void afterMapDTO(@MappingTarget StudentDTO studentDTO, Student course ) {
        List<CourseDTO> list = studentDTO.getStudentCourseDTOS().stream().map(StudentCourseDTO::getCourse).toList();
        studentDTO.setCourseDTOS(list);
    }
}


