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
import com.yla.entity.WarehouseList;
import com.yla.entity.WarehouseListGoods;
import com.yla.service.LogService;
import com.yla.service.WarehouseListGoodsService;
import com.yla.service.WarehouseListService;
import com.yla.service.UserService;
import com.yla.util.DateUtil;
import com.yla.util.StringUtil;

/**
 * 退货单Controller类
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/admin/warehouseList")
public class WarehouseListAdminController {

	@Resource
	private WarehouseListService warehouseListService;
	
	@Resource
	private WarehouseListGoodsService warehouseListGoodsService;
	
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
	 * @param warehouseList
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(WarehouseList warehouseList)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<WarehouseList> warehouseListList=warehouseListService.list(warehouseList, Direction.DESC, "outNumber");
		resultMap.put("rows", warehouseListList);
		return resultMap;
	}
	
	/**
	 * 根据退货单id查询所有退货单商品
	 * @param warehouseListId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listGoods")
	public Map<String,Object> listGoods(Integer warehouseListId)throws Exception{
		if(warehouseListId==null){
			return null;
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<WarehouseListGoods> warehouseListGoodsList=warehouseListGoodsService.listByWarehouseListId(warehouseListId);
		resultMap.put("rows", warehouseListGoodsList);
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
	public Map<String,Object> listCount(WarehouseList warehouseList,WarehouseListGoods warehouseListGoods)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<WarehouseList> warehouseListList=warehouseListService.list(warehouseList, Direction.DESC, "returnDate");
		for(WarehouseList pl:warehouseListList){
			warehouseListGoods.setWarehouseList(pl);
			List<WarehouseListGoods> rlgList=warehouseListGoodsService.list(warehouseListGoods);
			for(WarehouseListGoods rlg:rlgList){
				rlg.setWarehouseList(null);
			}
			pl.setWarehouseListGoodsList(rlgList);
		}
		resultMap.put("rows", warehouseListList);
		return resultMap;
	}
	
	/**
	 * 获取退货单号
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getOutNumber")
	public String genBillCode(String type)throws Exception{
		StringBuffer biilCodeStr=new StringBuffer();
		biilCodeStr.append("CC");
		biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
		String outNumber=warehouseListService.getTodayMaxOutNumber(); // 获取当天最大的退货单号
		if(outNumber!=null){
			biilCodeStr.append(StringUtil.formatCode(outNumber));
		}else{
			biilCodeStr.append("0001");
		}
		return biilCodeStr.toString();
	}
	
	/**
	 * 添加退货单 以及所有退货单商品 以及 修改商品的成本均价
	 * @param warehouseList
	 * @param goodsJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Map<String,Object> save(WarehouseList warehouseList,String goodsJson)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		warehouseList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
		Gson gson = new Gson();
		List<WarehouseListGoods> plgList=gson.fromJson(goodsJson, new TypeToken<List<WarehouseListGoods>>(){}.getType());
		warehouseListService.save(warehouseList, plgList);
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
		WarehouseList warehouseList=warehouseListService.findById(id);
		warehouseList.setState(1); // 修改成支付状态
		warehouseListService.update(warehouseList);
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
		warehouseListService.delete(id);
		logService.save(new Log(Log.DELETE_ACTION,"删除退货单信息"+warehouseListService.findById(id)));  // 写入日志
		resultMap.put("success", true);		
		return resultMap;
	}
}
