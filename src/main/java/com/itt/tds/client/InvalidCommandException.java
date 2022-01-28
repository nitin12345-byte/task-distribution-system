/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itt.tds.client;

public class InvalidCommandException extends Exception {

    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException() {
        super("!Command is invalid please check the command once.");
    }
}
