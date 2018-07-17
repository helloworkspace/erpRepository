package com.yla.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yla.entity.Model;
import com.yla.entity.Log;
import com.yla.service.ModelService;
import com.yla.service.LogService;

/**
 * 后台管理商品规格Controller
 * @author yla
 *
 */
@RestController
@RequestMapping("/admin/model")
public class ModelAdminController {

	@Resource
	private ModelService modelService;
	
	@Resource
	private LogService logService;
	
	@RequestMapping("/comboList")		//显示下拉框列表
	@RequiresPermissions(value = { "商品管理" })
	public List<Model> comboList()throws Exception{
		return modelService.listAll();
	}
	
	/**
	 * 查询所有商品规格
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listAll")				//显示规格的那个表
	@RequiresPermissions(value = { "商品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> listAll()throws Exception{
		List<Model> modelList=modelService.listAll();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", modelList);
		logService.save(new Log(Log.SEARCH_ACTION,"查询商品规格信息")); // 写入日志
		return resultMap;
	}
	
	/**
	 * 添加商品规格
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "商品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> save(Model model)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		logService.save(new Log(Log.ADD_ACTION,"添加商品规格信息"+model)); 
		modelService.save(model);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 删除商品规格信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "商品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> delete(Integer id)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		logService.save(new Log(Log.DELETE_ACTION,"删除商品规格信息"+modelService.findById(id)));  // 写入日志
		modelService.delete(id);				
		resultMap.put("success", true);
		return resultMap;
	}
	
}