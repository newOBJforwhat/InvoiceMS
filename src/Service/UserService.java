package Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Dao.UserDao;
import Form.ApplyUserForm;
import Model.User;

@Service
public class UserService {
	@Autowired
	private UserDao uDao ;
	public UserService(){
		System.out.println("service constructor called!");
	}
	/**
	 * propagation = Propagation.*** 配置事务的传播行为
	 * REQUIRED:果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
	 * REQUIRES_NEW:创建一个新的事务，如果当前存在事务，则把当前事务挂起。
	 * SUPPORTS:如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
	 * 
	 * readOnly 配置事务是否读写 true为只读
	 * 
	 * rollbackFor=Exception.class 发生Exception就回滚
	 * 
	 * timeout=15 15s超时
	 * 
	 * 设置事务的隔离级别 
	 * isolation = Isolation.READ_UNCOMMITTED
	 * isolation = Isolation.READ_COMMITTED
	 * isolation = Isolation.REPEATABLE_READ
	 * isolation = Isolation.SERIALIZABLE
	 * 
	 */
	
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public User login(String username,String password){
		return uDao.findByUsernameAndPassword(username, password);
	}
	
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public User validateUser(String username){
		/**
		 * 缓存就是一个事务中如果两次查询同一对象则不执行sql
		 */
		User u = uDao.findByUsername(username);
		System.out.println(u);
		return u ;
	}
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<User> departmentMembers(long departmentId){
		List<User> dmembers =  uDao.findByDepartment(departmentId);
		return dmembers ;
	}
	@Transactional(rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public long applyUser(ApplyUserForm form){
		User u = new User();
		u.setId(0);
		u.setDepartmentId(form.getDepartmentId());
		u.setName(form.getName());
		u.setPassword(form.getPassword());
		u.setType(form.getType());
		u.setUsername(form.getUsername());
		if(uDao.findByUsername(u.getUsername()) != null){
			uDao.applyUser(u);
		}
		return u.getId();
	}
	@Transactional(rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public void updateUserInfo(long id,String password,String name,long departmentId){
		uDao.updateInfo(id, departmentId, password, name);
	}
	@Transactional(rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public void deleteUser(long id){
		if(uDao.findById(id) != null){
			uDao.deleteById(id);
		}else
			throw new NullPointerException();
	}
	public void setuDao(UserDao uDao) {
		this.uDao = uDao;
	}
	
}
