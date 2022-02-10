package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.Constants;
import com.itt.tds.core.model.Client;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordAlreadyExistException;
import com.itt.tds.distributor.db.exceptions.RecordNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientDAO implements IClientDAO {

    private Connection dbConnection;

    public ClientDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public String save(Client client) throws RecordAlreadyExistException, DBException {
        String query = "INSERT INTO client(id, host_name,ip_address) VALUES(?,?,?)";
        String id = Constants.EMPTY_STRING;
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            UUID uuid = UUID.randomUUID();
            id = uuid.toString();
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, client.getHostName());
            preparedStatement.setString(3, client.getIpAddress());
            preparedStatement.execute();
        } catch (SQLIntegrityConstraintViolationException xception) {
            throw new RecordAlreadyExistException();
        } catch (SQLException exception) {
            throw new DBException();
        }

        return id;
    }

    @Override
    public void delete(String id) throws DBException {
        String query = "DELETE FROM client WHERE id = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        } catch (SQLIntegrityConstraintViolationException xception) {
            throw new RecordAlreadyExistException();
        } catch (SQLException exception) {
            throw new DBException();
        }

    }

    @Override
    public Client getClient(String id) throws RecordNotFoundException, DBException {

        String query = "SELECT * FROM client WHERE id = ? ";
        Client client = new Client();

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                client.setId(resultSet.getString(Constants.ID));
                client.setHostName(resultSet.getString(Constants.HOST_NAME));
                client.setIpAddress(resultSet.getString(Constants.IP_ADDRESS));
            }
        } catch (SQLIntegrityConstraintViolationException xception) {
            throw new RecordAlreadyExistException();
        } catch (SQLException exception) {
            throw new DBException();
        }

        return client;
    }

    @Override
    public List<Client> getAllClients() throws DBException {

        ArrayList<Client> clientList = new ArrayList<>();
        try {
            String query = "SELECT * FROM client";
            Statement stm = dbConnection.createStatement();
            ResultSet resultSet = stm.executeQuery(query);

            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getString(Constants.ID));
                client.setIpAddress(resultSet.getString(Constants.IP_ADDRESS));
                client.setHostName(resultSet.getString(Constants.HOST_NAME));
                clientList.add(client);
            }
        } catch (SQLException exception) {
            throw new DBException();
        }

        return clientList;
    }
}
