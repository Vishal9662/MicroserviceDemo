package com.teachers.service.impl;

import com.teachers.exception.TeachersException;
import com.teachers.model.Teachers;
import com.teachers.repository.TeachersRepository;
import com.teachers.service.TeachersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeachersServiceImpl implements TeachersService {

    Logger log = LoggerFactory.getLogger(TeachersServiceImpl.class);

    @Autowired
    private TeachersRepository teachersRepository;

    @RabbitListener(queues = "studentCreatedQueue")
    public void handleStudentCreated(Object student) {
        // Handle the student created event, e.g., update teacher records

        log.info("Received student created event: {}", student);
    }

    @Override
    public Teachers addOrUpdateTeacher(Teachers teachers) {
        return teachersRepository.save(teachers);
    }

    @Override
    public List<Teachers> getAllTeachersDetails() {
        return teachersRepository.findAll();
    }

    @Override
    public Teachers getTeacherById(Long id) throws TeachersException {
        Optional<Teachers> existingStudent = teachersRepository.findById(id);

        if (existingStudent.isPresent()) {
            return existingStudent.get();
        } else {
            throw new TeachersException("Teacher is not found 😜🤪😝");
        }
    }
}
