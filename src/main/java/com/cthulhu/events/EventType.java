package com.cthulhu.events;

public enum EventType {
    ROLL, USE_LUCK, DEVELOP, PUSH, //client events
    ROLL_RESULT, USE_LUCK_RESULT, DEVELOP_RESULT, PUSH_RESULT //server events
}
