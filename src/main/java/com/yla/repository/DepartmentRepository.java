package com.yla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.yla.entity.Department;

/**
 * 客户Repository接口
 * @author yla 小锋 老师
 *
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer>,JpaSpecificationExecutor<Department>{

	/**
	 * 根据名称模糊查询公司单位信息
	 * @param name
	 * @return
	 */
	@Query(value="select * from t_department where name like ?1",nativeQuery=true)
	public List<Department> findByName(String name);
}
