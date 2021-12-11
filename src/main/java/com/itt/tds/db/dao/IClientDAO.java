package com.itt.tds.db.dao;

import com.itt.tds.model.Client;
import java.util.List;


public interface IClientDAO {

    public void save(Client user);

    public Client getClient(long id);

    public void delete(long id);

    public List<Client> getAllClients();
}
