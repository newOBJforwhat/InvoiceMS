package Dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import Model.User;

public interface UserDao {
	//查询
	public User findById(long id);
	public User findByUsernameAndPassword(@Param("username")String username,@Param("password")String password);
	public User findByUsername(String username);
	public List<User> findByDepartment(long department);
	
	//插入
	public int applyUser(User u);
}
