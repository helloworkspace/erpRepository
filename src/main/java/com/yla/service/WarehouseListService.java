package com.yla.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.yla.entity.WarehouseList;
import com.yla.entity.WarehouseListGoods;

/**
 * 退货单Service接口
 * @author 
 *
 */
public interface WarehouseListService {

	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public WarehouseList findById(Integer id);
	
	/**
	 * 获取当天最大退货单号
	 * @return
	 */
	public String getTodayMaxOutNumber();
	
	/**
	 * 添加退货单 以及所有退货单商品
	 * @param warehouseList 退货单
	 * @param WarehouseListGoodsList 退货单商品
	 */
	public void save(WarehouseList warehouseList,List<WarehouseListGoods> warehouseListGoodsList);
	
	/**
	 * 根据条件查询退货单信息
	 * @param warehouseList
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<WarehouseList> list(WarehouseList warehouseList,Direction direction,String... properties);
	
	/**
	 * 根据id删除退货单信息 包括退货单里的商品
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 更新退货单
	 * @param warehouseList
	 */
	public void update(WarehouseList warehouseList);
}
