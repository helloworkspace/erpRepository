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

import com.yla.entity.Department;
import com.yla.entity.Log;
import com.yla.service.DepartmentService;
import com.yla.service.LogService;

/**
 * 后台管理领料部门Controller
 * @author yla 小锋 老师
 *
 */
@RestController
@RequestMapping("/admin/department")
public class DepartmentAdminController {
	
	@Resource
	private DepartmentService departmentService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 分页查询领料部门信息
	 * @param department
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(Department department,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		List<Department> departmentList=departmentService.list(department, page, rows, Direction.ASC, "id");
		Long total=departmentService.getCount(department);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", departmentList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询领料部门信息")); // 写入日志
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
	public List<Department> comboList(String q)throws Exception{
		if(q==null){
			q="";
		}
		return departmentService.findByName("%"+q+"%");
	}
	
	
	/**
	 * 添加或者修改领料部门信息
	 * @param department
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String,Object> save(Department department)throws Exception{
		if(department.getId()!=null){ // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION,"更新领料部门信息"+department)); 
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加领料部门信息"+department)); 
		}
		Map<String, Object> resultMap = new HashMap<>();
		departmentService.save(department);			
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/**
	 * 删除领料部门信息
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
			logService.save(new Log(Log.DELETE_ACTION,"删除领料部门信息"+departmentService.findById(id)));  // 写入日志
			departmentService.delete(id);							
		}
		resultMap.put("success", true);
		return resultMap;
	}

}
