package com.yla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.yla.entity.WarehouseInList;

public interface WarehouseInListRepository extends JpaRepository<WarehouseInList, Integer>,JpaSpecificationExecutor<WarehouseInList>{

	/**
	 * 获取当天最大进仓单号
	 * @return
	 */
	@Query(value="SELECT MAX(in_number) FROM t_warehouse_in_list WHERE TO_DAYS(in_date) = TO_DAYS(NOW())",nativeQuery=true)
	public String getTodayMaxInNumber();
}


