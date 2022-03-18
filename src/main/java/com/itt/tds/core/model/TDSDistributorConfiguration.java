package com.itt.tds.core.model;

import com.itt.tds.core.Constants;
import java.io.Serializable;

public class TDSDistributorConfiguration implements Serializable {

    private String distributorIpAddress;
    private int distributorPortNumber;

    public TDSDistributorConfiguration() {
        distributorIpAddress = Constants.EMPTY_STRING;
        distributorPortNumber = 40;
    }

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
