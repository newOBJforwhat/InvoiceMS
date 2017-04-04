package Dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import Model.Department;

public interface DepartmentDao {
	//查询
	public List<Department> findAll();
	public Department findById(long id);
	//增加
	public void addDepartment(Department de);
	//删除
	//修改
	public void alterDepartmentInfo(@Param("id")long id,@Param("name")String name);
}
