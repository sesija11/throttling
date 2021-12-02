package com.throttling.task.controller;

import com.throttling.task.AccessException;
import com.throttling.task.access.CheckerAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("")
public class AccessIPController {

    @GetMapping("/")
    @CheckerAnnotation("")
    public ResponseEntity checkAccessIP(HttpServletRequest request) {
        return ResponseEntity.ok(null);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<?> handleException() {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }
}
