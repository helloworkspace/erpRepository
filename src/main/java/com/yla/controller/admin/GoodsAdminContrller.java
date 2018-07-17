package com.yla.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yla.entity.Goods;
import com.yla.entity.GoodsType;
import com.yla.entity.Log;
import com.yla.service.GoodsService;
import com.yla.service.GoodsTypeService;
import com.yla.service.LogService;
import com.yla.util.StringUtil;

/**
 * 后台管理商品Controller
 * 
 *
 */
@RestController
@RequestMapping("/admin/goods")
public class GoodsAdminContrller {

	@Resource
	private GoodsService goodsService;
	
	@Resource
	private GoodsTypeService goodsTypeService;
	
	
	
	@Resource
	private LogService logService;
	
	/**
	 * 根据条件分页查询商品信息
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(Goods goods,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> goodsList=goodsService.list(goods, page, rows, Direction.ASC, "id");
		Long total=goodsService.getCount(goods);
		resultMap.put("rows", goodsList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询商品信息")); // 写入日志
		return resultMap;
	}
	
	/*
	 * 商品类别
	 */
	
	@RequestMapping("/goodsList")
	public Map<String,Object> goodslist(Goods goods,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		//根据条件查询出所有产品
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> goodsList=goodsService.list(goods, page, rows, Direction.ASC, "id");
		//获得商品分类的id的集合
		List<Integer> typeList = getGoodsTypeIdList(2);
		
		//过滤出属于该分类下的产品
		List<Goods> list = new ArrayList<Goods>();
		for(int i=0; i<goodsList.size(); i++){
			if(typeContains(typeList, goodsList.get(i).getType().getId())){
				list.add(goodsList.get(i));
			}
		}
		Long total = Integer.valueOf(list.size()).longValue();
		resultMap.put("rows", list);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询商品信息")); // 写入日志
		return resultMap;
	}
	
	
	@RequestMapping("/otherList")
	public Map<String,Object> otherlist(Goods goods,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		//根据条件查询出所有物品
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> goodsList=goodsService.list(goods, page, rows, Direction.ASC, "id");
		//获得物品分类的id的集合
		List<Integer> typeList = getGoodsTypeIdList(3);		//改这里
		
		//过滤出属于该分类下的物品
		List<Goods> list = new ArrayList<Goods>();
		for(int i=0; i<goodsList.size(); i++){
			if(typeContains(typeList, goodsList.get(i).getType().getId())){
				list.add(goodsList.get(i));
			}
		}
		Long total = Integer.valueOf(list.size()).longValue();
		resultMap.put("rows", list);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询物品信息")); // 写入日志
		return resultMap;
	}
	
	
	
	
	
	/**
	 * 根据条件分页查询商品库存信息
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	/*
	@RequestMapping("/listInventory")
	public Map<String,Object> listInventory(Goods goods,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> goodsList=goodsService.list(goods, page, rows, Direction.ASC, "id");
		Long total=goodsService.getCount(goods);
		for(Goods g:goodsList){
			g.setSaleTotal(saleListGoodsService.getTotalByGoodsId(g.getId())-customerReturnListGoodsService.getTotalByGoodsId(g.getId())); // 设置销量总数
		}
		resultMap.put("rows", goodsList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询商品库存信息")); // 写入日志
		return resultMap;
	}
	*/
	/**
	 * 查询库存报警商品
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listAlarm")
	public Map<String,Object> listAlart()throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> alarmGoodsList=goodsService.listAlarm();
		resultMap.put("rows", alarmGoodsList);
		return resultMap;
	}
	
	/**
	 * 根据条件分页查询没有库存的商品信息
	 * @param codeOrName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listNoInventoryQuantity")
	public Map<String,Object> listNoInventoryQuantity(@RequestParam(value="codeOrName",required=false)String codeOrName,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> goodsList=goodsService.listNoInventoryQuantityByCodeOrName(codeOrName, page, rows, Direction.ASC, "id");
		Long total=goodsService.getCountNoInventoryQuantityByCodeOrName(codeOrName);
		resultMap.put("rows", goodsList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询商品信息（无库存）")); // 写入日志
		return resultMap;
	}
	
	/**
	 * 分页查询有库存的商品信息
	 * @param codeOrName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listHasInventoryQuantity")
	public Map<String,Object> listHasInventoryQuantity(@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> goodsList=goodsService.listHasInventoryQuantity(page, rows, Direction.ASC, "id");
		Long total=goodsService.getCountHasInventoryQuantity();
		resultMap.put("rows", goodsList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询商品信息（有库存）")); // 写入日志
		return resultMap;
	}
	
	/**
	 * 删除库存 把商品的库存设置成0
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteStock")
	public Map<String,Object> deleteStock(Integer id)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		Goods goods=goodsService.findById(id);
		if(goods.getState()==2){ // 2表示有进货或者销售单据 不能删除
			resultMap.put("success", false);
			resultMap.put("errorInfo", "该商品已经发生单据，不能删除！");
		}else{
			goods.setInventoryQuantity(0);
			goodsService.save(goods);
			resultMap.put("success", true);	
		}
		return resultMap;			
	}
	
	/**
	 * 生成商品编码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/genGoodsCode")
	public String genGoodsCode()throws Exception{
		String maxGoodsCode=goodsService.getMaxGoodsCode();
		if(StringUtil.isNotEmpty(maxGoodsCode)){
			Integer code = Integer.valueOf(maxGoodsCode)+1;
			String codes = code.toString();
			int length = codes.length();
			for (int i = 4; i > length; i--) {
				codes = "0"+codes;
			}
			return codes;
		}else{
			return "0001";
		}
	}
	
	/**
	 * 添加商品
	 * @param goods
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String,Object> save(Goods goods)throws Exception{
		if(goods.getId()!=null){ // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION,"更新商品信息"+goods)); 
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加商品信息"+goods)); 
		}
		Map<String, Object> resultMap = new HashMap<>();
		goodsService.save(goods);
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/**
	 * 添加商品到仓库 修改库存信息
	 * @param id
	 * @param num
	 * @param price
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveStore")
	public Map<String,Object> saveStore(Integer id,Integer num,Float price)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		Goods goods=goodsService.findById(id);
		goods.setInventoryQuantity(num);
		goods.setPurchasingPrice(price);
		goodsService.save(goods);
		logService.save(new Log(Log.UPDATE_ACTION,"修改商品"+goods+"，价格="+price+",库存="+num)); // 写入日志
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/**
	 * 删除商品信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(Integer id)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		Goods goods=goodsService.findById(id);
		if(goods.getState()==1){
			resultMap.put("success", false);
			resultMap.put("errorInfo", "该商品已经期初入库，不能删除！");
		}else if(goods.getState()==2){
			resultMap.put("success", false);
			resultMap.put("errorInfo", "该商品已经发生单据，不能删除！");
		}else{
			logService.save(new Log(Log.DELETE_ACTION,"删除商品信息"+goodsService.findById(id)));  // 写入日志
			goodsService.delete(id);							
			resultMap.put("success", true);			
		}
		return resultMap;
	}
	
	
	/*
	 * 作用：根据分类id获取它的所有子类分类集合
	 * 参数：分类id
	 * 返回：该id 子类的id集合
	 */
	public List<Integer> getGoodsTypeIdList(Integer parentId){
		List<Integer> list = new ArrayList<Integer>();
		return getTypeIdList(parentId, list);
	}
	
	
	
	/*
	 * 作用：根据分类id获取它的所有子类分类集合
	 * 参数：分类id
	 * 返回：该id 子类的id集合
	 */
	private List<Integer> getTypeIdList(Integer parentId, List<Integer> list){
		List<GoodsType> childTypeList = goodsTypeService.findByParentId(parentId);
		for(int i=0; i<childTypeList.size(); i++){
			GoodsType type = childTypeList.get(i);
			list.add(type.getId());
			if(type.getState() == 0){
				continue;
			}else{
				getTypeIdList(type.getId(), list);
			}
		}
		return list;
	}
	
	
	/*
	 * 判断
	 */
	private boolean typeContains(List<Integer> list, Integer num){
		if(list == null)
			return false;
		for(int i=0; i<list.size(); i++){
			if(list.get(i).intValue() == num.intValue()){
				return true;
			}
		}
		return false;
	}
	
}
