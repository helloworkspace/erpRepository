package com.yla.service;

import java.util.List;

import com.yla.entity.WarehouseInListGoods;

/**
 * 退货单商品Service接口
 * @author 
 *
 */
public interface WarehouseInListGoodsService {

	/**
	 * 根据退货单id查询所有退货单商品
	 * @param returnListId
	 * @return
	 */
	public List<WarehouseInListGoods> listByWarehouseInListId(Integer warehouseInListId);
	
	/**
	 * 根据条件查询退货单所有商品
	 * @param warehouseInListGoods
	 * @return
	 */
	public List<WarehouseInListGoods> list(WarehouseInListGoods warehouseInListGoods);

	
}
