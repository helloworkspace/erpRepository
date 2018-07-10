package com.yla.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.yla.entity.WarehouseInList;
import com.yla.entity.WarehouseInListGoods;

/**
 * 退货单Service接口
 * @author 
 *
 */
public interface WarehouseInListService {

	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public WarehouseInList findById(Integer id);
	
	/**
	 * 获取当天最大退货单号
	 * @return
	 */
	public String getTodayMaxInNumber();
	
	/**
	 * 添加退货单 以及所有退货单商品
	 * @param warehouseInList 退货单
	 * @param WarehouseInListGoodsList 退货单商品
	 */
	public void save(WarehouseInList warehouseInList,List<WarehouseInListGoods> warehouseInListGoodsList);
	
	/**
	 * 根据条件查询退货单信息
	 * @param warehouseInList
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<WarehouseInList> list(WarehouseInList warehouseInList,Direction direction,String... properties);
	
	/**
	 * 根据id删除退货单信息 包括退货单里的商品
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 更新退货单
	 * @param warehouseInList
	 */
	public void update(WarehouseInList warehouseInList);
}
