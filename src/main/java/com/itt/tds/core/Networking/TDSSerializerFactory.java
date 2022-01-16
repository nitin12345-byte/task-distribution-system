package com.itt.tds.core.Networking;

public class TDSSerializerFactory {

    public static TDSSerializer getSerializer(String serializerType) {

        TDSSerializer tdsSerializer = null;

        if (serializerType.equals("Json")) {
            return new JSONSerializer();
        }

        return tdsSerializer;

    }

}
