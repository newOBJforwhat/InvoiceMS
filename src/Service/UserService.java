package Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Dao.UserDao;
import Model.User;

@Service
public class UserService {
	@Autowired
	private UserDao uDao ;
	
	@Transactional(rollbackFor=Exception.class)
	public User login(String username,String password){
		return uDao.findByUsernameAndPassword(username, password);
	}
}
