package com.itt.tds.core.Networking;

import java.io.Serializable;

public class TDSRequest extends TDSProtocol implements Serializable {

    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameter(String key) {
        return getHeader(key);
    }

    public void setParameter(String key, String value) {
        setHeader(key, value);
    }

}
