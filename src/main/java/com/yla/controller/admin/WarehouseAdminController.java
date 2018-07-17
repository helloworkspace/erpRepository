package com.yla.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yla.entity.Warehouse;
import com.yla.entity.Log;
import com.yla.service.WarehouseService;
import com.yla.service.LogService;

/**
 * 后台管理商品仓库Controller
 * @author yla 小锋 老师
 *
 */
@RestController
@RequestMapping("/admin/warehouse")
public class WarehouseAdminController {

	@Resource
	private WarehouseService warehouseService;
	
	@Resource
	private LogService logService;
	
	@RequestMapping("/comboList")		//显示下拉框列表
	@RequiresPermissions(value = { "商品管理" })
	public List<Warehouse> comboList()throws Exception{
		return warehouseService.listAll();
	}
	
	/**
	 * 查询所有商品仓库
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listAll")				//显示仓库的那个表
	@RequiresPermissions(value = { "商品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> listAll()throws Exception{
		List<Warehouse> warehouseList=warehouseService.listAll();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", warehouseList);
		logService.save(new Log(Log.SEARCH_ACTION,"查询商品仓库信息")); // 写入日志
		/**
		 * 注意注意
		 * 仓库要改成真正改变的，不然日志出错
		 */
		
		return resultMap;
	}
	
	/**
	 * 添加商品仓库
	 * @param warehouse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "商品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> save(Warehouse warehouse)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		logService.save(new Log(Log.ADD_ACTION,"添加商品仓库信息"+warehouse)); 
		warehouseService.save(warehouse);
		resultMap.put("success", true);
		return resultMap;
	}
	
	/**
	 * 删除商品仓库信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "商品管理","进货入库"},logical=Logical.OR)
	public Map<String,Object> delete(Integer id)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		logService.save(new Log(Log.DELETE_ACTION,"删除商品仓库信息"+warehouseService.findById(id)));  // 写入日志
		warehouseService.delete(id);				
		resultMap.put("success", true);
		return resultMap;
	}
	
}
