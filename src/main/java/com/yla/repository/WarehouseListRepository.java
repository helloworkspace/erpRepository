package com.yla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.yla.entity.WarehouseList;

public interface WarehouseListRepository extends JpaRepository<WarehouseList, Integer>,JpaSpecificationExecutor<WarehouseList>{

	/**
	 * 获取当天最大退货单号
	 * @return
	 */
	@Query(value="SELECT MAX(out_number) FROM t_warehouse_list WHERE TO_DAYS(out_date) = TO_DAYS(NOW())",nativeQuery=true)
	public String getTodayMaxOutNumber();
}