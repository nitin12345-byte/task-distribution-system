package com.itt.tds.node;

import com.itt.tds.client.*;
import com.itt.tds.core.model.TDSDistributorConfiguration;

/**
 *
 * @author nitin.jangid
 */
public class ConfigurationCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String[] parameters) throws InvalidCommandException {

        TDSDistributorConfigurationFileProcessor fileProcessor = new TDSDistributorConfigurationFileProcessor();

        if (parameters.length == 0) {
            Utils.showMessage("IP Address : " + fileProcessor.read().getDistributorIpAddress());
            Utils.showMessage("Port Number : " + fileProcessor.read().getDistributorPortNumber());
        } else if (parameters[0].equalsIgnoreCase("ip") && parameters.length == 2) {
            try {
                TDSDistributorConfiguration tdsClientConfiguration = fileProcessor.read();
                String ipAddress = parameters[1];
                tdsClientConfiguration.setDistributorIpAddress(ipAddress);
                fileProcessor.write(tdsClientConfiguration);
            } catch (Exception exception) {
                throw new InvalidCommandException();
            }
        } else if (parameters[0].equalsIgnoreCase("port") && parameters.length == 2) {
            try {
                TDSDistributorConfiguration tdsClientConfiguration = fileProcessor.read();
                int portNumber = Integer.parseInt(parameters[1]);
                tdsClientConfiguration.setDistributorPortNumber(portNumber);
                fileProcessor.write(tdsClientConfiguration);
            } catch (Exception exception) {
                throw new InvalidCommandException();
            }
        } else {
            throw new InvalidCommandException();
        }

    }
}
