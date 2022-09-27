package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

@RestController
public class CourseController {
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	/*
	 * endpoint used by gradebook service to transfer final course grades
	 * request body is the gradebook courseDTOG object turned into a JSON string,
	 * routed to updateCourseGrades method, and put into a CourseDTOG instance
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades( @RequestBody CourseDTOG courseDTO, @PathVariable("course_id") int course_id) {
		
		//TODO  complete this method in homework 4
		//loop through (do this for all of the) grades for students enrolled in that course (GradeDTO)
		for (CourseDTOG.GradeDTO g : courseDTO.grades) {
			
			// for each grade in the DTO, find the enrollment record for that student
			Enrollment e = enrollmentRepository.findByEmailAndCourseId(g.student_email, course_id);
			
			// set the grade
			e.setCourseGrade(g.grade);
			
			// save it back to the database
			enrollmentRepository.save(e);
		}
	}
}
