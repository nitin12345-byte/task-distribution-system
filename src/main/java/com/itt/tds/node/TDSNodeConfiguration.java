/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itt.tds.node;

import java.io.Serializable;

/**
 *
 * @author nitin.jangid
 */
public class TDSNodeConfiguration {
   public class TDSClientConfiguration implements Serializable{
    private String distributorIpAddress;
    private int distributorPortNumber;

    public String getDistributorIpAddress() {
        return distributorIpAddress;
    }

    public void setDistributorIpAddress(String distributorIpAddress) {
        this.distributorIpAddress = distributorIpAddress;
    }

    public int getDistributorPortNumber() {
        return distributorPortNumber;
    }

    public void setDistributorPortNumber(int distributorPortNumber) {
        this.distributorPortNumber = distributorPortNumber;
    }
  } 
}
