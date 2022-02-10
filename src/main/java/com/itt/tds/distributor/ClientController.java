package com.itt.tds.distributor;

import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.enums.ResponseStatus;
import com.itt.tds.core.enums.TaskStatus;
import com.itt.tds.core.enums.TDSResponseErrorCode;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import com.itt.tds.core.model.Client;
import com.itt.tds.core.model.Node;
import com.itt.tds.core.model.Task;
import com.itt.tds.core.model.TaskResult;
import com.itt.tds.distributor.db.DBManager;
import com.itt.tds.distributor.db.dao.ClientDAO;
import com.itt.tds.distributor.db.dao.NodeDAO;
import com.itt.tds.distributor.db.dao.TaskDAO;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordNotFoundException;
import java.sql.Connection;
import java.util.List;

public class ClientController implements TDSController {

    private Connection dbConnection;
    private Logger logger;

    public ClientController() {
        logger = LogManager.getLogger(this.getClass().getName());
    }

    @Override
    public TDSResponse processRequest(TDSRequest tdsRequest) {
        logger.logInfo("precessRequest", "client controller is proccessing the request");
        String method = tdsRequest.getMethod();
        dbConnection = DBManager.getInstance().createConnection();

        switch (method) {

            case Constants.CLIENT_REGISTER -> {
                return registerClient(tdsRequest);
            }

            case Constants.CLIENT_UNREGISTER -> {
                return unregisterClient(tdsRequest);
            }

            case Constants.CLIENT_ADD_TASK -> {
                return addTask(tdsRequest);
            }

            case Constants.CLIENT_TASKS -> {
                return getAlltasks(tdsRequest);
            }

            case Constants.CLIENT_TASK_RESULT -> {
                return getTaskResult(tdsRequest);
            }

            case Constants.CLIENT_TASK_STATUS -> {
                return getTaskStatus(tdsRequest);
            }

            default -> {
                return new TDSResponse();
            }
        }

    }

    private TDSResponse registerClient(TDSRequest tdsRequest) {
        logger.logInfo("registerClient", "Controller is registering the client");
        TDSResponse tdsResponse = new TDSResponse();
        Client client = new Client();
        client.setHostName((String) tdsRequest.getParameter(Constants.HOST_NAME));
        client.setIpAddress((String) tdsRequest.getParameter(Constants.IP_ADDRESS));
        ClientDAO clientDAO = new ClientDAO(dbConnection);
        try {
            String clientId = clientDAO.save(client);
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
            tdsResponse.setValue(Constants.CLIENT_ID, clientId);
            logger.logInfo("registerClient", "Client is registered successfully");

        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
            logger.logInfo("registerClient", "Error occured while registering cliend", exception);
        }
        return tdsResponse;
    }

    private TDSResponse unregisterClient(TDSRequest tdsRequest) {
        TDSResponse tdsResponse = new TDSResponse();
        String clientId = (String) tdsRequest.getParameter(Constants.CLIENT_ID);
        ClientDAO clientDAO = new ClientDAO(dbConnection);
        try {
            clientDAO.delete(clientId);
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
        }
        return tdsResponse;
    }

    private TDSResponse addTask(TDSRequest tdsRequest) {
        logger.logInfo("addTask", "Task is getting added in the database");
        TDSResponse tdsResponse = new TDSResponse();

        Task task = new Task();
        task.setClientId((String) tdsRequest.getParameter(Constants.CLIENT_ID));
        task.setFilePath((String) tdsRequest.getParameter(Constants.FILE_PATH));
        task.setStatus(TaskStatus.PENDING);
        task.setData((String) tdsRequest.getParameter(Constants.DATA));
        TaskDAO taskDAO = new TaskDAO(dbConnection);
        NodeDAO nodeDAO = new NodeDAO(dbConnection);

        try {

            String capability = Utils.getCapabilityFromFileName(task.getFilePath());
            if (!capability.isEmpty()) {
                List<Node> nodeList = nodeDAO.getAllAvailableOrBusyNodesFor(capability);
                if (nodeList.size() > 0) {
                    String taskId = taskDAO.save(task);
                    task.setId(taskId);
                    Queue queue = Queue.getInstance();
                    queue.put(task);
                    taskDAO.updateStatus(taskId, TaskStatus.QUEUE.name());
                    tdsResponse.setStatus(ResponseStatus.OK.getValue());
                    tdsResponse.setValue(Constants.TASK_ID, taskId);
                    logger.logInfo("addTask", "Task has been added successfuly in the database");
                } else {
                    tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
                    tdsResponse.setErrorCode(TDSResponseErrorCode.NODE_NOT_AVAILABLE.getValue());
                    tdsResponse.setErrorMessage("No node is available to execute the task");
                }
            } else {
                tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
                tdsResponse.setErrorCode(TDSResponseErrorCode.EXECUTOR_NOT_AVAILABLE.getValue());
                tdsResponse.setErrorMessage("System does not support the execution of this file");
            }
        } catch (DBException | InterruptedException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            logger.logError("addTask", "Unknow error occured", exception);

        }
        return tdsResponse;
    }

    private TDSResponse getAlltasks(TDSRequest tdsRequest) {
        TDSResponse tdsResponse = new TDSResponse();
        String clientId = (String) tdsRequest.getParameter(Constants.CLIENT_ID);
        TaskDAO taskDAO = new TaskDAO(dbConnection);
        try {
            List<Task> taskList = taskDAO.getAllTask(clientId);
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
            tdsResponse.setValue(Constants.TASKS, taskList);
        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
        }
        return tdsResponse;
    }

    private TDSResponse getTaskResult(TDSRequest tdsRequest) {
        logger.logInfo("getTaskResult", "System is retriving result from the database");
        TDSResponse tdsResponse = new TDSResponse();
        String taskId = (String) tdsRequest.getParameter(Constants.TASK_ID);
        TaskDAO taskDAO = new TaskDAO(dbConnection);
        try {
            Task task = taskDAO.getTask(taskId);
            TaskResult taskResult = task.getResult();
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
            tdsResponse.setValue(Constants.TASK_ID, taskResult.getTaskId());
            tdsResponse.setValue(Constants.ERROR_CODE, taskResult.getErrorCode());
            tdsResponse.setValue(Constants.ERROR_MESSAGE, taskResult.getErrorMessage());
            tdsResponse.setValue(Constants.RESULT, taskResult.getResult());
            logger.logInfo("getTaskResult", "result retrived successfully from database");

        } catch (RecordNotFoundException exception) {
            System.out.print(exception);
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
            tdsResponse.setErrorCode(TDSResponseErrorCode.RECORD_NOT_AVAILABLE.getValue());
            logger.logInfo("getTaskResult", "The requested record is not found");

        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            logger.logError("getTaskResult", "Unknown error occured", exception);
        }
        return tdsResponse;
    }

    private TDSResponse getTaskStatus(TDSRequest tdsRequest) {
        logger.logInfo("getTaskStatus", "Retriving status of requested task");
        TDSResponse tdsResponse = new TDSResponse();
        String taskId = (String) tdsRequest.getParameter(Constants.TASK_ID);
        TaskDAO taskDAO = new TaskDAO(dbConnection);
        try {
            Task task = taskDAO.getTask(taskId);
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
            tdsResponse.setValue(Constants.STATUS, task.getStatus().name());
            logger.logInfo("getTaskSatus", "task status retrived successfully");
        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            logger.logError("getTaskStatus", "Database error occured", exception);

        }
        return tdsResponse;
    }

}
