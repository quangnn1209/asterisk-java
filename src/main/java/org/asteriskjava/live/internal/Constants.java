package org.asteriskjava.live.internal;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.asteriskjava.pbx.internal.managerAPI.OriginateBaseClass;

import java.util.concurrent.TimeUnit;

/**
 * Defines constants used internally in the live package in multiple classes.
 *
 * @author srt
 * @version $Id$
 */
public class Constants {
    private static final Cache<String, OriginateBaseClass> CALL_ORIGINATE_BASE = CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build();

    public static void putCall(String callUUID, OriginateBaseClass obj) {
        CALL_ORIGINATE_BASE.put(callUUID, obj);
    }

    public static OriginateBaseClass getCall(String callUUID) {
        return CALL_ORIGINATE_BASE.getIfPresent(callUUID);
    }

    // hide constructor
    private Constants() {

    }

    static final String VARIABLE_TRACE_ID = "AJ_TRACE_ID";
    public static String CALL_UUID = "Call-UUID";
}
