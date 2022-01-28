package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Capability;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordAlreadyExistException;

public interface ICapabilityDAO {

    public void save(Capability capability) throws RecordAlreadyExistException, DBException;

    public void delete(Capability capability) throws DBException;

}
