package com.example.finalinternship.mapper;


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
}


