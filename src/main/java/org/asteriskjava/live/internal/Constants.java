package org.asteriskjava.live.internal;

/**
 * Defines constants used internally in the live package in multiple classes.
 *
 * @author srt
 * @version $Id$
 */
public class Constants {
    // hide constructor
    private Constants() {

    }

    static final String VARIABLE_TRACE_ID = "AJ_TRACE_ID";
    public static String CALL_UUID = "Call-UUID";
    public static String GCP_PROJECT_ID = "glassy-augury-248712";
    public static String CALL_RECORDING = "Call-Recording";

    public static boolean is1stRvm(String callUUID) {
        return callUUID != null && callUUID.startsWith("RVM") && !callUUID.startsWith("RVM2");
    }

    public static boolean is2ndRvm(String callUUID) {
        return callUUID != null && callUUID.startsWith("RVM2");
    }

    public static String getRvm1stId(String callUUID) {
        return callUUID.replace("RVM2", "RVM");
    }

    public static String getRvm2ndId(String callUUID) {
        return callUUID.replace("RVM", "RVM2");
    }
}
