package com.yla.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.yla.entity.Department;

/**
 * 公司单位Service接口
 * @author 
 *
 */
public interface DepartmentService {

	/**
	 * 根据名称模糊查询公司单位信息
	 * @param name
	 * @return
	 */
	public List<Department> findByName(String name);
	
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public Department findById(Integer id);
	
	/**
	 * 修改或者修改公司单位信息
	 * @param department
	 */
	public void save(Department department);
	
	/**
	 * 根据条件分页查询公司单位信息
	 * @param department
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Department> list(Department department,Integer page,Integer pageSize,Direction direction,String... properties);
	
	/**
	 * 获取总记录数
	 * @param department
	 * @return
	 */
	public Long getCount(Department department);
	
	/**
	 * 根据id删除公司单位
	 * @param id
	 */
	public void delete(Integer id);
}
