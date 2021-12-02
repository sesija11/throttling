package com.throttling.task.access;

import com.throttling.task.access.interfaces.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AccessService implements IAccessService {
    private final IBucketsService bucketController;
    private final IRateService rateController;
    private final ISession session = new Session();
    private final ITask optimizer;

    public AccessService(int minutes, int bursts) throws Exception {

        if (minutes < 1 || bursts < 1)
            throw new Exception("The number of minutes and bursts must be more than 0.");

        if (minutes > 1440)
            throw new Exception("The number of minutes must be less than 1440.");

        bucketController = new BucketsService();
        rateController = new RateService(bucketController, minutes, bursts);
        optimizer = new Optimizer(bucketController, session, (short) 24);
    }

    @Override
    public synchronized boolean checkAccess(String id) {

        IBucket bucket;
        if (!bucketController.contains(id)) {
            bucket = bucketController.create(id);
            bucket.setMaxTokensCount(rateController.getBurstCount());
            bucket.addTokens(rateController.getAverageBurstCount());
        } else {
            bucket = bucketController.get(id);
        }

        if (!bucket.getSessionID().equals(session.getSessionID()))
            bucket.setSessionID(session.getSessionID());

        return bucket.removeToken();
    }

    @Override
    public void start() {
        ITask rateStarter = (ITask) rateController;
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(optimizer.reservedThreadsCount() + rateStarter.reservedThreadsCount());
        rateStarter.start(executorService);
        optimizer.start(executorService);
    }

}
