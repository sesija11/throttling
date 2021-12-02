package com.throttling.task.controller;

import com.throttling.task.access.AccessService;
import com.throttling.task.access.interfaces.IAccessService;
import com.throttling.task.config.AccessConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class AccessController {
    private IAccessService accessController;

    @Autowired
    private AccessConfig accessConfig;

    @PostConstruct
    public void init() throws Exception {
        accessController = new AccessService(accessConfig.getMinutes(), accessConfig.getBursts());
        accessController.start();
    }

    public boolean checkAccess(String id) {
        return accessController.checkAccess(id);
    }
}
