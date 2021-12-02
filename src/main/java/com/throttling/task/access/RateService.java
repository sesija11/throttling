package com.throttling.task.access;

import com.throttling.task.access.interfaces.IBucketsService;
import com.throttling.task.access.interfaces.IRateService;
import com.throttling.task.access.interfaces.ITask;
import lombok.Data;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Data
class RateService implements IRateService, ITask {
    private final IBucketsService bucketsController;
    private final Integer burstCount;
    private final Integer averageBurstCount;
    private final Integer rate;
    private final int optimalRate = 400;

    public RateService(IBucketsService bucketsController, Integer minutes, Integer burstCount) {

        this.bucketsController = bucketsController;
        this.burstCount = burstCount;

        double millis = (double) TimeUnit.MINUTES.toMillis(minutes);
        int calculatedRate = (int) Math.round(millis / burstCount);

        if (calculatedRate < optimalRate) {
            averageBurstCount = (int)Math.floor((double) (optimalRate * 2) / (double) calculatedRate);
            rate = calculatedRate * averageBurstCount;
        } else {
            averageBurstCount = 1;
            rate = calculatedRate;
        }
    }

    @Override
    public void start(ScheduledExecutorService executorService) {
        executorService.scheduleAtFixedRate(() -> bucketsController.addTokens(getAverageBurstCount()), 0, rate, TimeUnit.MILLISECONDS);
    }

    @Override
    public int reservedThreadsCount() {
        return 3;
    }
}
