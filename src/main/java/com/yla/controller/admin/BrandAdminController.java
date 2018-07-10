package com.yla.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yla.entity.Brand;
import com.yla.entity.Log;
import com.yla.service.BrandService;
import com.yla.service.LogService;

/**
 * 后台管理商品单位Controller
 * @author yla 小锋 老师
 *
 */
@RestController
@RequestMapping("/admin/brand")
public class BrandAdminController {

	@Resource
	private BrandService brandService;
	
	@Resource
	private LogService logService;
	
	@RequestMapping("/comboList")
	@RequiresPermissions(value = { "商品管理" })
	public List<Brand> comboList()throws Exception{
		return brandService.listAll();
	}
	
	/**
	 * 查询所有商品单位
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listAll")
	public Map<String,Object> listAll()throws Exception{
		List<Brand> brandList=brandService.listAll();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", brandList);
		logService.save(new Log(Log.SEARCH_ACTION,"查询商品单位信息")); // 写入日志
		return resultMap;
	}
	
	/**
	 * 添加商品单位
	 * @param brand
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String,Object> save(Brand brand)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		logService.save(new Log(Log.ADD_ACTION,"添加商品单位信息"+brand)); 
		brandService.save(brand);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 删除商品单位信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(Integer id)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		logService.save(new Log(Log.DELETE_ACTION,"删除商品单位信息"+brandService.findById(id)));  // 写入日志
		brandService.delete(id);				
		resultMap.put("success", true);
		return resultMap;
	}
	
}
