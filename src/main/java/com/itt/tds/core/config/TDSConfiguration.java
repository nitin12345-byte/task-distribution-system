package com.itt.tds.core.config;

public class TDSConfiguration {
    public static final String DB_STRING = "jdbc:mysql://localhost:3306/TDS";
    public static final String DISTRIBUTOR_IP_ADDRESS = "localhost";
    public static final int DISTRIBUTOR_PORT_NUMBER = 40;
    public static final int NODE_PORT = 50;
    public static final String SERIALIZATION_FORMAT = "Json";
    public static final int RESULT_MAX_SIZE = 200;
    public static final int ERROR_MESSAGE_MAX_SIZE = 200;
    public static final String DB_DRIVER_STRING = "com.mysql.cj.jdbc.Driver";
}
