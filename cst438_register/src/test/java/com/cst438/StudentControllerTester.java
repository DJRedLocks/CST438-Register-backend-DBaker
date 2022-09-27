package com.cst438;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import com.cst438.controller.ScheduleController;
import com.cst438.controller.StudentController;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;

/* 
 * Example of using Junit with Mockito for mock objects
 *  the database repositories are mocked with test data.
 *  
 * Mockmvc is used to test a simulated REST call to the RestController
 * 
 * the http response and repository is verified.
 * 
 *   Note: This tests uses Junit 5.
 *  ContextConfiguration identifies the controller class to be tested
 *  addFilters=false turns off security.  (I could not get security to work in test environment.)
 *  WebMvcTest is needed for test environment to create Repository classes.
 */
@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class StudentControllerTester {

   static final String URL = "http://localhost:8080";
   public static final int TEST_COURSE_ID = 40442;
   public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
   public static final String TEST_STUDENT_NAME  = "test";
   public static final int TEST_YEAR = 2021;
   public static final String TEST_SEMESTER = "Fall";

   @MockBean
   StudentRepository studentRepository;

   @Autowired
   private MockMvc mvc;

   @Test
   public void createStudent() throws Exception {
      
      MockHttpServletResponse response;
      
      //Student student = studentRepository.findByEmail( TEST_STUDENT_EMAIL );
      //Successful add test         
         Student s = new Student();
         s.setName(TEST_STUDENT_NAME);
         s.setEmail(TEST_STUDENT_EMAIL);
         s.setStatusCode(0);
         s.setStudent_id(99);
         given( studentRepository.save( any() ) ).willReturn(s);
         response = mvc.perform(
               MockMvcRequestBuilders
                  .post("/createstudent?student_email=" + TEST_STUDENT_EMAIL + "&name=" + TEST_STUDENT_NAME))
               .andReturn().getResponse();
         
         // verify that return status = OK (value 200) 
         assertEquals(200, response.getStatus());
         Student retrieveStudent = fromJsonString(response.getContentAsString(), Student.class);

         assertEquals(TEST_STUDENT_EMAIL, retrieveStudent.getEmail());
   }
   
   

   /*
    * Test Set Registration Hold - Place a hold on a Student's registration status
    * setRegHold
    * */
   @Test
   public void setRegHold() throws Exception {
      MockHttpServletResponse response;

      Student teststudent = new Student();
      teststudent.setEmail(TEST_STUDENT_EMAIL);
      teststudent.setName(TEST_STUDENT_NAME);
      teststudent.setStatusCode(0);
      teststudent.setStudent_id(1);
      
      studentRepository.save(teststudent);
      
      Student student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
      
      if(student != null) {
         
         student.setStatusCode(1);
         student.setStatus("Registration Hold");
         
         Student savedStudent = studentRepository.save(student);

         response = mvc.perform(
               MockMvcRequestBuilders
                  .post("/setHold"))
               .andReturn().getResponse();
         
         // verify that return status = OK (value 200) 
         assertEquals(200, response.getStatus());
         
         Student statusStudent = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
         assertEquals(1, statusStudent.getStatusCode());
         
      }
   }
   
   /*
    * Release Hold - Release a hold on a Student's registration status
    * releaseHold
    * */
   
   private void releaseHold() throws Exception {
      MockHttpServletResponse response;

      Student teststudent = new Student();
      teststudent.setEmail(TEST_STUDENT_EMAIL);
      teststudent.setName(TEST_STUDENT_NAME);
      teststudent.setStatusCode(1);
      teststudent.setStudent_id(1);
      
      studentRepository.save(teststudent);
      
      Student student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
      
      if(student != null) {
         
         student.setStatusCode(0);
         student.setStatus("");
         
         Student savedStudent = studentRepository.save(student);

         response = mvc.perform(
               MockMvcRequestBuilders
                  .post("/releaseHold"))
               .andReturn().getResponse();
         
         // verify that return status = OK (value 200) 
         assertEquals(200, response.getStatus());
         
         Student statusStudent = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
         assertEquals(0, statusStudent.getStatusCode());
      }
   }
   
   private static String asJsonString(final Object obj) {
      try {

         return new ObjectMapper().writeValueAsString(obj);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private static <T> T  fromJsonString(String str, Class<T> valueType ) {
      try {
         return new ObjectMapper().readValue(str, valueType);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

}
