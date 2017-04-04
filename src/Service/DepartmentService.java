package Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import Dao.DepartmentDao;
import Model.Department;

@Service
public class DepartmentService {
	@Autowired
	private DepartmentDao dDao;
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Department> getAll(){
		return dDao.findAll();
	}
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public void alterInfo(long id,String name){
		if(dDao.findById(id) == null)
			throw new NullPointerException("部门id:"+id+" 不存在");
		dDao.alterDepartmentInfo(id, name);
	}
	@Transactional(rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public long addDepartment(String name){
		Department dm = new Department();
		dm.setName(name);
		dDao.addDepartment(dm);
		return dm.getId();
	}
	
}
