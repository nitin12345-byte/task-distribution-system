package com.itt.tds.core.enums;

public enum TDSResponseErrorCode {
    NODE_NOT_AVAILABLE(100), RECORD_NOT_AVAILABLE(101), UNKNOWN_ERROR(102), DATABASE_ERROR(103), EXECUTOR_NOT_AVAILABLE(104), RECORD_ALREADY_EXIST(105);

    private int value;

    public int getValue() {
        return value;
    }

    private TDSResponseErrorCode(int code) {
        this.value = code;
    }

}
