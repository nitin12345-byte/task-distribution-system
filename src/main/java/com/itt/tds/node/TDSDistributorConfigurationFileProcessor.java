package com.itt.tds.node;

import com.itt.tds.core.model.TDSDistributorConfiguration;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author nitin.jangid
 */
public class TDSDistributorConfigurationFileProcessor {

    private final String DISTRIBUTOR_CONFIGURATION_FILE_PATH = "c:\\ExecutorNode\\configuration.json";

    public TDSDistributorConfiguration read() {
        TDSDistributorConfiguration tdsClientConfiguration = new TDSDistributorConfiguration();
        File file = new File(DISTRIBUTOR_CONFIGURATION_FILE_PATH);
        try {
            DataInputStream dataInput = new DataInputStream(new FileInputStream(file));
            String jsonString = dataInput.readUTF();
            tdsClientConfiguration = new Gson().fromJson(jsonString, TDSDistributorConfiguration.class);
        } catch (IOException ex) {

        }
        return tdsClientConfiguration;
    }

    public void write(TDSDistributorConfiguration tdsClientConfiguration) {

        File configFile = new File(DISTRIBUTOR_CONFIGURATION_FILE_PATH);
        File nodeDir = new File(configFile.getParent());
        if (!nodeDir.exists()) {
            nodeDir.mkdir();
        }

        try {
            DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(configFile));
            String jsonString = new Gson().toJson(tdsClientConfiguration);
            dataOutput.writeUTF(jsonString);
        } catch (IOException ex) {
        }
    }
}
