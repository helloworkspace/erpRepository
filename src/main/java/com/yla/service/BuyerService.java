package com.yla.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.yla.entity.Buyer;

/**
 * 采购员Service接口
 * @author 
 *
 */
public interface BuyerService {

	/**
	 * 根据名称模糊查询采购员信息
	 * @param name
	 * @return
	 */
	public List<Buyer> findByName(String name);
	
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public Buyer findById(Integer id);
	
	/**
	 * 修改或者修改采购员信息
	 * @param Buyer
	 */
	public void save(Buyer buyer);
	
	/**
	 * 根据条件分页查询采购员信息
	 * @param Buyer
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Buyer> list(Buyer buyer,Integer page,Integer pageSize,Direction direction,String... properties);
	
	/**
	 * 获取总记录数
	 * @param Buyer
	 * @return
	 */
	public Long getCount(Buyer buyer);
	
	/**
	 * 根据id删除采购员
	 * @param id
	 */
	public void delete(Integer id);
}
