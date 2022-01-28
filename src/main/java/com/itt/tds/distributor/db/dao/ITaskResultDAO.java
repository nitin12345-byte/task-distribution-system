package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.TaskResult;
import com.itt.tds.distributor.db.exceptions.DBException;


public interface ITaskResultDAO {

    public void save(TaskResult taskResult) throws DBException;

    public void delete(String taskId) throws DBException;
}
