package com.yla.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.yla.entity.Salesman;

/**
 * 业务员Service接口
 * @author 
 *
 */
public interface SalesmanService {

	/**
	 * 根据名称模糊查询业务员信息
	 * @param name
	 * @return
	 */
	public List<Salesman> findByName(String name);
	
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public Salesman findById(Integer id);
	
	/**
	 * 修改或者修改业务员信息
	 * @param Salesman
	 */
	public void save(Salesman salesman);
	
	/**
	 * 根据条件分页查询业务员信息
	 * @param Salesman
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Salesman> list(Salesman salesman,Integer page,Integer pageSize,Direction direction,String... properties);
	
	/**
	 * 获取总记录数
	 * @param Salesman
	 * @return
	 */
	public Long getCount(Salesman salesman);
	
	/**
	 * 根据id删除业务员
	 * @param id
	 */
	public void delete(Integer id);
}
