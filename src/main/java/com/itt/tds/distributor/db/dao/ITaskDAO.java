package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Task;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordAlreadyExistException;
import com.itt.tds.distributor.db.exceptions.RecordNotFoundException;
import java.util.List;

public interface ITaskDAO {

    public String save(Task t) throws DBException, RecordAlreadyExistException;

    public Task getTask(String id) throws DBException, RecordNotFoundException;

    public void delete(String id) throws DBException;

    public void updateStatus(String id, String status) throws DBException;

    public List<Task> getAllTask(String clientId) throws DBException;
}
