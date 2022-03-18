package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.Constants;
import com.itt.tds.core.model.Node;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordAlreadyExistException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NodeDAO implements INodeDAO {

    private Connection dbConnection;

    public NodeDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public String save(Node node) throws RecordAlreadyExistException, DBException {

        String id = Constants.EMPTY_STRING;
        try {
            String query = "INSERT INTO node(id,host_name,ip_address,port_number,status) VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);

            UUID uuid = UUID.randomUUID();
            id = uuid.toString();

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, node.getHostName());
            preparedStatement.setString(3, node.getIpAddress());
            preparedStatement.setInt(4, node.getPortNumber());
            preparedStatement.setString(5, node.getStatus());
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
        try {
            String query = "DELETE FROM node WHERE id = ? ";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new DBException();
        }
    }

    @Override
    public Node getNode(String id) throws DBException {

        Node node = new Node();

        try {
            String query = "SELECT * FROM node WHERE id = ? ";

            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                node.setId(resultSet.getString(Constants.ID));
                node.setHostName(resultSet.getString(Constants.HOST_NAME));
                node.setIpAddress(resultSet.getString(Constants.IP_ADDRESS));
                node.setPortNumber(resultSet.getInt(Constants.PORT_NUMBER));
                node.setStatus(resultSet.getString(Constants.STATUS));
            }

        } catch (SQLException exception) {
            throw new DBException();
        }

        return node;
    }

    @Override
    public void updateStatus(String id, String status) throws DBException {
        String query = "UPDATE node SET status = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, id);
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new DBException();
        }

    }

    public void updateIpAddress(String id, String ipAddress) throws DBException {
        String query = "UPDATE node SET ip_address = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, ipAddress);
            preparedStatement.setString(2, id);
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw new DBException();
        }

    }

    @Override
    public List<Node> getAllAvailableNodesFor(String capability) throws DBException {

        ArrayList<Node> nodeList = new ArrayList<>();

        try {
            String query = "SELECT node.id,node.host_name,node.status,node.port_number,"
                    + "node.ip_address,capability.name FROM node INNER "
                    + "JOIN capability ON node.status = 'AVAILABLE'"
                    + " AND capability.name = ?  AND capability.node_id = node.id";

            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, capability);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Node node = new Node();
                node.setId(resultSet.getString(Constants.ID));
                node.setIpAddress(resultSet.getString(Constants.IP_ADDRESS));
                node.setPortNumber(resultSet.getInt(Constants.PORT_NUMBER));
                node.setStatus(resultSet.getString(Constants.STATUS));
                node.setHostName(resultSet.getString(Constants.HOST_NAME));
                nodeList.add(node);
            }
        } catch (SQLException exception) {
            
            throw new DBException(exception.getMessage());
        }

        return nodeList;
    }

    @Override
    public List<Node> getAllAvailableOrBusyNodesFor(String capability) throws DBException {

        ArrayList<Node> nodeList = new ArrayList<>();

        try {

            String query = "SELECT node.id,node.port_number,node.ip_address,"
                    + "node.status,node.host_name,capability.name FROM node INNER "
                    + "JOIN capability ON node.status IN ('BUSY','AVAILABLE')"
                    + " AND capability.name = ? AND capability.node_id = node.id";

            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, capability);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Node node = new Node();
                node.setId(resultSet.getString(Constants.ID));
                node.setIpAddress(resultSet.getString(Constants.IP_ADDRESS));
                node.setPortNumber(resultSet.getInt(Constants.PORT_NUMBER));
                node.setStatus(resultSet.getString(Constants.STATUS));
                node.setHostName(resultSet.getString(Constants.HOST_NAME));
                nodeList.add(node);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new DBException();
        }

        return nodeList;
    }

}
