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
	public List<User> findAll();
	//插入
	public int applyUser(User u);
	//更新
	public void updateInfo(@Param("id")long id,@Param("departmentId")long departmentId,@Param("password")String password,@Param("name")String name);
	public void updateDeleteStatus(@Param("id")long id,@Param("code")int code);
	//删除
	public void deleteById(long id);
}
