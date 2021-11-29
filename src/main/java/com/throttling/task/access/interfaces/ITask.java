package com.throttling.task.access.interfaces;

import java.util.concurrent.ScheduledExecutorService;

public interface ITask {

    void start(ScheduledExecutorService executorService);
    
    int reservedThreadsCount();
}
