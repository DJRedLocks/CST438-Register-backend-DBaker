package com.cst438.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.EnrollmentDTO;


public class GradebookServiceREST extends GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	String gradebook_url;
	
	public GradebookServiceREST() {
		System.out.println("REST grade book service");
	}

	// gradebook.enrollStudent
	// is an HTTP POST to EnrollmentController in gradebook
	// to do this, the POST needs to have a URL mapping of "/enrollment"
	// in the request body, EnrollmentController need an EnrollmentDTO object
	// Here, we need to create an EnrollmentDTO, set the courseid & email of the student
	// print for debugging purposes
	// create the HTTP message with "restTemplate([URL], [object/data to send], [type to be returned])"
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		
		//TODO  complete this method in homework 4
		EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
		enrollmentDTO.course_id = course_id;
		enrollmentDTO.studentEmail = student_email;
		enrollmentDTO.studentName = student_name;
		
		System.out.println("Post to gradebook " + enrollmentDTO);
		
		EnrollmentDTO response = restTemplate.postForObject(gradebook_url + "/enrollment", enrollmentDTO, EnrollmentDTO.class);
		
		System.out.println("Response from gradebook " + response);
	}

}
