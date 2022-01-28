package com.itt.tds.core.Networking;

import java.util.HashMap;

public class TDSRequest extends TDSProtocol {

    private String method;
    private HashMap<String, Object> parameters;

    public TDSRequest() {
        parameters = new HashMap<>();
        setProtocolType("request");
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }
}
