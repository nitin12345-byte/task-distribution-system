package com.itt.tds.core.Networking;

import com.google.gson.Gson;

public class JSONSerializer implements TDSSerializer {

    @Override
    public String serialize(TDSProtocol tdsProtocol) {

        Gson gson = new Gson();
        String jsonString = gson.toJson(tdsProtocol);
        return jsonString;
    }

    @Override
    public TDSProtocol deserialize(String protocolString) {
        Gson gson = new Gson();
        TDSProtocol tdsProtocol = gson.fromJson(protocolString, TDSProtocol.class);

        if (tdsProtocol.getProtocolType().equals("request")) {
            return gson.fromJson(protocolString, TDSRequest.class);
        }

        return gson.fromJson(protocolString, TDSResponse.class);
    }
}
