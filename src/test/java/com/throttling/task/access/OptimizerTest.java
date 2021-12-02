package com.throttling.task.access;

import com.throttling.task.access.interfaces.IBucket;
import com.throttling.task.access.interfaces.IBucketsService;
import com.throttling.task.access.interfaces.ISession;
import com.throttling.task.access.interfaces.ITask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimizerTest {

    @Test
    @DisplayName("optimization check")
    void start() throws InterruptedException, IOException {
        IBucketsService bucketController = new BucketsService();
        ISession session = new Session();

        for (int i = 0; i < 10; i++) {
            IBucket bucket1 = bucketController.create(String.valueOf(i));
            IBucket bucket2 = bucketController.create(String.valueOf(i + 100));
            bucket2.setSessionID(session.getSessionID());
        }

        assertEquals(bucketController.bucketCount(), 20);

        ITask task = new Optimizer(bucketController, session, (short) 1);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2 + task.reservedThreadsCount());
        task.start(executorService);

        sleep(61000);

        assertEquals(bucketController.bucketCount(), 10);
    }
}