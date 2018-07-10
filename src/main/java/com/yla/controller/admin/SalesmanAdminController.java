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

import com.yla.entity.Salesman;
import com.yla.entity.Log;
import com.yla.service.SalesmanService;
import com.yla.service.LogService;

/**
 * 后台管理业务员Controller
 * @author yla 小锋 老师
 *
 */
@RestController
@RequestMapping("/admin/salesman")
public class SalesmanAdminController {
	
	@Resource
	private SalesmanService salesmanService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 分页查询业务员信息
	 * @param salesman
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "业务员管理" })
	public Map<String,Object> list(Salesman salesman,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		List<Salesman> salesmanList=salesmanService.list(salesman, page, rows, Direction.ASC, "id");
		Long total=salesmanService.getCount(salesman);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", salesmanList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询业务员信息")); // 写入日志
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
	public List<Salesman> comboList(String q)throws Exception{
		if(q==null){
			q="";
		}
		return salesmanService.findByName("%"+q+"%");
	}
	
	
	/**
	 * 添加或者修改业务员信息
	 * @param salesman
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "业务员管理" })
	public Map<String,Object> save(Salesman salesman)throws Exception{
		if(salesman.getId()!=null){ // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION,"更新业务员信息"+salesman)); 
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加业务员信息"+salesman)); 
		}
		Map<String, Object> resultMap = new HashMap<>();
		salesmanService.save(salesman);			
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/**
	 * 删除业务员信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "业务员管理" })
	public Map<String,Object> delete(String ids)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			logService.save(new Log(Log.DELETE_ACTION,"删除业务员信息"+salesmanService.findById(id)));  // 写入日志
			salesmanService.delete(id);							
		}
		resultMap.put("success", true);
		return resultMap;
	}

}
