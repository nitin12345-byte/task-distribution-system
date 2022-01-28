/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itt.tds.distributor;

/**
 *
 * @author nitin.jangid
 */
public class Utils {

    public static String getCapabilityFromFileName(String fileName) {
        if (fileName.endsWith(".java")) {
            return "JAVA";
        } else if (fileName.endsWith(".c")) {
            return "C";
        } else if (fileName.endsWith(".cpp")) {
            return "CPP";
        } else if (fileName.endsWith(".py")) {
            return "PYTHON";
        }
        return "";
    }
}
