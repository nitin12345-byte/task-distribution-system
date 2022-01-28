/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itt.tds.node;

import java.io.IOException;

/**
 *
 * @author nitin.jangid
 */
public class EnvironmentChecker {

    public boolean checkEnvironmentFor(String capability) {

        switch (capability) {

            case "C" -> {
                return checkCEnvironment();
            }
            case "CPP" -> {
                return checkCPPEnvironment();
            }
            case "JAVA" -> {
                return checkJavaEnvironment();
            }
            case "PYTHON" -> {
                return checkPythonEnvironment();
            }

            default -> {
                return false;
            }
        }
    }

    private boolean checkCEnvironment() {
        try {
            Runtime.getRuntime().exec("gcc -verison");
            return true;
        } catch (IOException ex) {
        }
        return false;
    }

    private boolean checkJavaEnvironment() {
        try {
            Runtime.getRuntime().exec("java -version");
            return true;
        } catch (IOException ex) {
        }
        return false;
    }

    private boolean checkCPPEnvironment() {
        try {
            Runtime.getRuntime().exec("gcc -verison");
            return true;
        } catch (IOException ex) {
        }
        return false;

    }

    private boolean checkPythonEnvironment() {
        try {
            Runtime.getRuntime().exec("python -version");
            return true;
        } catch (IOException ex) {
        }
        return false;

    }

}
