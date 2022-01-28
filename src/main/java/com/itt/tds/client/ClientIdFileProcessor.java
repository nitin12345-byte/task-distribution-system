package com.itt.tds.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClientIdFileProcessor {

    public String read() {

        String clientKey = "";

        File keyFile = new File("c:\\tdsclient\\key.txt");
        try ( DataInputStream dataInputStream = new DataInputStream(new FileInputStream(keyFile))) {
            clientKey = dataInputStream.readUTF();
        } catch (IOException ex) {
        }

        return clientKey;
    }

    public boolean write(String key) {

        File tdsClientDir = new File("c:\\tdsclient");
        if (!tdsClientDir.exists()) {
            tdsClientDir.mkdir();
        }
        File keyFile = new File("c:\\tdsClient\\key.txt");
        try ( DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(keyFile))) {
            dataOutput.writeUTF(key);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean delete() {
        File keyFile = new File("c:\\tdsclient\\key.txt");
        return keyFile.delete();
    }
}
