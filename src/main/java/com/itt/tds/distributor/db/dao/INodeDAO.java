package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Node;
import java.sql.SQLException;
import java.util.List;

public interface INodeDAO {

    public boolean save(Node node) throws SQLException;

    public Node getNode(long id) throws SQLException;

    public void delete(long id) throws SQLException;

    public void updateStatus(long id, String status) throws SQLException;

    public List<Node> getAllNodes() throws SQLException;

}
