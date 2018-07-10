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

import com.yla.entity.Log;
import com.yla.entity.Buyer;
import com.yla.service.LogService;
import com.yla.service.BuyerService;

/**
 * 后台管理采购员Controller
 * @author 
 *
 */
@RestController
@RequestMapping("/admin/buyer")
public class BuyerAdminController {
	
	@Resource
	private BuyerService buyerService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 分页查询采购员信息
	 * @param buyer
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "采购员管理" })
	public Map<String,Object> list(Buyer buyer,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		List<Buyer> buyerList=buyerService.list(buyer, page, rows, Direction.ASC, "id");
		Long total=buyerService.getCount(buyer);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", buyerList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询采购员信息")); // 写入日志
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
	public List<Buyer> comboList(String q)throws Exception{
		if(q==null){
			q="";
		}
		return buyerService.findByName("%"+q+"%");
	}
	
	
	
	/**
	 * 添加或者修改采购员信息
	 * @param buyer
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "采购员管理" })
	public Map<String,Object> save(Buyer buyer)throws Exception{
		if(buyer.getId()!=null){ // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION,"更新采购员信息"+buyer)); 
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加采购员信息"+buyer)); 
		}
		Map<String, Object> resultMap = new HashMap<>();
		buyerService.save(buyer);			
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/**
	 * 删除采购员信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "采购员管理" })
	public Map<String,Object> delete(String ids)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			logService.save(new Log(Log.DELETE_ACTION,"删除采购员信息"+buyerService.findById(id)));  // 写入日志
			buyerService.delete(id);							
		}
		resultMap.put("success", true);
		return resultMap;
	}

}
