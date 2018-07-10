package com.yla.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.yla.entity.WarehouseListGoods;
import com.yla.repository.WarehouseListGoodsRepository;
import com.yla.service.WarehouseListGoodsService;
import com.yla.util.StringUtil;

/**
 * 退货单商品Service实现类
 * @author yla_小锋老师
 *
 */
@Service("warehouseListGoodsService")
public class WarehouseListGoodsServiceImpl implements WarehouseListGoodsService{

	@Resource
	private WarehouseListGoodsRepository warehouseListGoodsRepository;


	@Override
	public List<WarehouseListGoods> list(WarehouseListGoods warehouseListGoods) {
		return warehouseListGoodsRepository.findAll(new Specification<WarehouseListGoods>() {
			
			@Override
			public Predicate toPredicate(Root<WarehouseListGoods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(warehouseListGoods!=null){
					if(warehouseListGoods.getType()!=null && warehouseListGoods.getType().getId()!=null && warehouseListGoods.getType().getId()!=1){
						predicate.getExpressions().add(cb.equal(root.get("type").get("id"), warehouseListGoods.getType().getId()));
					}
					if(StringUtil.isNotEmpty(warehouseListGoods.getCodeOrName())){
						predicate.getExpressions().add(cb.or(cb.like(root.get("code"),"%"+warehouseListGoods.getCodeOrName()+"%"), cb.like(root.get("name"),"%"+warehouseListGoods.getCodeOrName()+"%")));
					}
					if(warehouseListGoods.getWarehouseList()!=null && StringUtil.isNotEmpty(warehouseListGoods.getWarehouseList().getOutNumber())){
						predicate.getExpressions().add(cb.like(root.get("warehouseList").get("outNumber"), "%"+warehouseListGoods.getWarehouseList().getOutNumber()+"%"));
					}
				}
				return predicate;
			}
		});
	}

	@Override
	public List<WarehouseListGoods> listByWarehouseListId(Integer warehouseListId) {
		return warehouseListGoodsRepository.listByWarehouseListId(warehouseListId);
	}

	
	
}
