package com.itt.tds.distributor;

import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import com.itt.tds.distributor.db.DBManager;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;

public class Main {

    public static void main(String args[]) throws IOException {

        Connection dbConnection = DBManager.getInstance().createConnection();

        Logger logger = LogManager.getLogger(Main.class.getName());

        if (dbConnection != null) {

            // MysqlScriptRunner scriptRunner = new MysqlScriptRunner(dbConnection);
            // scriptRunner.runScript();
            logger.logInfo("main", "Distributor is started");
            InetAddress inetAddress = InetAddress.getLocalHost();
            logger.logInfo("Main", inetAddress.getHostAddress());
            Thread serverThread = new Thread(new TDSServer());
            serverThread.start();

            Thread taskDispatcherThread = new Thread(new TaskDispatcher());
            taskDispatcherThread.start();
        } else {
            logger.logInfo("main", "For using TDSDistributor mysql should be installed and mysql server should be running ");
        }
    }
}
