package com.yla.service;

import java.util.List;

import com.yla.entity.Brand;

public interface BrandService {
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public Brand findById(Integer id);
	
	/**
	 * 查询所有商品品牌信息
	 * @return
	 */
	public List<Brand> listAll();
	
	/**
	 * 修改或者修改商品品牌信息
	 * @param goods
	 */
	public void save(Brand goodsUnit);
	
	/**
	 * 根据id删除商品品牌
	 * @param id
	 */
	public void delete(Integer id);
}
