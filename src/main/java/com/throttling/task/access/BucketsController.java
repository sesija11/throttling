package com.throttling.task.access;

import com.throttling.task.access.interfaces.IBucket;
import com.throttling.task.access.interfaces.IBucketsController;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class BucketsController implements IBucketsController {
    private final Map<String, IBucket> buckets = new ConcurrentHashMap<>();

    @Override
    public IBucket create(String id) {
        IBucket bucket = new Bucket();
        buckets.put(id, bucket);
        return bucket;
    }

    @Override
    public IBucket get(String id) {
        return buckets.get(id);
    }

    @Override
    public int bucketCount() {
        return buckets.size();
    }

    @Override
    public boolean contains(String id) {
        return buckets.containsKey(id);
    }

    @Override
    public synchronized void addTokens(Integer count) {
        buckets.entrySet().parallelStream().forEach(pair -> pair.getValue().addTokens(count));
    }

    @Override
    public synchronized void removeUnusedBuckets(Short sessionID) {
        var ids = buckets.entrySet().parallelStream()
                .collect(ArrayList::new,
                        (list, element) -> {
                            if (!element.getValue().getSessionID().equals(sessionID))
                                list.add(element.getKey());
                        },
                        ArrayList::addAll);
        ids.parallelStream().forEach(buckets::remove);
    }
}
