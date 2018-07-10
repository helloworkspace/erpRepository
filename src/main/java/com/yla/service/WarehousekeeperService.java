package com.yla.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.yla.entity.Warehousekeeper;

/**
 * 仓管Service接口
 * @author 
 *
 */
public interface WarehousekeeperService {

	/**
	 * 根据名称模糊查询仓管信息
	 * @param name
	 * @return
	 */
	public List<Warehousekeeper> findByName(String name);
	
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public Warehousekeeper findById(Integer id);
	
	/**
	 * 修改或者修改仓管信息
	 * @param Warehousekeeper
	 */
	public void save(Warehousekeeper warehousekeeper);
	
	/**
	 * 根据条件分页查询仓管信息
	 * @param Warehousekeeper
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Warehousekeeper> list(Warehousekeeper warehousekeeper,Integer page,Integer pageSize,Direction direction,String... properties);
	
	/**
	 * 获取总记录数
	 * @param Warehousekeeper
	 * @return
	 */
	public Long getCount(Warehousekeeper warehousekeeper);
	
	/**
	 * 根据id删除仓管
	 * @param id
	 */
	public void delete(Integer id);
}
