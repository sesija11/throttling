package com.throttling.task.controller;

import com.throttling.task.access.AccessService;
import com.throttling.task.access.interfaces.IAccessService;
import com.throttling.task.config.AccessConfig;
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
    private IAccessService accessController;

    @Autowired
    private AccessConfig accessConfig;

    @PostConstruct
    public void init() throws Exception {
        accessController = new AccessService(accessConfig.getMinutes(), accessConfig.getBursts());
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
