package com.itt.tds.core.logging;

import java.text.DateFormat;
import java.util.Date;

public class Logger {

    private String className;

    public Logger(String className) {
        this.className = className;
    }

    public void logError(String methodName, String message) {
        System.out.println(getFormattedString("ERROR", methodName, message));
    }

    public void logError(String methodName, String message, Exception exception) {
        System.out.println(getFormattedString("ERROR", methodName, message));
        exception.printStackTrace();
    }

    public void logInfo(String methodName, String message) {
        System.out.println(getFormattedString("INFO", methodName, message));
    }

    public void logInfo(String methodName, String message, Exception exception) {
        System.out.println(getFormattedString("INFO", methodName, message));
        exception.printStackTrace();
    }

    public void logWarn(String methodName, String message) {
        System.out.println(getFormattedString("WARN", methodName, message));
    }

    public void logWarn(String methodName, String message, Exception exception) {
        System.out.println(getFormattedString("WARN", methodName, message));
        exception.printStackTrace();
    }

    private String getFormattedString(String severity, String methodName, String message) {
        Date date = new Date();
        String dateString;
        dateString = DateFormat.getInstance().format(date);
        return String.format("[%s][%s][%s][%s] : %s", severity, dateString, className, methodName, message);
    }
}
