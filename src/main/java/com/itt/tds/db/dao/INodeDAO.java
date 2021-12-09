package com.itt.tds.db.dao;

import com.itt.tds.model.Node;

public interface INodeDAO {

    public void save(Node node);

    public Node getNode(long id);

    public void delete(long id);

    public void updateStatus(long id, String status);
}
