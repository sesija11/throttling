package com.throttling.task.access;

import com.throttling.task.access.interfaces.IBucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
class BucketTest {
    IBucket bucket;

    @BeforeEach
    @DisplayName("new Bucket()")
    void setUp() {
        bucket = new Bucket();
    }

    @Test
    @DisplayName("sessionID setter check")
    void setSessionID() {
        bucket.setSessionID((short) 11);
        assertEquals(bucket.getSessionID(), (short) 11);
    }

    @Test
    @DisplayName("max tokens count setter check")
    void setMaxTokensCount() {
        bucket.setMaxTokensCount(50);

        assertAll("max tokens count is 50",
                () -> assertEquals(bucket.getBaseTokens(), 0),
                () -> assertEquals(bucket.getExtraTokens(), 0),
                () -> assertEquals(bucket.getCommittedBurstSize(), 50),
                () -> assertEquals(bucket.getExcessBurstSize(), 25)
        );

        bucket.setMaxTokensCount(-1);
        assertAll("max tokens count is -1",
                () -> assertEquals(bucket.getCommittedBurstSize(), 0),
                () -> assertEquals(bucket.getExcessBurstSize(), 0)
        );

        bucket.setMaxTokensCount(3);
        assertAll("max tokens count is 3",
                () -> assertEquals(bucket.getCommittedBurstSize(), 3),
                () -> assertEquals(bucket.getExcessBurstSize(), 2)
        );

    }

    @Test
    @DisplayName("add tokens func check")
    void addTokens() {
        bucket.setMaxTokensCount(5);
        bucket.addTokens(4);
        assertAll("4 tokens added",
                () -> assertEquals(bucket.getBaseTokens(), 4),
                () -> assertEquals(bucket.getExtraTokens(), 0)
        );
        bucket.addTokens(2);
        assertAll("4 + 2 tokens added",
                () -> assertEquals(bucket.getBaseTokens(), 5),
                () -> assertEquals(bucket.getExtraTokens(), 1)
        );
    }

    @Test
    @DisplayName("remove tokens func check")
    void removeToken() {
        bucket.setMaxTokensCount(5);
        bucket.addTokens(4);
        bucket.addTokens(4);

        for (int i = 0; i < 6; i++) {
            bucket.removeToken();
        }
        assertAll("6 tokens removed",
                () -> assertEquals(bucket.getBaseTokens(), 0),
                () -> assertEquals(bucket.getExtraTokens(), 2)
        );
    }
}