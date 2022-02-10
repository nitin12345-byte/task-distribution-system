package com.itt.tds.client;

import com.itt.tds.core.Constants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClientIdFileProcessor {

    private final String CLIENT_KEY_FILE_PATH = "c:\\tdsclient\\key.txt";
    private final String CLIENT_DIR = "c:\\tdsclient";

    public String read() {
        String clientKey = Constants.EMPTY_STRING;
        File keyFile = new File(CLIENT_KEY_FILE_PATH);
        try ( DataInputStream dataInputStream = new DataInputStream(new FileInputStream(keyFile))) {
            clientKey = dataInputStream.readUTF();
        } catch (IOException ex) {
        }

        return clientKey;
    }

    public boolean write(String key) {
        File tdsClientDir = new File(CLIENT_DIR);
        if (!tdsClientDir.exists()) {
            tdsClientDir.mkdir();
        }
        File keyFile = new File(CLIENT_KEY_FILE_PATH);
        try ( DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(keyFile))) {
            dataOutput.writeUTF(key);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean delete() {
        File keyFile = new File(CLIENT_KEY_FILE_PATH);
        return keyFile.delete();
    }
}
