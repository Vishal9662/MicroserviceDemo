package com.teachers.controller;

import com.teachers.exception.TeachersException;
import com.teachers.model.Teachers;
import com.teachers.service.TeachersService;
import com.teachers.util.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeachersController {

    @Autowired
    private TeachersService teachersService;

    @PostMapping("/add-update")
    public ResponseEntity<ResponseModel> addOrUpdateTeachers(@RequestBody Teachers teachers) {
        Teachers savedTeachers = teachersService.addOrUpdateTeacher(teachers);
        ResponseModel response = new ResponseModel(HttpStatus.CREATED, "Successfully Saved 🥳🥳", savedTeachers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllTeachers")
    public ResponseEntity<ResponseModel> getAllTeachersList() {
        List<Teachers> teachersList = teachersService.getAllTeachersDetails();
        ResponseModel response = new ResponseModel(HttpStatus.OK, "Teachers List Fetched Successfully 🥳🥳", teachersList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getTeacherById/{id}")
    public ResponseEntity<ResponseModel> getTeachersById(@PathVariable("id") Long id) throws TeachersException {
        Teachers teacher = teachersService.getTeacherById(id);
        ResponseModel response = new ResponseModel(HttpStatus.OK, "Teachers is found 🥳🥳", teacher);
        return ResponseEntity.ok(response);
    }
}
