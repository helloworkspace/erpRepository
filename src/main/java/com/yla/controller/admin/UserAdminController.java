package com.yla.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yla.entity.Log;
import com.yla.entity.Role;
import com.yla.entity.User;
import com.yla.entity.UserRole;
import com.yla.service.LogService;
import com.yla.service.RoleService;
import com.yla.service.UserRoleService;
import com.yla.service.UserService;
import com.yla.util.StringUtil;

/**
 * 后台管理用户Controller
 * @author  
 *
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController {

	@Resource
	private UserService userService;
	
	@Resource
	private RoleService roleService;
	    
	@Resource
	private UserRoleService userRoleService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 修改密码
	 * @param id
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping("/modifyPassword")
	@RequiresPermissions(value = { "修改密码" })
	public Map<String,Object> modifyPassword(Integer id,String newPassword,HttpSession session)throws Exception{
		User currentUser=(User) session.getAttribute("currentUser");
		User user=userService.findById(currentUser.getId());
		user.setPassword(newPassword);
		userService.save(user);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("success", true);
		logService.save(new Log(Log.UPDATE_ACTION,"修改密码")); // 写入日志
		return map;
	}
	
	/**
	 * 安全退出
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/logout")
	@RequiresPermissions(value = { "安全退出" })
	public String logout()throws Exception{
		logService.save(new Log(Log.LOGOUT_ACTION,"用户注销"));
		SecurityUtils.getSubject().logout();
		return "redirect:/login.html";
	}
	
	/**
	 * 分页查询用户信息
	 * @param user 该参数在根据用户名查询用户列表时才会用到
	 * @param page 代表第几页（前端传过来的数据是从1开始的）
	 * @param rows 代表一页多少行
	 * @return map 返回传到客户端的格式为：{"total":2,
	 *                  "rows":[{"id":2,"userName":"jack","password":"123","trueName":"张三",
	 *                  "remarks":"","roles":"销售员,采购员"}]}
	 * @RequestParam value="page"指要绑定到的请求参数的名称。required=false 表示并不一定需要该参数，默认为true
	 * 
	 * @throws Exception
	 * 
	 * 实现步骤：
	 * 1.根据分页信息和查询条件查询出用户集合
	 * 2.遍历用户列表，取出每个用户对应的角色，并设置到对应用户上
	 * 3.查询用户集合的总数
	 * 4.将数据封装成easyUI规定的格式
	 * 5.将数据传到浏览器
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions(value = { "用户管理" })
	public Map<String,Object> list(User user, @RequestParam(value="page",required=false)Integer page, @RequestParam(value="rows",required=false)Integer rows)throws Exception{
		System.out.println("name "+user.getUserName());
		//Direction.ASC表示按正序排序；
		//"id" 表示根据id查询
		List<User> userList=userService.list(user, page, rows, Direction.ASC, "id");
		//遍历用户列表，取出每个用户对应的角色，set到对应用户的roles属性
		for(User u:userList){
			List<Role> roleList=roleService.findByUserId(u.getId());
			StringBuffer sb=new StringBuffer();
			for(Role r:roleList){
				sb.append(","+r.getName());
			}
			u.setRoles(sb.toString().replaceFirst(",", ""));
		}
		//查询出用户列表数据的总数（参数user是在根据用户信息查找时才有用）
		Long total=userService.getCount(user);
	    System.out.println("total"+total);
		Map<String, Object> resultMap = new HashMap<>();
		//要设置成total和rows，这是easyUI规定的。
		//total代表数据总数；rows代表表格显示的数据集
		resultMap.put("rows", userList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询用户信息")); // 写入日志
		return resultMap;
	}
	
	/**
	 * 保存用户角色设置
	 * @param roleIds: 浏览器传过来的该用户将保存的角色Id的字符串，有可能有多个，用逗号隔开。如："2,3,4"
	 * @param userId: 用户的id
	 * @return
	 * @throws Exception
	 * 
	 * 
	 * 实现步骤：
	 * 1.根据用户id删除原来该用户的所有角色
	 * 2.遍历新角色id组（roleIds），将用户与他的新角色id组（roleIds）关联起来
	 * 3.返回：("success", true)
	 */
	@ResponseBody
	@RequestMapping("/saveRoleSet")
	@RequiresPermissions(value = { "用户管理" })
	public Map<String,Object> saveRoleSet(String roleIds,Integer userId)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		userRoleService.deleteByUserId(userId);  // 根据用户id删除所有用户角色关联实体
		if(StringUtil.isNotEmpty(roleIds)){
			String idsStr[]=roleIds.split(",");
			for(int i=0;i<idsStr.length;i++){ // 然后添加所有用户角色关联实体
				UserRole userRole=new UserRole();
				userRole.setUser(userService.findById(userId));
				userRole.setRole(roleService.findById(Integer.parseInt(idsStr[i])));
				userRoleService.save(userRole);
			}
		}
		resultMap.put("success", true);
		logService.save(new Log(Log.UPDATE_ACTION,"保存用户角色设置")); 
		return resultMap;
	}
	
	
	/**
	 * 添加或者修改用户信息
	 * @param user ：当是更新用户时，参数user会有id；增加用户则没有
	 * @return
	 * @throws Exception
	 * 
	 * 说明：用户的增加和更新都是调用该方法，
	 *     同样也都调用service层的userService.save(user)方法
	 *     它是根据user的id是否为空来判断更新操作还是增加操作：
	 *     如果user的id为空则为增加用户，不空则为更新用户
	 * 
	 * 实现步骤：
	 * 1.如果增加用户时,即用户id为空，但用户名不为空，则判断用户名已经存在，增加失败
	 * 2.调用save()函数保存
	 * 注意：写入日志需根据id的不同来写入不同的日志
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions(value = { "用户管理" })
	public Map<String,Object> save(User user)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		if(user.getId()==null){
			if(userService.findByUserName(user.getUserName())!=null){
				resultMap.put("success", false);
				resultMap.put("errorInfo", "用户名已经存在!");
				return resultMap;
			}
		}
		if(user.getId()!=null){ // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION,"更新用户信息"+user)); 
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加用户信息"+user)); 
		}
		userService.save(user);			
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/**
	 * 删除用户信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * 
	 * 实现步骤：
	 * 1.删除用户与角色关联的信息
	 * 2.删除用户信息
	 * 3.返回结果
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "用户管理" })
	public Map<String,Object> delete(Integer id)throws Exception{
		logService.save(new Log(Log.DELETE_ACTION,"删除用户信息"+userService.findById(id)));  // 写入日志
		Map<String, Object> resultMap = new HashMap<>();
		userRoleService.deleteByUserId(id); // 删除用户角色关联信息
		userService.delete(id);				
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	
	
	/**
	 * 测试
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping("/test")
	@RequiresPermissions(value = { "用户管理" })
	public Map<String,Object> test()throws Exception{
		System.out.println("hello");
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	
	
	
	
	
	
	
}
