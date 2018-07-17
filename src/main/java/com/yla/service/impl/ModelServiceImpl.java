package com.yla.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yla.entity.Model;
import com.yla.repository.ModelRepository;
import com.yla.service.ModelService;


/**
 * 商品规格Service实现类
 * @author yla
 *
 */
@Service("modelService")
public class ModelServiceImpl implements ModelService{

	@Resource
	private ModelRepository modelRepository;
	
	@Override
	public List<Model> listAll() {
		return modelRepository.findAll();
	}

	@Override
	public void save(Model Model) {
		modelRepository.save(Model);
	}

	@Override
	public void delete(Integer id) {
		modelRepository.delete(id);
	}

	@Override
	public Model findById(Integer id) {
		return modelRepository.findOne(id);
	}

}
