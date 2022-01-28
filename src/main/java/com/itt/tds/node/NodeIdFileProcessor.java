package com.itt.tds.node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeIdFileProcessor {

    public String read() {

        String nodeKey = "";

        File keyFile = new File("c:\\ExecutorNode\\key.txt");
        try ( DataInputStream dataInputStream = new DataInputStream(new FileInputStream(keyFile))) {
            nodeKey = dataInputStream.readUTF();
        } catch (IOException ex) {
        }

        return nodeKey;
    }

    public boolean write(String key) {
        try {
            File tdsClientDir = new File("c:\\ExecutorNode");
            if (!tdsClientDir.exists()) {
                tdsClientDir.mkdir();
            }
            File keyFile = new File("c:\\ExecutorNode\\key.txt");
            DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(keyFile));
            dataOutput.writeUTF(key);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean delete() {
        File keyFile = new File("c:\\ExecutorNode\\key.txt");
        return keyFile.delete();
    }
}
