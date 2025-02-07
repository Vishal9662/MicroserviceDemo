package com.teachers.service;

import com.teachers.exception.TeachersException;
import com.teachers.model.Teachers;

import java.util.List;

public interface TeachersService {
    Teachers addOrUpdateTeacher(Teachers teachers);

    List<Teachers> getAllTeachersDetails();

    Teachers getTeacherById(Long id) throws TeachersException;
}
