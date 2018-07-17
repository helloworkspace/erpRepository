package com.yla.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yla.entity.Brand;
import com.yla.repository.BrandRepository;
import com.yla.service.BrandService;


/**
 * 商品规格Service实现类
 * @author yla
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
	public void save(Brand Brand) {
		brandRepository.save(Brand);
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
