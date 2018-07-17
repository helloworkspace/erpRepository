package com.yla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yla.entity.Warehouse;

/**
 * 商品仓库Repository接口
 * @author
 *
 */

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer>,JpaSpecificationExecutor<Warehouse>{

}
