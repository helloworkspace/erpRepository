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

import com.yla.entity.User;
import com.yla.repository.UserRepository;
import com.yla.service.UserService;
import com.yla.util.StringUtil;

/**
 * 用户Service实现类
 * @author 
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService{

	@Resource
	private UserRepository userRepository;
	
	
	@Override
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Override
	public User findById(Integer id) {
		return userRepository.findOne(id);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public List<User> list(User user, Integer page, Integer pageSize, Direction direction, String... properties) {
		/*
		 * Pageable是分页信息的抽象接口（封装分页查询的信息）
		 * 第一个参数（page-1）是从零开始的页面索引[因为界面传过来的页面参数是从1开始的，而该参数需从0开始，所以要-1]
		 * 第二个参数（pageSize）是要返回的页面的大小
		 * 第三个参数（direction）枚举排序的方向（正序或倒序）
		 * 第四个参数（properties）要排序的属性（如：根据id来排序）
		 */
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<User> pageUser=userRepository.findAll(new Specification<User>() {
			/*
			 *1.Root<User> root 用于获取实体的字段
			 *2.CriteriaBuilder cb 用于添加查询条件，如添加模糊查询
			 */
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(user!=null){//当根据用户名（用户信息）查询时，user才会发挥作用
					
					if(StringUtil.isNotEmpty(user.getUserName())){//根据用户名进行模糊查询
						predicate.getExpressions().add(cb.like(root.get("userName"), "%"+user.getUserName().trim()+"%"));
					}	
					predicate.getExpressions().add(cb.notEqual(root.get("id"), 1)); // 管理员除外
				}
				return predicate;
			}
		}, pageable);
		return pageUser.getContent();
	}

	@Override
	public Long getCount(User user) {
		Long count=userRepository.count(new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(user!=null){
					
					if(StringUtil.isNotEmpty(user.getUserName())){
						predicate.getExpressions().add(cb.like(root.get("userName"), "%"+user.getUserName().trim()+"%"));
					}	
					predicate.getExpressions().add(cb.notEqual(root.get("id"), 1)); // 管理员除外
				}
				return predicate;
			}
		});
		return count;
	}

	@Override
	public void delete(Integer id) {
		userRepository.delete(id);
	}
	
	

	@Override
	public Long getCount() {
		// TODO 自动生成的方法存根
		return userRepository.count();
	}
	


}
