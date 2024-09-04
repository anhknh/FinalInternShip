package com.example.finalinternship.controller;

import com.example.finalinternship.dto.StudentDTO;
import com.example.finalinternship.exception.CreateGroup;
import com.example.finalinternship.exception.UpdateGroup;
import com.example.finalinternship.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/view-all-student")
    public ResponseEntity<List<StudentDTO>> viewAllStudent() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/view-detail-student")
    public ResponseEntity<StudentDTO> viewDetailStudent(@RequestParam(value = "id", required = false) Integer studentId) {
        return ResponseEntity.ok(studentService.getDetailStudent(studentId));
    }

    @GetMapping("/search-student")
    public ResponseEntity<List<StudentDTO>> searchStudent(@RequestParam(value = "studentCode", required = false) String studentCode,
                                                        @RequestParam(value = "name", required = false) String name,
                                                        @RequestParam(value = "email", required = false) String email,
                                                        @RequestParam(value = "startDate", required = false) String startDate,
                                                        @RequestParam(value = "endDate", required = false) String endDate,
                                                        @RequestParam(value = "courseCode", required = false) String courseCode,
                                                        @RequestParam(value = "size", required = false) Optional<Integer> size,
                                                        @RequestParam(value = "page", required = false) Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(5));
        return ResponseEntity.ok(studentService.searchStudent(studentCode,name,email,startDate,endDate,courseCode,pageable));
    }

    @PostMapping("/add-student")
    public ResponseEntity<StudentDTO> addStudent(@Validated(CreateGroup.class)
                                               @ModelAttribute StudentDTO studentDTO,
                                               @RequestParam("courseIds") Integer[] courseIds,
                                               @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(studentService.addStudent(studentDTO, file, courseIds));
    }

    @PutMapping("/update-student")
    public ResponseEntity<StudentDTO> updateStudent(@Validated(UpdateGroup.class)
                                                  @ModelAttribute StudentDTO studentDTO,
                                                  @RequestParam("courseIds") Integer[] courseIds,
                                                  @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(studentService.updateStudent(studentDTO, file, courseIds));
    }

    @DeleteMapping("/delete-student")
    public ResponseEntity<StudentDTO> deleteStudent(@RequestParam(value = "id", required = false) Integer id) {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }
}
