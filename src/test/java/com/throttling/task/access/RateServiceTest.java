package com.throttling.task.access;

import com.throttling.task.access.interfaces.IBucket;
import com.throttling.task.access.interfaces.IBucketsService;
import com.throttling.task.access.interfaces.IRateService;
import com.throttling.task.access.interfaces.ITask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RateServiceTest {
    IBucketsService bucketsController;

    @BeforeEach
    @DisplayName("new BucketsController()")
    void setUp(){
        bucketsController = new BucketsService();
    }

    @Nested
    @DisplayName("after creating a bucket controller")
    class AfterCreating {

        @Test
        @DisplayName("burst count check")
        void getBurstCount() {
            IRateService rateController = new RateService(bucketsController, 1, 6);
            assertEquals(rateController.getBurstCount(), 6);
        }

        @Test
        @DisplayName("average burst count check")
        void getAverageBurstCount() {
            IRateService rateController1 = new RateService(bucketsController, 1, 150);
            assertAll("calculated rate is more than optimal",
                    () -> assertEquals(rateController1.getAverageBurstCount(), 1),
                    () -> assertEquals(rateController1.getRate(), 400)
            );

            IRateService rateController2 = new RateService(bucketsController, 1, 200);
            assertAll("calculated rate is less than optimal",
                    () -> assertEquals(rateController2.getAverageBurstCount(), 2),
                    () -> assertEquals(rateController2.getRate(), 600)
            );
        }

        @Test
        @DisplayName("tokens adder")
        void start() throws InterruptedException {
            IRateService rateController = new RateService(bucketsController, 1, 150);
            for (int i = 0; i < 1000000; i++) {
                IBucket bucket = bucketsController.create(String.valueOf(i));
                bucket.setMaxTokensCount(rateController.getBurstCount());
                bucket.addTokens(rateController.getAverageBurstCount());
            }
            assertEquals(bucketsController.bucketCount(), 1000000);

            ITask rateStarter = (ITask) rateController;
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2 + rateStarter.reservedThreadsCount());
            rateStarter.start(executorService);

            AtomicInteger t = new AtomicInteger();
            executorService.scheduleWithFixedDelay(() -> {
                t.set(bucketsController.get(String.valueOf(100)).getBaseTokens());
            }, 0, 1, TimeUnit.MINUTES);

            sleep(61000);
            assertEquals(t.get(), 150);
        }
    }
}