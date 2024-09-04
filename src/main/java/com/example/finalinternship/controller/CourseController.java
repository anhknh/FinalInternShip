package com.example.finalinternship.controller;

import com.example.finalinternship.dto.CourseDTO;
import com.example.finalinternship.exception.CreateGroup;
import com.example.finalinternship.exception.UpdateGroup;
import com.example.finalinternship.service.CourseService;
import com.example.finalinternship.service.FileUploadService;
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
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    FileUploadService fileUploadService;

    @GetMapping("/view-all-course")
    public ResponseEntity<List<CourseDTO>> viewAllCourse() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/view-detail-course")
    public ResponseEntity<CourseDTO> viewDetailCourse(@RequestParam(value = "id", required = false) Integer id) {
        return ResponseEntity.ok(courseService.getDetailCourse(id));
    }

    @GetMapping("/search-course")
    public ResponseEntity<List<CourseDTO>> searchCourse(@RequestParam(value = "courseCode", required = false) String courseCode,
                                                        @RequestParam(value = "title", required = false) String title,
                                                        @RequestParam(value = "startDate", required = false) String startDate,
                                                        @RequestParam(value = "endDate", required = false) String endDate,
                                                        @RequestParam(value = "size", required = false) Optional<Integer> size,
                                                        @RequestParam(value = "page", required = false) Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(5));
        return ResponseEntity.ok(courseService.searchCourse(courseCode,title,startDate,endDate, pageable));
    }

    @PostMapping("/add-course")
    public ResponseEntity<CourseDTO> addCourse(@Validated(CreateGroup.class)
                                               @ModelAttribute CourseDTO courseDTO,
                                               @RequestParam(value = "file", required = false) MultipartFile file)  {
        return ResponseEntity.ok(courseService.addCourse(courseDTO, file));
    }

    @PutMapping("/update-course")
    public ResponseEntity<CourseDTO> updateCourse(@Validated(UpdateGroup.class)
                                                  @ModelAttribute CourseDTO courseDTO,
                                                  @RequestParam(value = "file", required = false) MultipartFile file)  {
        return ResponseEntity.ok(courseService.updateCourse(courseDTO, file));
    }

    @DeleteMapping("/delete-course")
    public ResponseEntity<CourseDTO> deleteCourse(@RequestParam(value = "id", required = false) Integer id) {
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }
}
