package com.throttling.task.access.interfaces;

public interface IBucket {

    int getExcessBurstSize();

    int getCommittedBurstSize();

    int getExtraTokens();

    int getBaseTokens();

    void addTokens(Integer count);

    void setMaxTokensCount(Integer count);

    boolean removeToken();

    Short getSessionID();

    void setSessionID(Short sessionID);
}

