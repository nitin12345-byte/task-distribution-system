package com.itt.tds.core.enums;

public enum TaskResultErrorCode {
    COMPILE_TIME_ERROR(300), RUN_TIME_ERROR(301), EXECUTION_TIME_EXCEED_ERROR(302), EXECUTOR_NOT_AVAILABLE(303);

    private int value;

    public int getValue() {
        return value;
    }

    private TaskResultErrorCode(int code) {
        this.value = code;
    }

}
