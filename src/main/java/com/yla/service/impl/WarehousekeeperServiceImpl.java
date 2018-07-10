package com.yla.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.yla.entity.Warehousekeeper;
import com.yla.repository.WarehousekeeperRepository;
import com.yla.service.WarehousekeeperService;
import com.yla.util.StringUtil;

/**
 * 供应商Service实现类
 * @author yla 
 *
 */
@Service("warehousekeeperService")
public class WarehousekeeperServiceImpl implements WarehousekeeperService{

	@Resource
	private WarehousekeeperRepository warehousekeeperRepository;
	

	@Override
	public void save(Warehousekeeper warehousekeeper) {
		warehousekeeperRepository.save(warehousekeeper);
	}

	@Override
	public List<Warehousekeeper> list(Warehousekeeper warehousekeeper, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Warehousekeeper> pageWarehousekeeper=warehousekeeperRepository.findAll(new Specification<Warehousekeeper>() {
			
			@Override
			public Predicate toPredicate(Root<Warehousekeeper> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(warehousekeeper!=null){
					if(StringUtil.isNotEmpty(warehousekeeper.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+warehousekeeper.getName().trim()+"%"));
					}	
				}
				return predicate;
			}
		}, pageable);
		return pageWarehousekeeper.getContent();
	}

	@Override
	public Long getCount(Warehousekeeper warehousekeeper) {
		Long count=warehousekeeperRepository.count(new Specification<Warehousekeeper>() {

			@Override
			public Predicate toPredicate(Root<Warehousekeeper> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(warehousekeeper!=null){
					if(StringUtil.isNotEmpty(warehousekeeper.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+warehousekeeper.getName().trim()+"%"));
					}	
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		warehousekeeperRepository.delete(id);
	}

	@Override
	public Warehousekeeper findById(Integer id) {
		return warehousekeeperRepository.findOne(id);
	}

	@Override
	public List<Warehousekeeper> findByName(String name) {
		return warehousekeeperRepository.findByName(name);
	}


}
