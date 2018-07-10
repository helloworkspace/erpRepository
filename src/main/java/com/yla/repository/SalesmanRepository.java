package com.yla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.yla.entity.Salesman;

public interface SalesmanRepository extends JpaRepository<Salesman, Integer>,JpaSpecificationExecutor<Salesman>{

	/**
	 * 根据名称模糊查询业务员信息
	 * @param name
	 * @return
	 */
	@Query(value="select * from t_salesman where name like ?1",nativeQuery=true)
	public List<Salesman> findByName(String name);
	
}
