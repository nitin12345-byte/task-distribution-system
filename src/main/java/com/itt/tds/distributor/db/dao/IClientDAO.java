package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Client;
import java.sql.SQLException;
import java.util.List;

public interface IClientDAO {

    public void save(Client user) throws SQLException;

    public Client getClient(long id) throws SQLException;

    public void delete(long id) throws SQLException;

    public List<Client> getAllClients() throws SQLException;
}
