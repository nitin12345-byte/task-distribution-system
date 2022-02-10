package com.itt.tds.core.enums;

public enum ResponseStatus {
    OK(1), FAIL(0);

    private int value;

    public int getValue() {
        return value;
    }

    ResponseStatus(int value) {
        this.value = value;
    }

}
