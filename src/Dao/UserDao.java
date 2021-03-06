package Dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import Model.User;

public interface UserDao {
	//查询
	public User findById(long id);
	public User findByUsernameAndPassword(@Param("username")String username,@Param("password")String password);
	public User findByUsername(String username);
	public List<User> findAll();
	public String findNameById(long id);
	public List<User> getCharacter(int type);
	//插入
	public int applyUser(User u);
	//更新
	public void updateInfo(@Param("id")long id,@Param("name")String name);
	public void updateDeleteStatus(@Param("id")long id,@Param("code")int code);
	public void updatePassword(@Param("id")long id,@Param("password")String password);
	//删除
	public void deleteById(long id);
}
