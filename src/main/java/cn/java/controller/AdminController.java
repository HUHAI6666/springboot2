
package cn.java.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.java.entity.Menu;
import cn.java.entity.User;
import cn.java.service.AdminService;
import cn.java.utils.SM3Digest;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/login")
    public ModelAndView toIndex() {// 配置默认访问首页
    	return new ModelAndView("login");
    }
    
    /**
     * 
     * Description: 后台登录方法<br/>
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/isLogin")
    @ResponseBody
/*    public boolean isLogin(String username, String password, HttpServletRequest request) {
    	System.out.println("welcome " + username);
    	SM3Digest sm3Digest=new SM3Digest();
		String pwd=sm3Digest.getEncrypt(password);
        if(adminService.isLogin(username, pwd)){
        	System.out.println("welcome " + username);
        	User user = new User();
        	user.setUsername(username);
        	user.setPassword(password);
        	request.getSession().setAttribute("session_user",user);
        	return true;
        }
        return false;
    
    }*/
    //配套序列化的params
    public boolean isLogin(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        SM3Digest sm3Digest=new SM3Digest();
		String pwd=sm3Digest.getEncrypt(params.get("password").toString());
        if(adminService.isLogin(params.get("name").toString(), pwd)){
        	System.out.println("welcome " + params.get("name"));
        	User user = new User();
        	user.setUsername(params.get("name").toString());
        	user.setPassword(params.get("password").toString());
        	request.getSession().setAttribute("session_user",user);
        	return true;
        }
        return false;
    }
    
    //退出
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //移除session
        request.getSession().invalidate();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() +request.getContextPath()+"/";
        return "redirect:" + basePath;
    }

    
    @RequestMapping(value = "/getAllNavs")
    @ResponseBody
    public List<Map<String, Object>> getAllNavs(@RequestParam(defaultValue = "0") Long id) {
		return adminService.setectNavs(id);
	}
    
    
    @RequestMapping(value = "/getAllMenu")
    @ResponseBody
    public Map<String, Object> getAllMenu(int page, int rows) {
		return adminService.setectMenu(page, rows);
	}
    
    @RequestMapping(value = "/addMenu",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addMenu(@RequestBody Menu menu) {
    	Map<String,  Object> resultMap = new HashMap<String, Object>();
    	String msg = adminService.addMenu(menu);
    	resultMap.put("result", msg);
    	return resultMap;
	}
    
    @RequestMapping(value = "/editMenu",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editMenu(@RequestBody Menu menu) {
    	Map<String,  Object> resultMap = new HashMap<String, Object>();
    	String msg = adminService.editMenu(menu);
    	resultMap.put("result", msg);
    	return resultMap;
	}
    
    @RequestMapping(value = "/removeMenu",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> removeMenu(@RequestBody List<Integer> parm) {
    	Map<String,  Object> resultMap = new HashMap<String, Object>();
    	System.out.println("str:"+parm);
    	String msg = adminService.removeMenu(parm);
    	resultMap.put("result", msg);
    	return resultMap;
	}
    
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addUser(@RequestBody User user) {
    	Map<String,  Object> resultMap = new HashMap<String, Object>();
    	SM3Digest sm3Digest=new SM3Digest();
        user.setPassword(sm3Digest.getEncrypt(user.getPassword()));
    	String msg = adminService.addUser(user);
    	resultMap.put("result", msg);
    	return resultMap;
	}
    
    @RequestMapping(value = "/getAllUser")
    @ResponseBody
    public Map<String, Object> getAllUser(int page, int rows) {
		return adminService.selectUser(page, rows);
	}

    //校验名字是否重复
    @RequestMapping(value = "/checkUsername",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkUsername(@RequestParam("username") String username) {
    	Map<String,  Object> resultMap = new HashMap<String, Object>();
    	String msg = adminService.checkUsername(username);
    	resultMap.put("result", msg);
		return resultMap;
	}
    
    @RequestMapping(value = "/editUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editUser(@RequestBody User user) {
        SM3Digest sm3Digest=new SM3Digest();
        user.setPassword(sm3Digest.getEncrypt(user.getPassword()));
    	Map<String,  Object> resultMap = new HashMap<String, Object>();
    	String msg = adminService.editUser(user);
    	resultMap.put("result", msg);
    	return resultMap;
	}

    @RequestMapping(value = "/removeUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> removeUser(@RequestBody List<Integer> parm) {
    	Map<String,  Object> resultMap = new HashMap<String, Object>();
    	System.out.println("str:"+parm);
    	String msg = adminService.removeUser(parm);
    	resultMap.put("result", msg);
    	return resultMap;
	}
    
}
