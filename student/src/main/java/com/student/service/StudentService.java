package com.student.service;

import com.student.exception.StudentException;
import com.student.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    Student addOrUpdateStudent(Student student);

    List<Student> getAllStudentDetails();

    Student getStudentById(Long id) throws StudentException;
}
