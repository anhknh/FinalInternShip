package com.example.finalinternship.service;

import com.example.finalinternship.dto.CourseDTO;
import com.example.finalinternship.entity.Course;
import com.example.finalinternship.exception.DuplicateCourseCodeException;
import com.example.finalinternship.exception.ResourceNotFoundException;
import com.example.finalinternship.mapper.CourseMapper;
import com.example.finalinternship.repository.CourseRepo;
import com.example.finalinternship.repository.StudentRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    CourseMapper courseMapper;
    @PersistenceContext
    EntityManager em;

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepo.findAll();
        return courseMapper.toListDto(courses);
    }

    public CourseDTO getDetailCourse(Integer idCourse) {
        if (!(idCourse != null && courseRepo.existsById(idCourse))) {
            throw new ResourceNotFoundException("Course", idCourse);
        }
        Course course = courseRepo.findById(idCourse).orElse(null);
        if (course != null) {
            return courseMapper.toDto(course);
        }
        throw new ResourceNotFoundException("Course", idCourse);
    }

    public List<CourseDTO> searchCourse(String courseCode, String title,
                                        String startDate, String endDate, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> course = cq.from(Course.class);

        List<Predicate> predicates = new ArrayList<>();

        if (courseCode != null && !courseCode.isEmpty()) {
            predicates.add(cb.like(course.get("courseCode"), "%" + courseCode + "%"));
        }
        if (title != null && !title.isEmpty()) {
            predicates.add(cb.like(course.get("title"), "%" + title + "%"));
        }
        if (startDate != null && endDate != null) {
            predicates.add(cb.between(course.get("createdDate").as(String.class), startDate, endDate));
        } else if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(course.get("createdDate").as(String.class), startDate));
        } else if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(course.get("createdDate").as(String.class), endDate));
        }
        predicates.add(cb.equal(course.get("status"), 1));

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Course> query = em.createQuery(cq);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Course> courses = query.getResultList();
        return courseMapper.toListDto(courses);
    }


    @Transactional
    public CourseDTO addCourse(CourseDTO courseDTO, MultipartFile file) throws IOException {
        if (courseRepo.existsByCourseCode(courseDTO.getCourseCode())){
            throw new DuplicateCourseCodeException("CourseCode", courseDTO.getCourseCode());
        }
        Course course = courseMapper.toEntity(courseDTO);
        course.setImage(fileUploadService.saveFile(file));
        course.setStatus(1);
        return courseMapper.toDto(courseRepo.save(course));
    }

    @Transactional
    public CourseDTO updateCourse(CourseDTO courseDTO, MultipartFile file) throws IOException {
        if (!(courseDTO.getId() != null && courseRepo.existsById(courseDTO.getId()))) {
            throw new ResourceNotFoundException("Course", courseDTO.getId());
        }
        Course course = courseRepo.findById(courseDTO.getId()).orElse(null);
        if (course != null) {
            fileUploadService.deleteFile(course.getImage());
            course.setImage(fileUploadService.saveFile(file));
            course.setTitle(courseDTO.getTitle());
            course.setDescription(courseDTO.getDescription());
            course.setStatus(courseDTO.getStatus());
            return courseMapper.toDto(courseRepo.save(course));
        }
        throw new ResourceNotFoundException("Course", courseDTO.getId());
    }

    @Transactional
    public CourseDTO deleteCourse(Integer idCourse) {
        if (!(idCourse != null && courseRepo.existsById(idCourse))) {
            throw new ResourceNotFoundException("Course", idCourse);
        }
        Course course = courseRepo.findById(idCourse).orElse(null);
        if (course != null) {
            course.setStatus(0);
            courseRepo.save(course);
            return courseMapper.toDto(course);
        }
        throw new ResourceNotFoundException("Course", idCourse);
    }

}
