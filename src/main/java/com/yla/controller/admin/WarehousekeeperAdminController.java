package com.yla.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yla.entity.Warehousekeeper;
import com.yla.entity.Log;
import com.yla.service.WarehousekeeperService;
import com.yla.service.LogService;

/**
 * 后台管理仓管Controller
 * @author yla 小锋 老师
 *
 */
@RestController
@RequestMapping("/admin/warehousekeeper")
public class WarehousekeeperAdminController {
	
	@Resource
	private WarehousekeeperService warehousekeeperService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 分页查询仓管信息
	 * @param warehousekeeper
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(Warehousekeeper warehousekeeper,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		List<Warehousekeeper> warehousekeeperList=warehousekeeperService.list(warehousekeeper, page, rows, Direction.ASC, "id");
		Long total=warehousekeeperService.getCount(warehousekeeper);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", warehousekeeperList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询仓管信息")); // 写入日志
		return resultMap;
	}
	
	/**
	 * 下拉框模糊查询
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/comboList")
	public List<Warehousekeeper> comboList(String q)throws Exception{
		if(q==null){
			q="";
		}
		return warehousekeeperService.findByName("%"+q+"%");
	}
	
	
	/**
	 * 添加或者修改仓管信息
	 * @param warehousekeeper
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String,Object> save(Warehousekeeper warehousekeeper)throws Exception{
		if(warehousekeeper.getId()!=null){ // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION,"更新仓管信息"+warehousekeeper)); 
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加仓管信息"+warehousekeeper)); 
		}
		Map<String, Object> resultMap = new HashMap<>();
		warehousekeeperService.save(warehousekeeper);			
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/**
	 * 删除仓管信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(String ids)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			logService.save(new Log(Log.DELETE_ACTION,"删除仓管信息"+warehousekeeperService.findById(id)));  // 写入日志
			warehousekeeperService.delete(id);							
		}
		resultMap.put("success", true);
		return resultMap;
	}

}
