package com.student.controller;

import com.student.exception.StudentException;
import com.student.model.Student;
import com.student.service.KafkaProducer;
import com.student.service.StudentService;
import com.student.util.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crud")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/add-update")
    public ResponseEntity<ResponseModel> addOrUpdateStudent(@RequestBody Student student) {
        Student savedStudent = studentService.addOrUpdateStudent(student);
        ResponseModel response = new ResponseModel(HttpStatus.CREATED, "Successfully Saved 🥳🥳", savedStudent);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllStudents")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel> getAllStudentsList() {
        List<Student> studentList = studentService.getAllStudentDetails();
        ResponseModel response = new ResponseModel(HttpStatus.OK, "Student List Fetched Successfully 🥳🥳", studentList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getStudentById/{id}")
//    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResponseModel> getStudentById(@PathVariable("id") Long id) throws StudentException {
        Student student = studentService.getStudentById(id);
        ResponseModel response = new ResponseModel(HttpStatus.OK, "Student is found 🥳🥳", student);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/send-message")
    public String sendMessage(@RequestParam String message) {
        kafkaProducer.sendMessage(message);
        return "Message sent to Kafka: " + message;
    }
}
