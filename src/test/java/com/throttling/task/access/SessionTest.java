package com.throttling.task.access;

import com.throttling.task.access.interfaces.ISession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SessionTest {
    ISession session;

    @BeforeEach
    @DisplayName("new Session()")
    void setUp() {
        session = new Session();
    }

    @Test
    @DisplayName("new id generate func")
    void updateSessionID() {
        Short id = session.getSessionID();
        session.updateSessionID();
        assertNotEquals(id, session.getSessionID());
    }
}