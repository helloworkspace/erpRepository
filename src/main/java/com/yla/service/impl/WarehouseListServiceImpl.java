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
import com.yla.entity.WarehouseList;
import com.yla.entity.WarehouseListGoods;
import com.yla.repository.GoodsRepository;
import com.yla.repository.GoodsTypeRepository;
import com.yla.repository.WarehouseListGoodsRepository;
import com.yla.repository.WarehouseListRepository;
import com.yla.service.WarehouseListService;
import com.yla.util.StringUtil;

/**
 * 进仓单Service实现类
 * @author yla_小锋老师
 *
 */
@Service("warehouseListService")
@Transactional
public class WarehouseListServiceImpl implements WarehouseListService{

	@Resource
	private WarehouseListRepository warehouseListRepository;
	
	@Resource
	private WarehouseListGoodsRepository warehouseListGoodsRepository;
	
	@Resource
	private GoodsRepository goodsRepository;
	
	@Resource
	private GoodsTypeRepository goodsTypeRepository;
	
	@Override
	public String getTodayMaxOutNumber() {
		return warehouseListRepository.getTodayMaxOutNumber();
	}

	@Transactional
	public void save(WarehouseList warehouseList, List<WarehouseListGoods> warehouseListGoodsList) {
		// 保存每个退货单商品
		for(WarehouseListGoods warehouseListGoods:warehouseListGoodsList){
			warehouseListGoods.setType(goodsTypeRepository.findOne(warehouseListGoods.getTypeId())); // 设置类别
			warehouseListGoods.setWarehouseList(warehouseList); // 设置退货单
			warehouseListGoodsRepository.save(warehouseListGoods);
			// 修改商品库存
			Goods goods=goodsRepository.findOne(warehouseListGoods.getGoodsId());
			goods.setInventoryQuantity(goods.getInventoryQuantity()-warehouseListGoods.getNum());
			goods.setState(2);
			goodsRepository.save(goods);
		}
		warehouseListRepository.save(warehouseList); // 保存退货单
	}

	@Override
	public WarehouseList findById(Integer id) {
		return warehouseListRepository.findOne(id);
		
	}

	@Override
	public List<WarehouseList> list(WarehouseList warehouseList, Direction direction, String... properties) {
		return warehouseListRepository.findAll(new Specification<WarehouseList>(){

			@Override
			public Predicate toPredicate(Root<WarehouseList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(warehouseList!=null){
					if(warehouseList.getDepartment()!=null && warehouseList.getDepartment().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("department").get("id"), warehouseList.getDepartment().getId()));
					}
					if(warehouseList.getWarehousekeeper()!=null && warehouseList.getWarehousekeeper().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("warehousekeeper").get("id"), warehouseList.getWarehousekeeper().getId()));
					}
					if(StringUtil.isNotEmpty(warehouseList.getOutNumber())){
						predicate.getExpressions().add(cb.like(root.get("outNumber"), "%"+warehouseList.getOutNumber().trim()+"%"));
					}
					if(warehouseList.getState()!=null){
						predicate.getExpressions().add(cb.equal(root.get("state"), warehouseList.getState()));
					}
					if(warehouseList.getbOutDate()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("outDate"), warehouseList.getbOutDate()));
					}
					if(warehouseList.geteOutDate()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("outDate"), warehouseList.geteOutDate()));
					}
				}
				return predicate;
			}
		  },new Sort(direction, properties));
	}

	@Override
	public void delete(Integer id) {
		warehouseListGoodsRepository.deleteByWarehouseListId(id);
		warehouseListRepository.delete(id);
	}

	@Override
	public void update(WarehouseList warehouseList) {
		warehouseListRepository.save(warehouseList);
	}

}
