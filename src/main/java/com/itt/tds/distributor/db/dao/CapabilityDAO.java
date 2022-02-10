package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Capability;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordAlreadyExistException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 *
 * @author nitin.jangid
 */
public class CapabilityDAO implements ICapabilityDAO {

    private Connection dbConnection;

    public CapabilityDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void save(Capability capability) throws DBException {
        String query = "INSERT INTO capability(node_id, name) VALUES(?,?)";

        try {
            PreparedStatement prepearedStatement = dbConnection.prepareStatement(query);
            prepearedStatement.setString(1, capability.getNodeId());
            prepearedStatement.setString(2, capability.getName());
            prepearedStatement.execute();

        } catch (SQLIntegrityConstraintViolationException exception) {
            exception.printStackTrace();
            throw new RecordAlreadyExistException("Capability already added");
        } catch (SQLException exception) {
            throw new DBException(exception.getMessage());
        }

    }

    @Override
    public void delete(Capability capability) throws DBException {

        String query = "DELETE FROM capability WHERE node_id = ? and name = ?";

        try {
            PreparedStatement prepearedStatement = dbConnection.prepareStatement(query);
            prepearedStatement.setString(1, capability.getNodeId());
            prepearedStatement.setString(2, capability.getName());
            prepearedStatement.execute();

        } catch (SQLException exception) {
            throw new DBException(exception.getMessage());
        }

    }

}
