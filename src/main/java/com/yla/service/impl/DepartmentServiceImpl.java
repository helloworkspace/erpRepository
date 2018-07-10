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

import com.yla.entity.Department;
import com.yla.repository.DepartmentRepository;
import com.yla.service.DepartmentService;
import com.yla.util.StringUtil;

/**
 * 公司单位Service实现类
 * @author yla 小锋 老师
 *
 */
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService{

	@Resource
	private DepartmentRepository departmentRepository;
	

	@Override
	public void save(Department department) {
		departmentRepository.save(department);
	}

	@Override
	public List<Department> list(Department department, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Department> pageDepartment=departmentRepository.findAll(new Specification<Department>() {
			
			@Override
			public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(department!=null){
					if(StringUtil.isNotEmpty(department.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+department.getName().trim()+"%"));
					}	
				}
				return predicate;
			}
		}, pageable);
		return pageDepartment.getContent();
	}

	@Override
	public Long getCount(Department department) {
		Long count=departmentRepository.count(new Specification<Department>() {

			@Override
			public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(department!=null){
					if(StringUtil.isNotEmpty(department.getName())){
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+department.getName().trim()+"%"));
					}	
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		departmentRepository.delete(id);
	}

	@Override
	public Department findById(Integer id) {
		return departmentRepository.findOne(id);
	}

	@Override
	public List<Department> findByName(String name) {
		return departmentRepository.findByName(name);
	}


}
