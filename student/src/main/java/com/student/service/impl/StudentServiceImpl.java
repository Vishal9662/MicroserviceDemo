package com.student.service.impl;

import com.student.exception.StudentException;
import com.student.model.Student;
import com.student.repository.StudentRepository;
import com.student.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "my_topic"; // Kafka Topic Name
    private static final String EXCHANGE_NAME = "studentExchange";
    private static final String ROUTING_KEY = "student.created";

    @Override
    public Student addOrUpdateStudent(Student student) {
        Student saved = studentRepository.save(student);
        log.info("Student {}", saved.getName() + " is created successfully...");
        if (saved != null) {
            kafkaTemplate.send(TOPIC, saved.toString());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, "Student " + saved.getName() + " is created...😀");
        }
        return saved;
    }

    @Override
    @Cacheable(value = "students", key = "#root.method.name")
    public List<Student> getAllStudentDetails() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) throws StudentException {
        Optional<Student> existingStudent = studentRepository.findById(id);

        if (existingStudent.isPresent()) {
            return existingStudent.get();
        } else {
            throw new StudentException("Student is not found 😜🤪😝");
        }
    }
}
