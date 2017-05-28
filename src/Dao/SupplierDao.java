package Dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import Model.Supplier;

public interface SupplierDao {
	//添加
	public void addSupplier(@Param("supplierNumber")String supplierNumber,@Param("supplierName")String supplierName);
	//查询
	public List<Supplier> findLikeNumberorName(@Param("supplierNumber")String supplierNumber,@Param("supplierName")String supplierName);
	public List<Supplier> findListByPage(@Param("pageIndex")int pageIndex,@Param("pageSize")int pageSize);
	public int findCountListByPage();
	public Supplier findByNumber(String supplierNumber);
	public Supplier findById(long id);
	//删除供应商
	public void deleteByNumber(long id);
	//修改供应商信息
	public void alterById(@Param("id")long id,@Param("supplierName")String supplierName);
}
