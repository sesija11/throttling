package com.throttling.task.access;

import com.throttling.task.access.interfaces.ISession;

import java.util.Random;

class Session implements ISession {
    private Short sessionID = -1;
    private final Random random = new Random();

    public Session() {
        updateSessionID();
    }

    @Override
    public Short getSessionID() {
        return sessionID;
    }

    @Override
    public synchronized void updateSessionID() {
        sessionID = (short) random.nextInt(Short.MAX_VALUE);
    }

}
