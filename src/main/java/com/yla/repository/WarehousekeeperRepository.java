package com.yla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.yla.entity.Warehousekeeper;

/**
 * 客户Repository接口
 * @author yla 小锋 老师
 *
 */
public interface WarehousekeeperRepository extends JpaRepository<Warehousekeeper, Integer>,JpaSpecificationExecutor<Warehousekeeper>{

	/**
	 * 根据名称模糊查询客户信息
	 * @param name
	 * @return
	 */
	@Query(value="select * from t_warehousekeeper where name like ?1",nativeQuery=true)
	public List<Warehousekeeper> findByName(String name);
}
