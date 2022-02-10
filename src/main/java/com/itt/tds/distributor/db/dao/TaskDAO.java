package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.Constants;
import com.itt.tds.core.enums.TaskStatus;
import com.itt.tds.core.model.Task;
import com.itt.tds.core.model.TaskResult;
import com.itt.tds.distributor.db.exceptions.DBConnectionException;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordAlreadyExistException;
import com.itt.tds.distributor.db.exceptions.RecordNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskDAO implements ITaskDAO {

    private Connection dbConnection;

    public TaskDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public String save(Task task) throws RecordAlreadyExistException, DBException {

        String id = Constants.EMPTY_STRING;

        try {
            String query = "INSERT INTO task(id,client_id,status,file_path) VALUES(?,?,?,?)";

            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            UUID uuid = UUID.randomUUID();
            id = uuid.toString();

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, task.getClientId());
            preparedStatement.setString(3, task.getStatus().name());
            preparedStatement.setString(4, task.getFilePath());
            preparedStatement.execute();
        } catch (SQLIntegrityConstraintViolationException exception) {
            throw new RecordAlreadyExistException();
        } catch (SQLException exception) {
            throw new DBException();
        }
        return id;
    }

    @Override
    public void delete(String id) throws DBException {
        String query = "DELETE FROM task WHERE id = ? ";
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new DBException();
        }

    }

    @Override
    public Task getTask(String id) throws RecordNotFoundException, DBException {

        String query = "SELECT task.id,task.client_id,task.node_id,task.status,"
                + "task.file_path,task_result.error_code,task_result.error_message,"
                + "task_result.result FROM `task` INNER JOIN task_result "
                + "ON task.id = task_result.task_id WHERE task.id = ?";

        Task task = new Task();
        TaskResult taskResult = new TaskResult();

        try {
            PreparedStatement preparedStatement;
            preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                task.setId(resultSet.getString(Constants.ID));
                task.setClientId(resultSet.getString(Constants.CLIENT_ID));
                task.setNodeId(resultSet.getString(Constants.NODE_ID));
                task.setFilePath(resultSet.getString(Constants.FILE_PATH));
                task.setStatus(TaskStatus.valueOf(resultSet.getString(Constants.STATUS).toUpperCase()));
                taskResult.setTaskId(resultSet.getString(Constants.ID));
                taskResult.setErrorMessage(resultSet.getString(Constants.ERROR_MESSAGE));
                taskResult.setErrorCode(resultSet.getInt(Constants.ERROR_CODE));
                taskResult.setResult(resultSet.getString(Constants.RESULT));
                task.setResult(taskResult);
            } else {
                throw new RecordNotFoundException();
            }
        } catch (SQLException exception) {
            throw new DBException();
        }

        return task;
    }

    @Override
    public void updateStatus(String id, String status) throws DBConnectionException, DBException {
        String query = "UPDATE task SET status = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, id);
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new DBException();
        }
    }

    public void updateNodeId(String id, String nodeId) throws DBException {
        String query = "UPDATE task SET node_id = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, nodeId);
            preparedStatement.setString(2, id);
            preparedStatement.execute();

        } catch (SQLException exception) {
            throw new DBException();
        }
    }

    @Override
    public List<Task> getAllTask(String clientId) throws DBException {

        ArrayList<Task> taskList = new ArrayList<>();
        try {
            String query = "SELECT * FROM task WHERE client_id = ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Task task = new Task();
                task.setId(resultSet.getString(Constants.ID));
                task.setFilePath(resultSet.getString(Constants.FILE_PATH));
                task.setNodeId(resultSet.getString(Constants.NODE_ID));
                task.setStatus(TaskStatus.valueOf(resultSet.getString(Constants.STATUS)));
                task.setDateTime(resultSet.getString(Constants.DATE_TIME));
                taskList.add(task);

            }
        } catch (SQLException exception) {
            throw new DBException();
        }

        return taskList;
    }
}
