package org.asteriskjava.live;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class OriginateFailureHelper {
    private static final BlockingQueue<OriginateFailure> callUUIDQueue = new LinkedBlockingQueue<>(1);

    public static void add(String callUUID, String reason) {
        callUUIDQueue.add(new OriginateFailure(callUUID, reason));
    }

    public static OriginateFailure poll() throws InterruptedException {
        return callUUIDQueue.poll(2, TimeUnit.SECONDS);
    }
}
