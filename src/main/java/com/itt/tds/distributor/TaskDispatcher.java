package com.itt.tds.distributor;

import com.itt.tds.core.enums.NodeStatus;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import com.itt.tds.core.model.Node;
import com.itt.tds.core.model.Task;
import com.itt.tds.distributor.db.DBManager;
import com.itt.tds.distributor.db.dao.NodeDAO;
import com.itt.tds.distributor.db.dao.TaskDAO;
import com.itt.tds.distributor.db.exceptions.DBException;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;

public class TaskDispatcher implements Runnable {

    private Logger logger;

    public TaskDispatcher() {
        logger = LogManager.getLogger(this.getClass().getName());
    }

    @Override
    public void run() {

        Queue queue = Queue.getInstance();
        List<Node> nodeList;

        logger.logInfo("run", "TaskDispatcher is running");

        Connection dbConnection = DBManager.getInstance().createConnection();
        NodeDAO nodeDAO = new NodeDAO(dbConnection);
        TaskDAO taskDAO = new TaskDAO(dbConnection);

        while (true) {
            Task task;
            try {
                if ((task = (Task) queue.poll()) != null) {
                    nodeList = nodeDAO.getAllAvailableNodesFor(Utils.getCapabilityFromFileName(task.getFilePath()));
                    if (nodeList.size() > 0) {
                        Node node = nodeList.get(0);
                        taskDAO.updateNodeId(task.getId(), node.getId());
                        nodeDAO.updateStatus(node.getId(), NodeStatus.BUSY.name());
                        NodeCommunicationHandler nodeCommunicationHandler = new NodeCommunicationHandler(task, node);
                        Thread thread = new Thread(nodeCommunicationHandler);
                        thread.start();
                        logger.logInfo("run", String.format("Task(%s) is assigned to Node(%s)", task.getId(), node.getId()));
                    } else {
                        queue.put(task);
                    }
                }
            } catch (DBException exception) {
                logger.logError("run", "Database error occured", exception);
            } catch (InterruptedException ex) {
                logger.logError("run", "UnknowError");
            }
        }
    }
}
