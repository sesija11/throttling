package com.throttling.task.access.interfaces;

public interface IAccessService {

    boolean checkAccess(String id);
    
    void start();
}

