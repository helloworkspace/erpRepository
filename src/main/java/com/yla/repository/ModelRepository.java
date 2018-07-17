package com.yla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yla.entity.Model;

public interface ModelRepository extends JpaRepository<Model, Integer>,JpaSpecificationExecutor<Model>{

}
