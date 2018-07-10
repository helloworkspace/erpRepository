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

import com.yla.entity.Buyer;
import com.yla.repository.BuyerRepository;
import com.yla.service.BuyerService;
import com.yla.util.StringUtil;

/**
 * 供应商Service实现类
 * @author yla 
 *
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService{

	@Resource
	private BuyerRepository buyerRepository;
	

	@Override
	public void save(Buyer buyer) {
		buyerRepository.save(buyer);
	}

	@Override
	public List<Buyer> list(Buyer buyer, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Buyer> pageBuyer=buyerRepository.findAll(new Specification<Buyer>() {
			
			@Override
			public Predicate toPredicate(Root<Buyer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(buyer!=null){
					if(StringUtil.isNotEmpty(buyer.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+buyer.getName().trim()+"%"));
					}	
				}
				return predicate;
			}
		}, pageable);
		return pageBuyer.getContent();
	}

	@Override
	public Long getCount(Buyer buyer) {
		Long count=buyerRepository.count(new Specification<Buyer>() {

			@Override
			public Predicate toPredicate(Root<Buyer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(buyer!=null){
					if(StringUtil.isNotEmpty(buyer.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+buyer.getName().trim()+"%"));
					}	
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		buyerRepository.delete(id);
	}

	@Override
	public Buyer findById(Integer id) {
		return buyerRepository.findOne(id);
	}

	@Override
	public List<Buyer> findByName(String name) {
		return buyerRepository.findByName(name);
	}


}
