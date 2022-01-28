package com.itt.tds.distributor;

import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import com.itt.tds.distributor.db.DBManager;
import java.io.IOException;
import java.sql.Connection;

public class Main {

    public static void main(String args[]) throws IOException {

        Connection dbConnection = DBManager.getInstance().createConnection();

        Logger logger = LogManager.getLogger(Main.class.getName());

        if (dbConnection != null) {

            logger.logInfo("main", "Distributor is started");

            Thread serverThread = new Thread(new TDSServer());
            serverThread.start();

            Thread taskDispatcherThread = new Thread(new TaskDispatcher());
            taskDispatcherThread.start();
        } else {
            logger.logInfo("main", "The database is not started please start the database");
        }
    }
}
