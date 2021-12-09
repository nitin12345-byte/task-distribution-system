package com.itt.tds.db.dao;

import com.itt.tds.model.Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskDAO implements ITaskDAO {

    private Connection dbConnection;

    public TaskDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(Task task) {

        String query = "insert into task(user_id,node_id,status,result,file_path) values(?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setInt(1, task.getUserId());
            preparedStatement.setInt(2, task.getNodeId());
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
        String query = "delete from task where id = ? ";
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

        String query = "Select * from task where id = ? ";
        Task task = null;

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            task = new Task();
            task.setId(resultSet.getLong("id"));
            task.setUserId(resultSet.getInt("user_id"));
            task.setNodeId(resultSet.getInt("node_id"));
            task.setFilePath(resultSet.getString("file_path"));
            task.setResult(resultSet.getString("result"));
            task.setStatus(resultSet.getString("status"));

        } catch (SQLException exp) {
            System.out.print(exp);
        }

        return task;
    }

    @Override
    public void updateStatus(long id, String status) {
        String query = "update table task set status = ? where id = ?";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }

    }
}
