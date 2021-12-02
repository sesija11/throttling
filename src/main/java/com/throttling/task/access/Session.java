package com.throttling.task.access;

import com.throttling.task.access.interfaces.ISession;
import lombok.Data;

import java.util.Random;

@Data
class Session implements ISession {
    private final Random random = new Random();
    private Short sessionID = -1;

    public Session() {
        updateSessionID();
    }

    @Override
    public synchronized void updateSessionID() {
        sessionID = (short) random.nextInt(Short.MAX_VALUE);
    }

}
