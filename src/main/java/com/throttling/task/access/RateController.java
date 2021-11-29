package com.throttling.task.access;

import com.throttling.task.access.interfaces.IBucketsController;
import com.throttling.task.access.interfaces.IRateController;
import com.throttling.task.access.interfaces.ITask;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class RateController implements IRateController, ITask {
    private final IBucketsController bucketsController;
    private final Integer burstCount;
    private final Integer averageBurstCount;
    private final Integer rate;
    private final int optimalRate = 400;

    public RateController(IBucketsController bucketsController, Integer minutes, Integer burstCount) {

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
    public Integer getBurstCount() {
        return burstCount;
    }

    @Override
    public Integer getAverageBurstCount() {
        return averageBurstCount;
    }

    @Override
    public Integer getRate() {
        return rate;
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
