package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Client;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordAlreadyExistException;
import com.itt.tds.distributor.db.exceptions.RecordNotFoundException;
import java.util.List;

public interface IClientDAO {

    public String save(Client user) throws RecordAlreadyExistException, DBException;

    public Client getClient(String id) throws RecordNotFoundException, DBException;

    public void delete(String id) throws DBException;

    public List<Client> getAllClients() throws DBException;
}
