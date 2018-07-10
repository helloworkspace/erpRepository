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

import com.yla.entity.Salesman;
import com.yla.repository.SalesmanRepository;
import com.yla.service.SalesmanService;
import com.yla.util.StringUtil;

/**
 * 业务员Service实现类
 * @author yla 
 *
 */
@Service("SalesmanService")
public class SalesmanServiceImpl implements SalesmanService{

	@Resource
	private SalesmanRepository salesmanRepository;
	

	@Override
	public void save(Salesman salesman) {
		salesmanRepository.save(salesman);
	}

	@Override
	public List<Salesman> list(Salesman salesman, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Salesman> pageSalesman=salesmanRepository.findAll(new Specification<Salesman>() {
			
			@Override
			public Predicate toPredicate(Root<Salesman> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(salesman!=null){
					if(StringUtil.isNotEmpty(salesman.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+salesman.getName().trim()+"%"));
					}	
				}
				return predicate;
			}
		}, pageable);
		return pageSalesman.getContent();
	}

	@Override
	public Long getCount(Salesman salesman) {
		Long count=salesmanRepository.count(new Specification<Salesman>() {

			@Override
			public Predicate toPredicate(Root<Salesman> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(salesman!=null){
					if(StringUtil.isNotEmpty(salesman.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+salesman.getName().trim()+"%"));
					}	
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		salesmanRepository.delete(id);
	}

	@Override
	public Salesman findById(Integer id) {
		return salesmanRepository.findOne(id);
	}

	@Override
	public List<Salesman> findByName(String name) {
		return salesmanRepository.findByName(name);
	}


}
