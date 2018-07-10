package com.yla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yla.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer>,JpaSpecificationExecutor<Warehouse>{

}
