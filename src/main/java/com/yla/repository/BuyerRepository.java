package com.yla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.yla.entity.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Integer>,JpaSpecificationExecutor<Buyer>{

	/**
	 * 根据名称模糊查询采购员信息
	 * @param name
	 * @return
	 */
	@Query(value="select * from t_buyer where name like ?1",nativeQuery=true)
	public List<Buyer> findByName(String name);
	
}
