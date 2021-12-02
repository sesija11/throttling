package com.throttling.task.access;

import com.throttling.task.access.interfaces.IBucket;
import lombok.Data;

@Data
class Bucket implements IBucket {
    private int excessBurstSize = 0;
    private int committedBurstSize = 0;
    private int extraTokens = 0;
    private int baseTokens = 0;
    private Short sessionID = -1;

    @Override
    public void setMaxTokensCount(Integer count) {
        extraTokens = 0;
        baseTokens = 0;
        committedBurstSize = count < 1 ? 0 : count;
        excessBurstSize = (committedBurstSize < 1) ? 0 : (int) Math.round(committedBurstSize / 2.0);
    }

    @Override
    public synchronized void addTokens(Integer count) {
        int tokens = baseTokens + count;

        if (tokens <= committedBurstSize) {
            baseTokens = tokens;
            return;
        }

        baseTokens = committedBurstSize;
        tokens = tokens - committedBurstSize;
        tokens = tokens + extraTokens;
        extraTokens = Math.min(excessBurstSize, tokens);
    }

    @Override
    public boolean removeToken() {
        if (baseTokens == 0 && extraTokens == 0)
            return false;

        if (baseTokens != 0) {
            baseTokens = baseTokens - 1;
        } else {
            extraTokens = extraTokens - 1;
        }
        return true;
    }
}