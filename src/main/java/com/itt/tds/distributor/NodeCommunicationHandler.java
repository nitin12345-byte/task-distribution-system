package com.itt.tds.distributor;

import com.itt.tds.core.enums.NodeStatus;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.RequestSender;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.enums.TaskStatus;
import com.itt.tds.core.config.TDSConfiguration;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import com.itt.tds.core.model.Node;
import com.itt.tds.core.model.Task;
import com.itt.tds.core.model.TaskResult;
import com.itt.tds.distributor.db.DBManager;
import com.itt.tds.distributor.db.dao.NodeDAO;
import com.itt.tds.distributor.db.dao.TaskDAO;
import com.itt.tds.distributor.db.dao.TaskResultDAO;
import com.itt.tds.distributor.db.exceptions.DBException;
import java.io.IOException;
import java.sql.Connection;

class NodeCommunicationHandler implements Runnable {

    private Node node;
    private Task task;
    private Connection dbConnection;
    private TaskDAO taskDAO;
    private NodeDAO nodeDAO;
    private TaskResultDAO taskResultDAO;
    private Logger logger;

    public NodeCommunicationHandler(Task task, Node node) {
        this.node = node;
        this.task = task;
        dbConnection = DBManager.getInstance().createConnection();
        taskDAO = new TaskDAO(dbConnection);
        nodeDAO = new NodeDAO(dbConnection);
        taskResultDAO = new TaskResultDAO(dbConnection);
        logger = LogManager.getLogger(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        try {

            TDSRequest tdsRequest = prepareRequest(node, task);
            taskDAO.updateStatus(task.getId(), TaskStatus.IN_EXECUTION.name());

            try {
                TDSResponse tdsResponse = RequestSender.sendRequest(tdsRequest);
                handleResponse(tdsResponse);
            } catch (IOException exception) {
                logger.logError("run", "Connection with node intrrupted");
                Queue.getInstance().put(task);
                taskDAO.updateStatus(task.getId(), TaskStatus.QUEUE.name());
                nodeDAO.updateStatus(node.getId(), NodeStatus.NOT_OPERATIONAL.name());

            }
        } catch (DBException | ClassNotFoundException | InterruptedException exp) {
            logger.logError("run", "Unknown error occured", exp);
        }
    }

    private TDSRequest prepareRequest(Node node, Task task) {
        TDSRequest tdsRequest = new TDSRequest();
        tdsRequest.setParameter(Constants.TASK_ID, task.getId());
        tdsRequest.setParameter(Constants.DATA, task.getData());
        tdsRequest.setParameter(Constants.FILE_PATH, task.getFilePath());
        tdsRequest.setDestinationPort(node.getPortNumber());
        tdsRequest.setDestinationIp(node.getIpAddress());
        return tdsRequest;
    }

    private void handleResponse(TDSResponse tdsResponse) throws DBException {
        logger.logInfo("handleResponse", "Handling the response form the node");
        TaskResult taskResult = new TaskResult();
        taskResult.setTaskId(task.getId());

        if (tdsResponse.getStatus() == 1) {

            if (tdsResponse.getValue(Constants.ERROR_CODE) == null) {
                String result = (String) tdsResponse.getValue(Constants.RESULT);
                if (result.length() > TDSConfiguration.RESULT_MAX_SIZE) {
                    result = result.substring(0, result.length());
                }

                taskResult.setResult(result);
            } else {
                String errorMessage = (String) tdsResponse.getValue(Constants.ERROR_MESSAGE);
                if (errorMessage.length() > TDSConfiguration.RESULT_MAX_SIZE) {
                    errorMessage = errorMessage.substring(0, errorMessage.length());
                }
                taskResult.setErrorMessage(errorMessage);
                int error_code = Double.valueOf((double) tdsResponse.getValue(Constants.ERROR_CODE)).intValue();
                taskResult.setErrorCode((error_code));
            }

        } else {

            taskResult.setErrorMessage(tdsResponse.getErrorMessage());
            logger.logError("handleResponse", tdsResponse.getErrorMessage());
        }

        taskDAO.updateStatus(task.getId(), TaskStatus.COMPLETED.name());
        nodeDAO.updateStatus(node.getId(), NodeStatus.AVAILABLE.name());
        taskResultDAO.save(taskResult);
        logger.logInfo("handleResponse", "Response from node handled successfuly");
    }
}
