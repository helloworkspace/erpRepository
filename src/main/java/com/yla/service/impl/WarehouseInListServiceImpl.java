package com.yla.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.yla.entity.Goods;
import com.yla.entity.WarehouseInList;
import com.yla.entity.WarehouseInListGoods;
import com.yla.repository.GoodsRepository;
import com.yla.repository.GoodsTypeRepository;
import com.yla.repository.WarehouseInListGoodsRepository;
import com.yla.repository.WarehouseInListRepository;
import com.yla.service.WarehouseInListService;
import com.yla.util.StringUtil;

/**
 * 进仓单Service实现类
 * @author yla_小锋老师
 *
 */
@Service("warehouseInListService")
@Transactional
public class WarehouseInListServiceImpl implements WarehouseInListService{

	@Resource
	private WarehouseInListRepository warehouseInListRepository;
	
	@Resource
	private WarehouseInListGoodsRepository warehouseInListGoodsRepository;
	
	@Resource
	private GoodsRepository goodsRepository;
	
	@Resource
	private GoodsTypeRepository goodsTypeRepository;
	
	@Override
	public String getTodayMaxInNumber() {
		return warehouseInListRepository.getTodayMaxInNumber();
	}

	@Transactional
	public void save(WarehouseInList warehouseInList, List<WarehouseInListGoods> warehouseInListGoodsList) {
		// 保存每个退货单商品
		for(WarehouseInListGoods warehouseInListGoods:warehouseInListGoodsList){
			warehouseInListGoods.setType(goodsTypeRepository.findOne(warehouseInListGoods.getTypeId())); // 设置类别
			warehouseInListGoods.setWarehouseInList(warehouseInList); // 设置退货单
			warehouseInListGoodsRepository.save(warehouseInListGoods);
			// 修改商品库存
			Goods goods=goodsRepository.findOne(warehouseInListGoods.getGoodsId());
			goods.setInventoryQuantity(goods.getInventoryQuantity()+warehouseInListGoods.getNum());
			goods.setState(2);
			goodsRepository.save(goods);
		}
		warehouseInListRepository.save(warehouseInList); // 保存退货单
	}

	@Override
	public WarehouseInList findById(Integer id) {
		return warehouseInListRepository.findOne(id);
		
	}

	@Override
	public List<WarehouseInList> list(WarehouseInList warehouseInList, Direction direction, String... properties) {
		return warehouseInListRepository.findAll(new Specification<WarehouseInList>(){

			@Override
			public Predicate toPredicate(Root<WarehouseInList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(warehouseInList!=null){
					if(warehouseInList.getDepartment()!=null && warehouseInList.getDepartment().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("department").get("id"), warehouseInList.getDepartment().getId()));
					}
					if(warehouseInList.getWarehousekeeper()!=null && warehouseInList.getWarehousekeeper().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("warehousekeeper").get("id"), warehouseInList.getWarehousekeeper().getId()));
					}
					if(StringUtil.isNotEmpty(warehouseInList.getInNumber())){
						predicate.getExpressions().add(cb.like(root.get("inNumber"), "%"+warehouseInList.getInNumber().trim()+"%"));
					}
					if(warehouseInList.getState()!=null){
						predicate.getExpressions().add(cb.equal(root.get("state"), warehouseInList.getState()));
					}
					if(warehouseInList.getbInDate()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("inDate"), warehouseInList.getbInDate()));
					}
					if(warehouseInList.geteInDate()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("inDate"), warehouseInList.geteInDate()));
					}
				}
				return predicate;
			}
		  },new Sort(direction, properties));
	}

	@Override
	public void delete(Integer id) {
		warehouseInListGoodsRepository.deleteByWarehouseInListId(id);
		warehouseInListRepository.delete(id);
	}

	@Override
	public void update(WarehouseInList warehouseInList) {
		warehouseInListRepository.save(warehouseInList);
	}

}
