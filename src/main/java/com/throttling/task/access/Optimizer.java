package com.throttling.task.access;

import com.throttling.task.access.interfaces.IBucketsService;
import com.throttling.task.access.interfaces.ISession;
import com.throttling.task.access.interfaces.ITask;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public record Optimizer(IBucketsService bucketsController,
                        ISession session,
                        short period) implements ITask {

    @Override
    public void start(ScheduledExecutorService executorService) {
        executorService.scheduleAtFixedRate(() -> {
            bucketsController.removeUnusedBuckets(session.getSessionID());
            session.updateSessionID();
        }, period, period, TimeUnit.MINUTES);
    }

    @Override
    public int reservedThreadsCount() {
        return 1;
    }
}
