package com.yla.service;

import java.util.List;

import com.yla.entity.Model;

public interface ModelService {
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public Model findById(Integer id);
	
	/**
	 * 查询所有商品规格信息
	 * @return
	 */
	public List<Model> listAll();
	
	/**
	 * 修改或者修改商品规格信息
	 * @param goods
	 */
	public void save(Model goodsUnit);
	
	/**
	 * 根据id删除商品规格
	 * @param id
	 */
	public void delete(Integer id);
}
