package com.yla.controller;

import org.springframework.ui.Model;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yla.entity.Log;
import com.yla.entity.Menu;
import com.yla.entity.Role;
import com.yla.entity.User;
import com.yla.service.LogService;
import com.yla.service.MenuService;
import com.yla.service.RoleService;
import com.yla.service.UserService;
import com.yla.util.StringUtil;

/**
 * 当前登录用户控制器
 * @author
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private RoleService roleService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private MenuService menuService;
	
	@Resource
	private LogService logService;
	
	/**
     * 用户登录请求
     * @param user
     * @return
     * 
     * 参数：@Valid User user表明要对实体user进行检验
     * 参数：BindingResult 对user检验的结果会存到该实体中
     * （User实体的userName属性标有@NotEmpty(message="请输入用户名！")
     * 表示如果userName为空，则将"请输入用户名！"存到BindingResult中）
     * 
     */
    @ResponseBody
    @PostMapping("/login")
    public Map<String,Object> login(String imageCode,@Valid User user,BindingResult bindingResult,HttpSession session){
    	Map<String,Object> map=new HashMap<String,Object>();
    	if(StringUtil.isEmpty(imageCode)){
    		map.put("success", false);
    		map.put("errorInfo", "请输入验证码！");
    		return map;
    	}
    	
    	 //checkcode是在控制器DrawImageController（用于生成验证码）中的drawImage（）方法中set进去的
    	/*
    	if(!session.getAttribute("checkcode").equals(imageCode)){
    		map.put("success", false);
    		map.put("errorInfo", "验证码输入错误！");
    		return map;
    	}
    	*/
    	if(bindingResult.hasErrors()){//对user进行检验的结果
    		map.put("success", false);
    		map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());//该信息就是在User实体属性上标注的@NotEmpty的信息
    		//System.out.println("haha"+bindingResult.getFieldError().getDefaultMessage());
    		return map;
    	}
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken(user.getUserName(), user.getPassword());
		try{
			
			subject.login(token); // 登录认证
			String userName=(String) SecurityUtils.getSubject().getPrincipal();
			User currentUser=userService.findByUserName(userName);
			session.setAttribute("currentUser", currentUser); //保存当前用户信息
			List<Role> roleList=roleService.findByUserId(currentUser.getId());
			map.put("roleList", roleList);
			map.put("roleSize", roleList.size());
			map.put("success", true);
			logService.save(new Log(Log.LOGIN_ACTION,"用户登录")); // 写入日志
			return map;
			
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("errorInfo", "用户名或者密码错误！");
			return map;
		}
    }
    
    /**
     * 保存角色信息
     * @param roleId
     * @param session
     * @return
     * @throws Exception
     * 
     * 保存当前用户的角色
     */
    @ResponseBody
    @PostMapping("/saveRole")
    public Map<String,Object> saveRole(Integer roleId,HttpSession session)throws Exception{
    	Map<String,Object> map=new HashMap<String,Object>();
    	Role currentRole=roleService.findById(roleId);
    	session.setAttribute("currentRole", currentRole); // 保存当前角色信息
    	map.put("success", true);
    	return map;
    }
    
    /**
     * 加载当前用户信息
     * @param session
     * @return
     * @throws Exception
     * 
     * 从session中获取当前用户和角色
     */
    @ResponseBody
    @GetMapping("/loadUserInfo")
    public String loadUserInfo(HttpSession session)throws Exception{
    	User currentUser=(User) session.getAttribute("currentUser");
    	Role currentRole=(Role) session.getAttribute("currentRole");
    	return "欢迎您："+currentUser.getTrueName()+"&nbsp;[&nbsp;"+currentRole.getName()+"&nbsp;]";
    }
    
    /**
     * 加载权限菜单
     * @param session
     * @return
     * @throws Exception
     * 
     * 菜单的格式（json格式）：
     * [{"id":1,"text":"系统菜单","state":"closed","iconCls":"menu-plugin","attributes":{"url":null},
     * "children":[{"id":20,"text":"销售管理","state":"closed","iconCls":"menu-2","attributes":{"url":null},
     * "children":[{"id":2010,"text":"销售出库","state":"open","iconCls":"menu-21","attributes":{"url":"/sale/saleout.html"}},
     * {"id":2020,"text":"客户退货","state":"open","iconCls":"menu-22","attributes":{"url":"/sale/salereturn.html"}},
     * {"id":2030,"text":"销售单据查询","state":"open","iconCls":"menu-23","attributes":{"url":"/sale/saleSearch.html"}},
     * {"id":2040,"text":"客户退货查询","state":"open","iconCls":"menu-24","attributes":{"url":"/sale/returnSearch.html"}},
     * {"id":2050,"text":"当前库存查询","state":"open","iconCls":"menu-25","attributes":{"url":"/common/stockSearch.html"}}]},
     * {"id":60,"text":"系统管理","state":"closed","iconCls":"menu-6","attributes":{"url":null},
     * "children":[{"id":6040,"text":"修改密码","state":"open","iconCls":"menu-63","attributes":{"url":null}},
     * {"id":6050,"text":"安全退出","state":"open","iconCls":"menu-64","attributes":{"url":null}}]}]}]
     * 
     * 
     * 说明：必须按以上格式和属性名来设置，因为这是easyUI规定的（可参考easyUI的官网）
     */
    @ResponseBody
    @PostMapping("/loadMenuInfo")
    public String loadMenuInfo(HttpSession session,Integer parentId)throws Exception{
    	Role currentRole=(Role) session.getAttribute("currentRole");
    	return getAllMenuByParentId(parentId,currentRole.getId()).toString();
    }
    
    /**
     * 获取所有菜单信息
     * @param parentId
     * @param roleId
     * @return
     */
    private JsonArray getAllMenuByParentId(Integer parentId,Integer roleId){
    	//查询对应父节点菜单下的子菜单集（只一层），结果被封装为json格式
    	JsonArray jsonArray=this.getMenuByParentId(parentId, roleId);
    	for(int i=0;i<jsonArray.size();i++){//遍历子菜单集，如果是根节点菜单则需要继续获取它的下一级菜单
    		JsonObject jsonObject=(JsonObject) jsonArray.get(i);
    		if("open".equals(jsonObject.get("state").getAsString())){//state为open代表叶子节点
    			continue;
    		}else{//根节点，需要继续获取对应菜单子集
    			jsonObject.add("children", getAllMenuByParentId(jsonObject.get("id").getAsInt(),roleId));
    		}
    	}
    	return jsonArray;
    }
    
    /**
     * 根据父节点和用户角色id查询菜单
     * @param parentId
     * @param roleId
     * @return
     * 
     * 这里只查询对应父节点菜单下的子菜单集（只一层）
     */
    private JsonArray getMenuByParentId(Integer parentId,Integer roleId){
    	//根据菜单的p_id和角色id查询出菜单集合
    	List<Menu> menuList=menuService.findByParentIdAndRoleId(parentId, roleId);
    	JsonArray jsonArray=new JsonArray();
    	for(Menu menu:menuList){//遍历菜单集合，将每个菜单实例封装成json格式（按easyUI规定的格式，easyUI树形菜单规定的，可参考easyUI官网）
    		JsonObject jsonObject=new JsonObject();
    		jsonObject.addProperty("id", menu.getId()); // 节点id
    		jsonObject.addProperty("text", menu.getName()); // 节点名称
    		if(menu.getState()==1){//数据库中state==1代表是根节点菜单
    			jsonObject.addProperty("state", "closed"); // 根节点
    		}else{
    			jsonObject.addProperty("state", "open"); // 叶子节点
    		}
    		jsonObject.addProperty("iconCls", menu.getIcon());
    		JsonObject attributeObject=new JsonObject(); // 扩展属性
    		attributeObject.addProperty("url", menu.getUrl()); // 菜单请求地址
			jsonObject.add("attributes", attributeObject);
			jsonArray.add(jsonObject);
    	}
    	return jsonArray;
    }
    
    
    
    
    
    @RequestMapping("/hello")
    public String hello(Model m) {
        m.addAttribute("name", "thymeleaf");
        return "hello";
    }
    
    
    
    
}
