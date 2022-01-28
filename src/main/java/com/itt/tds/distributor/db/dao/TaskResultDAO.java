package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.TaskResult;
import com.itt.tds.distributor.db.exceptions.DBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskResultDAO implements ITaskResultDAO {

    private Connection dbConnection;

    public TaskResultDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(TaskResult taskResult) throws DBException {

        try {
            String query = "INSERT INTO task_result(task_id, error_code, error_message, result) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, taskResult.getTaskId());
            preparedStatement.setInt(2, taskResult.getErrorCode());
            preparedStatement.setString(3, taskResult.getErrorMessage());
            preparedStatement.setString(4, taskResult.getResult());

            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new DBException();
        }

    }

    @Override
    public void delete(String taskId) throws DBException {
        try {
            String query = "DELETE FROM task_result WHERE task_id = ? ";

            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, taskId);
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new DBException();
        }
    }

}
