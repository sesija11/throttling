package com.throttling.task.access;

import com.throttling.task.access.interfaces.IBucket;
import com.throttling.task.access.interfaces.IBucketsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BucketsServiceTest {
    IBucketsService bucketsController;

    @BeforeEach
    @DisplayName("new BucketsController()")
    void setUp(){
        bucketsController = new BucketsService();
    }

    @Nested
    @DisplayName("after creating an element")
    class AfterCreating {

        @BeforeEach
        void create() {
            bucketsController.create("1");
            bucketsController.create("2");
        }

        @Test
        @DisplayName("buckets count check")
        void bucketCount() {
            assertEquals(bucketsController.bucketCount(), 2);
        }

        @Test
        @DisplayName("get bucket by id check")
        void get() {
            assertAll("one assert from group is invalid",
                    () -> assertNull(bucketsController.get("3")),
                    () -> assertNotNull(bucketsController.get("2"))
            );
        }

        @Test
        @DisplayName("contains bucket check")
        void contains() {
            assertAll("one assert from group is invalid",
                    () -> assertFalse(bucketsController.contains("3")),
                    () -> assertTrue(bucketsController.contains("2"))
            );
        }

        @Test
        @DisplayName("all buckets must get tokens")
        void addTokens() {
            IBucket bucket1 = bucketsController.get("1");
            bucket1.setMaxTokensCount(5);
            IBucket bucket2 = bucketsController.get("2");
            bucket2.setMaxTokensCount(5);

            bucketsController.addTokens(4);
            assertAll("one assert from group is invalid",
                    () -> assertEquals(bucket1.getBaseTokens(), 4),
                    () -> assertEquals(bucket2.getBaseTokens(), 4)
            );
        }

        @Test
        @DisplayName("buckets with a different session ID must be removed")
        void removeUnusedBuckets() {
            IBucket bucket1 = bucketsController.get("1");
            bucket1.setSessionID((short) 1);
            IBucket bucket2 = bucketsController.get("2");
            bucket2.setSessionID((short) 2);
            bucketsController.removeUnusedBuckets((short) 1);

            assertAll("one assert from group is invalid",
                    () -> assertNull(bucketsController.get("2")),
                    () -> assertNotNull(bucketsController.get("1"))
            );
        }
    }
}