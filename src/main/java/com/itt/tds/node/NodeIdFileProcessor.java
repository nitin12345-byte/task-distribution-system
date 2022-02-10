package com.itt.tds.node;

import com.itt.tds.core.Constants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NodeIdFileProcessor {

    private final String NODE_KEY_FILE_PATH = "c:\\ExecutorNode\\key.txt";
    private final String NODE_DIR = "c:\\ExecutorNode";

    public String read() {
        String nodeKey = Constants.EMPTY_STRING;
        File keyFile = new File(NODE_KEY_FILE_PATH);
        try ( DataInputStream dataInputStream = new DataInputStream(new FileInputStream(keyFile))) {
            nodeKey = dataInputStream.readUTF();
        } catch (IOException ex) {
        }
        return nodeKey;
    }

    public boolean write(String key) {
        try {
            File tdsClientDir = new File(NODE_DIR);
            if (!tdsClientDir.exists()) {
                tdsClientDir.mkdir();
            }
            File keyFile = new File(NODE_KEY_FILE_PATH);
            DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(keyFile));
            dataOutput.writeUTF(key);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean delete() {
        File nodeDir = new File(NODE_DIR);

        for (File file : nodeDir.listFiles()) {
            file.delete();
        }

        return nodeDir.delete();
    }
}
