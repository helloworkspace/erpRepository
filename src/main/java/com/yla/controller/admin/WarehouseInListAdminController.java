package com.yla.controller.admin;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yla.entity.Log;
import com.yla.entity.WarehouseInList;
import com.yla.entity.WarehouseInListGoods;
import com.yla.service.LogService;
import com.yla.service.WarehouseInListGoodsService;
import com.yla.service.WarehouseInListService;
import com.yla.service.UserService;
import com.yla.util.DateUtil;
import com.yla.util.StringUtil;

/**
 * 入库单Controller类
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/admin/warehouseInList")
public class WarehouseInListAdminController {

	@Resource
	private WarehouseInListService warehouseInListService;
	
	@Resource
	private WarehouseInListGoodsService warehouseInListGoodsService;
	
	@Resource
	private LogService logService;
	
	@Resource
	private UserService userService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
	}
	
	/**
	 * 根据条件分页查询退货单信息
	 * @param warehouseInList
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(WarehouseInList warehouseInList)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<WarehouseInList> warehouseInListList=warehouseInListService.list(warehouseInList, Direction.DESC, "inNumber");
		resultMap.put("rows", warehouseInListList);
		return resultMap;
	}
	
	/**
	 * 根据退货单id查询所有退货单商品
	 * @param warehouseInListId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listGoods")
	public Map<String,Object> listGoods(Integer warehouseInListId)throws Exception{
		if(warehouseInListId==null){
			return null;
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<WarehouseInListGoods> warehouseInListGoodsList=warehouseInListGoodsService.listByWarehouseInListId(warehouseInListId);
		resultMap.put("rows", warehouseInListGoodsList);
		return resultMap;
	}
	
	/**
	 * 客户统计 获取退货单的所有商品信息
	 * @param purchaseList
	 * @param purchaseListGoods
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listCount")
	public Map<String,Object> listCount(WarehouseInList warehouseInList,WarehouseInListGoods warehouseInListGoods)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<WarehouseInList> warehouseInListList=warehouseInListService.list(warehouseInList, Direction.DESC, "returnDate");
		for(WarehouseInList pl:warehouseInListList){
			warehouseInListGoods.setWarehouseInList(pl);
			List<WarehouseInListGoods> rlgList=warehouseInListGoodsService.list(warehouseInListGoods);
			for(WarehouseInListGoods rlg:rlgList){
				rlg.setWarehouseInList(null);
			}
			pl.setWarehouseInListGoodsList(rlgList);
		}
		resultMap.put("rows", warehouseInListList);
		return resultMap;
	}
	
	/**
	 * 获取退货单号
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getInNumber")
	public String genBillCode(String type)throws Exception{
		StringBuffer biilCodeStr=new StringBuffer();
		biilCodeStr.append("JC");
		biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
		String inNumber=warehouseInListService.getTodayMaxInNumber(); // 获取当天最大的退货单号
		if(inNumber!=null){
			biilCodeStr.append(StringUtil.formatCode(inNumber));
		}else{
			biilCodeStr.append("0001");
		}
		return biilCodeStr.toString();
	}
	
	/**
	 * 添加退货单 以及所有退货单商品 以及 修改商品的成本均价
	 * @param warehouseInList
	 * @param goodsJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Map<String,Object> save(WarehouseInList warehouseInList,String goodsJson)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		warehouseInList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
		Gson gson = new Gson();
		List<WarehouseInListGoods> plgList=gson.fromJson(goodsJson, new TypeToken<List<WarehouseInListGoods>>(){}.getType());
		warehouseInListService.save(warehouseInList, plgList);
		logService.save(new Log(Log.ADD_ACTION,"添加退货单")); 
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/**
	 * 修改退货单的支付状态
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Map<String,Object> update(Integer id)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		WarehouseInList warehouseInList=warehouseInListService.findById(id);
		warehouseInList.setState(1); // 修改成支付状态
		warehouseInListService.update(warehouseInList);
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/**
	 * 根据id删除退货单信息 包括退货单里的商品
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(Integer id)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		warehouseInListService.delete(id);
		logService.save(new Log(Log.DELETE_ACTION,"删除退货单信息"+warehouseInListService.findById(id)));  // 写入日志
		resultMap.put("success", true);		
		return resultMap;
	}
}
