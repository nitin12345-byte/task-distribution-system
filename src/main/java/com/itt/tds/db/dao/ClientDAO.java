package com.itt.tds.db.dao;

import com.itt.tds.model.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements IClientDAO {

    private Connection dbConnection;

    public ClientDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(Client client) {
        String query = "INSERT INTO client(client_name,ip_address,port_number) VALUES(?,?,?)";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, client.getUserName());
            preparedStatement.setString(2, client.getIpAddress());
            preparedStatement.setInt(3, client.getPortNumber());
            preparedStatement.execute();

        } catch (SQLException exp) {
            System.out.print(exp.getMessage());
        }

    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM client WHERE id = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException exp) {
            System.out.print(exp);
        }
    }

    @Override
    public Client getClient(long id) {

        String query = "SELECT * FROM client WHERE id = ? ";
        Client client = new Client();

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                client.setId(resultSet.getInt("id"));
                client.setUserName(resultSet.getString("client_name"));
                client.setIpAddress(resultSet.getString("ip_address"));
                client.setPortNumber(resultSet.getInt("port_number"));
            }
        } catch (SQLException exp) {
            System.out.print(exp);
        }

        return client;
    }

    @Override
    public List<Client> getAllClients() {

        ArrayList<Client> clientList = new ArrayList<>();

        try {
            String query = "SELECT * FROM client";
            Statement stm = dbConnection.createStatement();
            ResultSet resultSet = stm.executeQuery(query);

            while (resultSet.next()) {

                Client client = new Client();
                client.setId(resultSet.getLong("id"));
                client.setIpAddress(resultSet.getString("ip_address"));
                client.setPortNumber(resultSet.getInt("port_number"));
                client.setUserName(resultSet.getString("client_name"));
                clientList.add(client);

            }

        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }
        return clientList;
    }
}
