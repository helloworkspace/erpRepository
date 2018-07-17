package com.yla.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yla.entity.Warehouse;
import com.yla.repository.WarehouseRepository;
import com.yla.service.WarehouseService;

/**
 * 商品仓库Service实现类
 * @author yla
 *
 */
@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService{

	@Resource
	private WarehouseRepository warehouseRepository;
	
	@Override
	public List<Warehouse> listAll() {
		return warehouseRepository.findAll();
	}

	@Override
	public void save(Warehouse warehouse) {
		warehouseRepository.save(warehouse);
	}

	@Override
	public void delete(Integer id) {
		warehouseRepository.delete(id);
	}

	@Override
	public Warehouse findById(Integer id) {
		return warehouseRepository.findOne(id);
	}

}
