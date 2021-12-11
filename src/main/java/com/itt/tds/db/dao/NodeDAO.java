package com.itt.tds.db.dao;

import com.itt.tds.model.Node;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NodeDAO implements INodeDAO {

    private Connection dbConnection;

    public NodeDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(Node node) {

        String query = "INSERT INTO node(name,ip_address,port_number,status) VALUES(?,?,?,?)";

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, node.getName());
            preparedStatement.setString(2, node.getIpAddress());
            preparedStatement.setInt(3, node.getPortNumber());
            preparedStatement.setString(4, node.getStatus());
            preparedStatement.execute();

        } catch (SQLException exp) {
            System.out.print(exp.getMessage());
        }
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM node WHERE id = ? ";
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException exp) {
            System.out.print(exp);
        }
    }

    @Override
    public Node getNode(long id) {
        String query = "SELECT * FROM node WHERE id = ? ";
        Node node = new Node();

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                node.setId(resultSet.getInt("id"));
                node.setName(resultSet.getString("name"));
                node.setIpAddress(resultSet.getString("ip_address"));
                node.setPortNumber(resultSet.getInt("port_number"));
                node.setStatus(resultSet.getString("status"));
            }
        } catch (SQLException exp) {
            System.out.print(exp);
        }

        return node;
    }

    @Override
    public void updateStatus(long id, String status) {
        String query = "UPDATE node SET status = ? WHERE id = ?";

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
    public List<Node> getAllNodes() {

        ArrayList<Node> nodeList = new ArrayList<>();

        try {
            String query = "SELECT * FROM node";
            Statement stm = dbConnection.createStatement();
            ResultSet resultSet = stm.executeQuery(query);

            while (resultSet.next()) {

                Node node = new Node();
                node.setId(resultSet.getLong("id"));
                node.setIpAddress(resultSet.getString("ip_address"));
                node.setPortNumber(resultSet.getInt("port_number"));
                node.setStatus(resultSet.getString("status"));
                node.setName(resultSet.getString("name"));
                nodeList.add(node);

            }

        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }
        return nodeList;
    }
}
