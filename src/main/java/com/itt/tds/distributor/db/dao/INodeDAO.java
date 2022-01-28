package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Node;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordAlreadyExistException;
import com.itt.tds.distributor.db.exceptions.RecordNotFoundException;

import java.util.List;

public interface INodeDAO {

    public String save(Node node) throws RecordAlreadyExistException, DBException;

    public Node getNode(String id) throws RecordNotFoundException, DBException;

    public void delete(String id) throws DBException;

    public void updateStatus(String id, String status) throws DBException;

    public List<Node> getAllAvailableNodesFor(String capability) throws DBException;

    public List<Node> getAllAvailableOrBusyNodesFor(String capability) throws DBException;

}
