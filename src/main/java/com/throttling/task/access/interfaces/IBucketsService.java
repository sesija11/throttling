package com.throttling.task.access.interfaces;

public interface IBucketsService {

    IBucket create(String id);

    IBucket get(String id);

    int bucketCount();

    boolean contains(String id);

    void addTokens(Integer count);

    void removeUnusedBuckets(Short sessionID);
}
