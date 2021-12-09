
package com.itt.tds.db.dao;

import com.itt.tds.model.Node;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NodeDAO implements INodeDAO{

    private Connection dbConnection;

    public NodeDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(Node node) {

        String query = "insert into node(name,ip_address,port_number,status) values(?,?,?,?)";

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
        String query = "delete from node where id = ? ";
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
        String query = "select * from node where id = ? ";
        Node node = null;

        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            node = new Node();
            node.setId(resultSet.getInt("id"));
            node.setName(resultSet.getString("name"));
            node.setIpAddress(resultSet.getString("ip_address"));
            node.setPortNumber(resultSet.getInt("port_number"));
            node.setStatus(resultSet.getString("status"));

        } catch (SQLException exp) {
            System.out.print(exp);
        }

        return node;
    }

    @Override
    public void updateStatus(long id, String status) {
        String query = "update table node set status = ? where id = ?";

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
