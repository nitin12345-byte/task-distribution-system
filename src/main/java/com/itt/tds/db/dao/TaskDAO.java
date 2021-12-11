package com.itt.tds.db.dao;

import com.itt.tds.model.Task;
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
    public void save(Task task) {

        String query = "INSERT INTO task(client_id,node_id,status,result,file_path) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, task.getClientId());
            preparedStatement.setLong(2, task.getNodeId());
            preparedStatement.setString(4, task.getResult());
            preparedStatement.setString(3, task.getStatus());
            preparedStatement.setString(5, task.getFilePath());
            preparedStatement.execute();

        } catch (SQLException exp) {
            System.out.print(exp.getMessage());
        }

    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM task WHERE id = ? ";
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException exp) {
            System.out.print(exp);
        }
    }

    @Override
    public Task getTask(long id) {

        String query = "SELECT * FROM task WHERE id = ? ";
        Task task = new Task();

        try {
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
        } catch (SQLException exp) {
            System.out.print(exp);
        }

        return task;
    }

    @Override
    public void updateStatus(long id, String status) {
        String query = "UPDATE task SET status = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }

    }

    @Override
    public List<Task> getAllTask() {

        ArrayList<Task> taskList = new ArrayList<>();

        try {
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

        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }
        return taskList;
    }
}
