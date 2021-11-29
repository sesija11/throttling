package com.throttling.task.access.interfaces;

public interface IAccessController {

    boolean checkAccess(String id);
    
    void start();
}

