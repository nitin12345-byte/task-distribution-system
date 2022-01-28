package com.itt.tds.core.Networking;

import java.util.HashMap;

public class TDSResponse extends TDSProtocol {

    private int status;
    private int errorCode;
    private String errorMessage;
    private HashMap<String, Object> values;

    public TDSResponse(){
        values = new HashMap();
        setProtocolType("response");
    }
    
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

    public Object getValue(String key) {
        return values.get(key);
    }

    public void setValue(String key, Object value) {
        values.put(key, value);
    }
}
