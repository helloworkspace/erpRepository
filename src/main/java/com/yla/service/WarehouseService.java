package com.yla.service;

import java.util.List;

import com.yla.entity.Warehouse;

/**
 * 商品仓库Service接口
 * @author java1234 小锋 老师
 *
 */
public interface WarehouseService {

	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public Warehouse findById(Integer id);
	
	/**
	 * 查询所有商品仓库信息
	 * @return
	 */
	public List<Warehouse> listAll();
	
	/**
	 * 修改或者修改商品仓库信息
	 * @param goods
	 */
	public void save(Warehouse goodsUnit);
	
	/**
	 * 根据id删除商品仓库
	 * @param id
	 */
	public void delete(Integer id);
}
