package com.example.finalinternship.service;

import com.example.finalinternship.dto.StudentDTO;
import com.example.finalinternship.entity.Course;
import com.example.finalinternship.entity.Student;
import com.example.finalinternship.entity.StudentCourse;
import com.example.finalinternship.entity.StudentCourseId;
import com.example.finalinternship.exception.DuplicateCourseCodeException;
import com.example.finalinternship.exception.ResourceNotFoundException;
import com.example.finalinternship.mapper.StudentMapper;
import com.example.finalinternship.mapper.StudentSearchMapper;
import com.example.finalinternship.repository.CourseRepo;
import com.example.finalinternship.repository.StudentCourseRepo;
import com.example.finalinternship.repository.StudentRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    StudentCourseRepo studentCourseRepo;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    StudentSearchMapper studentSearchMapper;
    @PersistenceContext
    EntityManager em;


    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepo.findAll();
        return studentMapper.toListStudentDTO(students);
    }

    public StudentDTO getDetailStudent(Integer studentId) {
        if (!(studentId != null && studentRepo.existsById(studentId))) {
            throw new ResourceNotFoundException("Student", studentId);
        }
        Student student = studentRepo.findById(studentId).orElse(null);
        if (student != null) {
            return studentMapper.toStudentDTO(student);
        }
        throw new ResourceNotFoundException("Student", studentId);
    }

    public List<StudentDTO> searchStudent(String studentCode, String name,
                                                String email, String startDate, String endDate, String courseCode, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> student = cq.from(Student.class);
        student.fetch("studentCourses", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (studentCode != null && !studentCode.isEmpty()) {
            predicates.add(cb.like(student.get("studentCode"), "%" + studentCode + "%"));
        }
        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(student.get("name"), "%" + name + "%"));
        }
        if (email != null && !email.isEmpty()) {
            predicates.add(cb.like(student.get("email"), "%" + email + "%"));
        }
        if (startDate != null && endDate != null) {
            predicates.add(cb.between(student.get("createdDate").as(String.class), startDate, endDate));
        } else if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(student.get("createdDate").as(String.class), startDate));
        } else if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(student.get("createdDate").as(String.class), endDate));
        }

        if (courseCode != null) {
            predicates.add(cb.equal(student.get("studentCourses").get("id").get("course").get("courseCode"), courseCode));
        }
        predicates.add(cb.equal(student.get("status"), 1));

        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Student> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Student> students = query.getResultList();
        return studentMapper.toListStudentDTO(students);
    }


    @Transactional
    public StudentDTO addStudent(StudentDTO studentDTO, MultipartFile file, Integer[] listCourseIds){
        if (studentRepo.existsByStudentCode(studentDTO.getStudentCode())){
            throw new DuplicateCourseCodeException("StudentCode", studentDTO.getStudentCode());
        }
        List<Course> allCourses = courseRepo.findAll();

        List<Course> selectedCourses = allCourses.stream()
                .filter(course -> Arrays.asList(listCourseIds).contains(course.getId()))
                .collect(Collectors.toList());

        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setStudentCode(studentDTO.getStudentCode());
        student.setStatus(1);
        student.setImage(fileUploadService.saveFile(file, null));
        Student savedStudent = studentRepo.save(student);
        for (Course course : selectedCourses) {
            StudentCourse studentCourse = new StudentCourse();
            StudentCourseId studentCourseId = new StudentCourseId(savedStudent, course);
            studentCourse.setId(studentCourseId);
            studentCourse.setLearnStatus(1);
            studentCourseRepo.save(studentCourse);
        }
        return studentMapper.toStudentDTO(savedStudent);
    }

    @Transactional
    public StudentDTO updateStudent(StudentDTO studentDTO, MultipartFile file, Integer[] listCourseIds){
        if (!(studentDTO.getId() != null && studentRepo.existsById(studentDTO.getId()))) {
            throw new ResourceNotFoundException("Student", studentDTO.getId());
        }

        Student student = studentRepo.findById(studentDTO.getId()).orElse(null);
        if (student != null) {
            student.setName(studentDTO.getName());
            student.setEmail(studentDTO.getEmail());
            student.setStatus(studentDTO.getStatus());
            fileUploadService.deleteFile(studentDTO.getImage());
            student.setImage(fileUploadService.saveFile(file, student.getImage()));
        }

        List<Course> allCourses = courseRepo.findAll();

        List<Course> selectedCourses = allCourses.stream()
                .filter(course -> Arrays.asList(listCourseIds).contains(course.getId()))
                .collect(Collectors.toList());

        // set LearnStatus of all course = 0
        List<StudentCourse> existingCourses = studentCourseRepo.findById_Student(student);
        for (StudentCourse studentCourse : existingCourses) {
            studentCourse.setLearnStatus(0);
            studentCourseRepo.save(studentCourse);
        }

        // update course for student
        for (Course course : selectedCourses) {
            // check course exist set LearnStatus = 1
            StudentCourse existingStudentCourse = studentCourseRepo.findById_StudentAndId_Course(student, course);
            if (existingStudentCourse != null) {
                existingStudentCourse.setLearnStatus(1);
                studentCourseRepo.save(existingStudentCourse);
            } else {
                // add new course
                StudentCourse studentCourse = new StudentCourse();
                StudentCourseId studentCourseId = new StudentCourseId(student, course);
                studentCourse.setId(studentCourseId);
                studentCourse.setLearnStatus(1);
                studentCourseRepo.save(studentCourse);
            }
        }
        Student updatedStudent = studentRepo.save(student);
        return studentMapper.toStudentDTO(updatedStudent);
    }


    @Transactional
    public StudentDTO deleteStudent(Integer studentId) {
        if (!(studentId != null && courseRepo.existsById(studentId))) {
            throw new ResourceNotFoundException("Student", studentId);
        }
        Student student = studentRepo.findById(studentId).orElse(null);
        if (student != null) {
            student.setStatus(0);
            studentRepo.save(student);
            return studentMapper.toStudentDTO(student);
        }
        throw new ResourceNotFoundException("Student", studentId);
    }
}
