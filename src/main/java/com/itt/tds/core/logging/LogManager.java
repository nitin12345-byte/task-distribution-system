package com.itt.tds.core.logging;

public class LogManager {

    public static Logger getLogger(String className) {
        return new Logger(className);
    }
}
