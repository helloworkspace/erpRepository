package com.yla.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.yla.entity.WarehouseInListGoods;
import com.yla.repository.WarehouseInListGoodsRepository;
import com.yla.service.WarehouseInListGoodsService;
import com.yla.util.StringUtil;

/**
 * 退货单商品Service实现类
 * @author yla_小锋老师
 *
 */
@Service("warehouseInListGoodsService")
public class WarehouseInListGoodsServiceImpl implements WarehouseInListGoodsService{

	@Resource
	private WarehouseInListGoodsRepository warehouseInListGoodsRepository;


	@Override
	public List<WarehouseInListGoods> list(WarehouseInListGoods warehouseInListGoods) {
		return warehouseInListGoodsRepository.findAll(new Specification<WarehouseInListGoods>() {
			
			@Override
			public Predicate toPredicate(Root<WarehouseInListGoods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(warehouseInListGoods!=null){
					if(warehouseInListGoods.getType()!=null && warehouseInListGoods.getType().getId()!=null && warehouseInListGoods.getType().getId()!=1){
						predicate.getExpressions().add(cb.equal(root.get("type").get("id"), warehouseInListGoods.getType().getId()));
					}
					if(StringUtil.isNotEmpty(warehouseInListGoods.getCodeOrName())){
						predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+warehouseInListGoods.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+warehouseInListGoods.getCodeOrName()+"%")));
					}
					if(warehouseInListGoods.getWarehouseInList()!=null && StringUtil.isNotEmpty(warehouseInListGoods.getWarehouseInList().getInNumber())){
						predicate.getExpressions().add(cb.like(root.get("warehouseInList").get("inNumber"), "%"+warehouseInListGoods.getWarehouseInList().getInNumber()+"%"));
					}
				}
				return predicate;
			}
		});
	}

	@Override
	public List<WarehouseInListGoods> listByWarehouseInListId(Integer warehouseInListId) {
		return warehouseInListGoodsRepository.listByWarehouseInListId(warehouseInListId);
	}
	
}
