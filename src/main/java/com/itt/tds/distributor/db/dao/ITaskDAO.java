package com.itt.tds.distributor.db.dao;

import com.itt.tds.core.model.Task;
import java.sql.SQLException;
import java.util.List;

public interface ITaskDAO {

    public void save(Task t) throws SQLException;

    public Task getTask(long id) throws SQLException;

    public void delete(long id) throws SQLException;

    public void updateStatus(long id, String status) throws SQLException;

    public List<Task> getAllTask() throws SQLException;
}
