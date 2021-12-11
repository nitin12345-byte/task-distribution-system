package com.itt.tds.db.dao;

import com.itt.tds.model.Task;
import java.util.List;

public interface ITaskDAO {

    public void save(Task t);

    public Task getTask(long id);

    public void delete(long id);

    public void updateStatus(long id, String status);
    
    public List<Task> getAllTask();
}
