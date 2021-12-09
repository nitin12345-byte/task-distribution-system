package com.itt.tds.db.dao;

import com.itt.tds.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements IUserDAO {

    private Connection dbConnection;

    public UserDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(User user) {
        String query = "insert into user(user_name,password,ip_address,status) values(?,?,?,?)";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getIpAddress());
            preparedStatement.setString(4, user.getStatus());
            preparedStatement.execute();

        } catch (SQLException exp) {
            System.out.print(exp.getMessage());
        }

    }

    @Override
    public void delete(long id) {
        String query = "delete from user where id = ? ";
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException exp) {
            System.out.print(exp);
        }
    }

    @Override
    public User getUser(long id) {

        String query = "select * from user where id = ? ";
        User user = null;

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUserName(resultSet.getString("user_name"));
            user.setPassword(resultSet.getString("password"));
            user.setIpAddress(resultSet.getString("ip_address"));
            user.setStatus(resultSet.getString("status"));

        } catch (SQLException exp) {
            System.out.print(exp);
        }

        return user;
    }
    
    @Override
    public void updateStatus(long id, String status){
        
    }
}
