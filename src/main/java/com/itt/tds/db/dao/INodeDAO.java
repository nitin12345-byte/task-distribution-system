package com.itt.tds.db.dao;

import com.itt.tds.model.Node;
import java.util.List;

public interface INodeDAO {

    public void save(Node node);

    public Node getNode(long id);

    public void delete(long id);

    public void updateStatus(long id, String status);
    
    public List<Node> getAllNodes();
    
}
