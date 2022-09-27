package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class GradebookServiceMQ extends GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	Queue gradebookQueue;
	
	
	public GradebookServiceMQ() {
		System.out.println("MQ grade book service");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		 
		// TODO 
		
		// create EnrollmentDTO and send to gradebookQueue
		EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
		enrollmentDTO.course_id = course_id;
		enrollmentDTO.studentEmail = student_email;
		enrollmentDTO.studentName = student_name;
		
		
		rabbitTemplate.convertAndSend(enrollmentDTO);
		
		//dev console print
		System.out.println("Message send to gradbook service for student "+ student_email +" " + course_id);  
		
	}
	
	@RabbitListener(queues = "registration-queue")
	public void receive(CourseDTOG courseDTOG) {
		System.out.println("Receive enrollment :" + courseDTOG);

		//TODO 
		// same code as CourseController.java
		// for each student grade in courseDTOG,  find the student enrollment entity, update the grade and save back to enrollmentRepository.
		//loop through (do this for all of the) grades for students enrolled in that course (GradeDTO)
				for (CourseDTOG.GradeDTO g : courseDTOG.grades) {
					
					// for each grade in the DTO, find the enrollment record for that student
					Enrollment e = enrollmentRepository.findByEmailAndCourseId(g.student_email, courseDTOG.course_id);
					
					// set the grade
					e.setCourseGrade(g.grade);
					
					// save it back to the database
					enrollmentRepository.save(e);
					
					//dev console print
					System.out.println("final grade update " + g.student_email + " " + courseDTOG.course_id + " " + g.grade);
				}
	}

}
