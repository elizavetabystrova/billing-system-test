package ru.learnhub.commondto.dto;

import java.util.HashMap;
import java.util.Map;

public enum CallType {
    INCOMING("02"),
    OUTGOING("01");

    private static Map<String, CallType> callMap = new HashMap<>();

    static {
        for (CallType callType : values()) {
            callMap.put(callType.index, callType);
        }
    }

    private final String index;

    CallType(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }

    public static CallType getInstanceByIndex(String index) {
        if (callMap.containsKey(index)) {
            return callMap.get(index);
        }
        return null;
    }
}

