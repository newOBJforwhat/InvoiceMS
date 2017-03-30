package Dao;

import Model.User;

public interface UserDao {
	public User findById(long id);
}
