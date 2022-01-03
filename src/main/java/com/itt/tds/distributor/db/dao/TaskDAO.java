package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO implements ITaskDAO {

    private Connection dbConnection;

    public TaskDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(Task task) throws SQLException {

        String query = "INSERT INTO task(client_id,node_id,status,result,file_path) VALUES(?,?,?,?,?)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setLong(1, task.getClientId());
        preparedStatement.setLong(2, task.getNodeId());
        preparedStatement.setString(4, task.getResult());
        preparedStatement.setString(3, task.getStatus());
        preparedStatement.setString(5, task.getFilePath());
        preparedStatement.execute();

    }

    @Override
    public void delete(long id) throws SQLException {
        String query = "DELETE FROM task WHERE id = ? ";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
    }

    @Override
    public Task getTask(long id) throws SQLException {

        String query = "SELECT * FROM task WHERE id = ? ";
        Task task = new Task();

        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            task.setId(resultSet.getLong("id"));
            task.setClientId(resultSet.getLong("client_id"));
            task.setNodeId(resultSet.getLong("node_id"));
            task.setFilePath(resultSet.getString("file_path"));
            task.setResult(resultSet.getString("result"));
            task.setStatus(resultSet.getString("status"));
        }

        return task;
    }

    @Override
    public void updateStatus(long id, String status) throws SQLException {
        String query = "UPDATE task SET status = ? WHERE id = ?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, status);
        preparedStatement.setLong(2, id);
        preparedStatement.execute();
    }

    @Override
    public List<Task> getAllTask() throws SQLException {

        ArrayList<Task> taskList = new ArrayList<>();
        String query = "SELECT * FROM task";
        Statement stm = dbConnection.createStatement();
        ResultSet resultSet = stm.executeQuery(query);

        while (resultSet.next()) {

            Task task = new Task();
            task.setId(resultSet.getLong("id"));
            task.setFilePath(resultSet.getString("file_path"));
            task.setNodeId(resultSet.getLong("node_id"));
            task.setResult(resultSet.getString("result"));
            task.setStatus(resultSet.getString("status"));
            taskList.add(task);

        }

        return taskList;
    }
}
