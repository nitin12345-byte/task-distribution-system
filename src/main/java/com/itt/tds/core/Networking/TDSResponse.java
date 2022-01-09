package com.itt.tds.core.Networking;

import java.io.Serializable;

public class TDSResponse extends TDSProtocol implements Serializable {

    private int status;
    private int errorCode;
    private String errorMessage;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getValue(String key) {
        return getHeader(key);
    }

    public void setValue(String key, String value) {
        setHeader(key, value);
    }
}
