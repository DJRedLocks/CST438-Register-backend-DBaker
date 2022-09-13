package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.Enrollment;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@RestController
public class StudentController
{
   
   @Autowired
   StudentRepository studentRepository;
   
   /*
    * Create Student
    * */
   @PostMapping("/createstudent")
   @Transactional
   private Student createStudent(
      String student_email,
      String name
   ) {
      Student student = studentRepository.findByEmail(student_email);
      
      if(student == null) {
         
         Student s = new Student();
         s.setEmail(student_email);
         s.setName(name);
         s.setStatusCode(0);
         
         //Student savedStudent = studentRepository.save(s);
         
         //return savedStudent;
         return s;
         
      } else {
         throw new ResponseStatusException( 
            HttpStatus.BAD_REQUEST,
            "Student login email already exists in the database.  "
         );
      }
   }
   
   /*
    * Set Registration Hold - Place a hold on a Student's registration status
    * setRegHold
    * */
   @PostMapping("/sethold")
   @Transactional
   private Student setRegHold() {
      
      String student_email = "test@csumb.edu";   // student's email 
      
      Student student = studentRepository.findByEmail(student_email);
      
      if(student != null) {
         
         student.setStatusCode(1);
         student.setStatus("Registration Hold");
         
         Student savedStudent = studentRepository.save(student);

         return savedStudent;
         
      } else {
         throw new ResponseStatusException( 
            HttpStatus.BAD_REQUEST,
            "Student email DNE.  "
         );
      }
   }
   
   
   /*
    * Release Hold - Release a hold on a Student's registration status
    * releaseHold
    * */
   @PostMapping("/releasehold")
   @Transactional
   private Student releaseHold(Student s) {

      String student_email = "test@csumb.edu";   // student's email 
      
      Student student = studentRepository.findByEmail(student_email);
      
      if(student != null) {
         
         student.setStatusCode(0);
         student.setStatus("");
         
         Student savedStudent = studentRepository.save(student);

         return savedStudent;
         
      } else {
         throw new ResponseStatusException( 
            HttpStatus.BAD_REQUEST,
            "Student email DNE.  "
         );
      }
   }
}
