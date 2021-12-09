package com.itt.tds.db.dao;

import com.itt.tds.model.User;

public interface IUserDAO {

    public void save(User user);

    public User getUser(long id);

    public void delete(long id);

    public void updateStatus(long id, String status);
}
