package com.throttling.task.access;

import com.throttling.task.access.interfaces.IAccessController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccessControllerTest {

    @Test
    @DisplayName("access check")
    void checkAccess() throws Exception {
        IAccessController controller = new AccessController((short) 1, (short) 150);
        controller.start();

        AtomicBoolean check1 = new AtomicBoolean(true);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(102);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executorService.scheduleAtFixedRate(() -> {
                if (!controller.checkAccess(String.valueOf(finalI + 1000)))
                    check1.set(false);
            }, 0, 600, TimeUnit.MILLISECONDS);
        }

        sleep(61000 + 60000);
        assertTrue(check1.get());

        AtomicBoolean check2 = new AtomicBoolean(true);
        executorService.scheduleAtFixedRate(() -> {
            if (!controller.checkAccess(String.valueOf(222 + 1000)))
                check2.set(false);

        }, 0, 300, TimeUnit.MILLISECONDS);

        sleep(60000);
        assertFalse(check2.get());
    }
}