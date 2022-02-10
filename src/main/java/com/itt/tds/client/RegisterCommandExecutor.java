package com.itt.tds.client;

import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.enums.ResponseStatus;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author nitin.jangid
 */
public class RegisterCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String parameter) throws InvalidCommandException {
        String hostName;
        String ipAddress;

        if (parameter.isEmpty()) {

            if (!Utils.isClientRegistered()) {

                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    hostName = inetAddress.getHostName();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException ex) {
                    Utils.showMessage("Not able to retrive ip address of the system");
                    return;
                }

                try {
                    TDSRequest tdsRequest = new TDSRequest();
                    tdsRequest.setMethod(Constants.CLIENT_REGISTER);
                    tdsRequest.setDestinationPort(TDSConfiguration.DISTRIBUTOR_PORT_NUMBER);
                    tdsRequest.setDestinationIp(TDSConfiguration.DISTRIBUTOR_IP_ADDRESS);
                    tdsRequest.setParameter(Constants.IP_ADDRESS, ipAddress);
                    tdsRequest.setParameter(Constants.HOST_NAME, hostName);
                    TDSResponse response = RequestSender.sendRequest(tdsRequest);
                    if (response.getStatus() == ResponseStatus.OK.getValue()) {
                        new ClientIdFileProcessor().write((String) response.getValue(Constants.CLIENT_ID));
                        Utils.showMessage("client is registered successfully with ID : " + response.getValue(Constants.CLIENT_ID));
                    } else {
                        Utils.showMessage("Error Code : " + Double.valueOf((double) response.getErrorCode()).intValue());
                        Utils.showMessage("Error Message : " + response.getErrorMessage());
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    Utils.showMessage(exception.getMessage() + ", check server");
                }
            } else {
                Utils.showMessage("client is already registered");
            }
        } else {
            throw new InvalidCommandException();
        }
    }
}
