package com.yla.service;

import java.util.List;

import com.yla.entity.WarehouseListGoods;

/**
 * 销售单商品Service接口
 * @author yla_小锋老师
 *
 */
public interface WarehouseListGoodsService {

	/**
	 * 根据销售单id查询所有销售单商品
	 * @param warehouseListId
	 * @return
	 */
	public List<WarehouseListGoods> listByWarehouseListId(Integer warehouseListId);
	
	
	/**
	 * 根据条件查询销售单所有商品
	 * @param warehouseListGoods
	 * @return
	 */
	public List<WarehouseListGoods> list(WarehouseListGoods warehouseListGoods);


}
