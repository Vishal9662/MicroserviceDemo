package com.teachers.exception;

import com.teachers.util.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({TeachersException.class})
    public ResponseEntity<ResponseModel> teacherException(Exception ex) {
        ex.printStackTrace();
        ResponseModel error = new ResponseModel(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
