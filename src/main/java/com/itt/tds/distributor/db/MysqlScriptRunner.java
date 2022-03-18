package com.itt.tds.distributor.db;

import com.itt.tds.client.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import org.apache.ibatis.jdbc.ScriptRunner;

/**
 *
 * @author nitin.jangid
 */
public class MysqlScriptRunner {

    private Connection connection;

    public MysqlScriptRunner(Connection connection) {
        this.connection = connection;
    }

    public void runScript() {

        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setAutoCommit(true);
        scriptRunner.setLogWriter(null);
        scriptRunner.setErrorLogWriter(null);
        scriptRunner.setThrowWarning(false);

        Reader reader;

        try {
            File file = new File("target\\classes\\com\\itt\\tds\\distributor\\db\\tds.sql");
            reader = new BufferedReader(new FileReader(file));
            scriptRunner.runScript(reader);
        } catch (FileNotFoundException ex) {
            Utils.showMessage("Not able to locate the sql script file");
        }
    }
}
