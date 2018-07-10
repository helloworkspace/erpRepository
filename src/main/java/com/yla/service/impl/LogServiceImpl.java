package com.yla.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.yla.entity.Log;
import com.yla.repository.LogRepository;
import com.yla.repository.UserRepository;
import com.yla.service.LogService;
import com.yla.util.StringUtil;

/**
 * 系统日志Service实现类
 * @author
 * 
 */
@Service("logService")
public class LogServiceImpl implements LogService{

	@Resource
	private LogRepository logRepository;
	
	@Resource
	private UserRepository userRepository;
	
	@Override
	public void save(Log log) {
		log.setTime(new Date()); // 设置操作日期
		log.setUser(userRepository.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置用户名
		logRepository.save(log);
	}

	@Override
	public List<Log> list(Log log, Integer page, Integer pageSize, Direction direction, String... properties) {
		Pageable pageable=new PageRequest(page-1, pageSize, direction,properties);
		Page<Log> pageLog=logRepository.findAll(new Specification<Log>(){

			@Override
			public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(log!=null){//根据用户姓名进行模糊查询
					if(log.getUser()!=null && StringUtil.isNotEmpty(log.getUser().getTrueName())){
						predicate.getExpressions().add(cb.like(root.get("user").get("trueName"), "%"+log.getUser().getTrueName()+"%"));
					}
					if(StringUtil.isNotEmpty(log.getType())){//根据日志的类型进行查询
						predicate.getExpressions().add(cb.equal(root.get("type"), log.getType()));
					}
					if(log.getBtime()!=null){//查询大于开始时间的日志
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("time"), log.getBtime()));
					}
					if(log.getEtime()!=null){//查询小于结束时间的日志
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("time"), log.getEtime()));
					}
				}
				return predicate;
			}
		  },pageable);
		return pageLog.getContent();
	}

	@Override
	public Long getCount(Log log) {
		Long count=logRepository.count(new Specification<Log>() {

			@Override
			public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(log!=null){
					if(log.getUser()!=null && StringUtil.isNotEmpty(log.getUser().getTrueName())){
						predicate.getExpressions().add(cb.like(root.get("user").get("trueName"), "%"+log.getUser().getTrueName()+"%"));
					}
					if(StringUtil.isNotEmpty(log.getType())){
						predicate.getExpressions().add(cb.equal(root.get("type"), log.getType()));
					}
					if(log.getBtime()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("time"), log.getBtime()));
					}
					if(log.getEtime()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("time"), log.getEtime()));
					}
				}
				return predicate;
			}
		});
		return count;
	}

}
