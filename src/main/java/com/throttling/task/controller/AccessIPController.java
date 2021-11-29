package com.throttling.task.controller;

import com.throttling.task.access.AccessController;
import com.throttling.task.access.interfaces.IAccessController;
import com.throttling.task.model.AccessProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("")
public class AccessIPController {
    private IAccessController accessController;

    @Autowired
    private AccessProperties accessProperties;

    @PostConstruct
    public void init() throws Exception {
        accessController = new AccessController(accessProperties.getMinutes(), accessProperties.getBursts());
        accessController.start();
    }

    @GetMapping("/")
    public ResponseEntity checkAccessIP(HttpServletRequest request) {
        try {
            if (accessController.checkAccess(request.getRemoteAddr())) {
                return ResponseEntity.ok(null);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
