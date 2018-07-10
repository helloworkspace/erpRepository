package com.yla.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yla.entity.Brand;
import com.yla.repository.BrandRepository;
import com.yla.service.BrandService;

/**
 * 商品单位Service实现类
 * @author yla 小锋 老师
 *
 */
@Service("brandService")
public class BrandServiceImpl implements BrandService{

	@Resource
	private BrandRepository brandRepository;
	
	@Override
	public List<Brand> listAll() {
		return brandRepository.findAll();
	}

	@Override
	public void save(Brand brand) {
		brandRepository.save(brand);
	}

	@Override
	public void delete(Integer id) {
		brandRepository.delete(id);
	}

	@Override
	public Brand findById(Integer id) {
		return brandRepository.findOne(id);
	}

}
