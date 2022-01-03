package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Client;
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
    public void save(Client client) throws SQLException {
        String query = "INSERT INTO client(client_name,ip_address,port_number) VALUES(?,?,?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, client.getUserName());
        preparedStatement.setString(2, client.getIpAddress());
        preparedStatement.setInt(3, client.getPortNumber());
        preparedStatement.execute();

    }

    @Override
    public void delete(long id) throws SQLException {
        String query = "DELETE FROM client WHERE id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
    }

    @Override
    public Client getClient(long id) throws SQLException {

        String query = "SELECT * FROM client WHERE id = ? ";
        Client client = new Client();

        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            client.setId(resultSet.getLong("id"));
            client.setUserName(resultSet.getString("client_name"));
            client.setIpAddress(resultSet.getString("ip_address"));
            client.setPortNumber(resultSet.getInt("port_number"));
        }

        return client;
    }

    @Override
    public List<Client> getAllClients() throws SQLException {

        ArrayList<Client> clientList = new ArrayList<>();
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

        return clientList;
    }
}
