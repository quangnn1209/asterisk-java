package org.asteriskjava.live;

public class OriginateFailure {
    private final String callUUID;
    private final String reason;

    public OriginateFailure(String callUUID, String reason) {
        this.callUUID = callUUID;
        this.reason = reason;
    }

    public String getCallUUID() {
        return callUUID;
    }

    public String getReason() {
        return reason;
    }
}
