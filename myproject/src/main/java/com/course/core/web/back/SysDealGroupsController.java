package com.course.core.web.back;

import static com.course.core.constant.Constants.CREATE;
import static com.course.core.constant.Constants.DELETE_SUCCESS;
import static com.course.core.constant.Constants.OPRT;
import static com.course.core.constant.Constants.SAVE_SUCCESS;
import static com.course.core.constant.Constants.EDIT;
import static com.course.core.constant.Constants.MESSAGE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.util.SQLServerDriver;
import com.course.common.web.Servlets;
import com.course.core.constant.Constants;
import com.course.core.domain.Org;
import com.course.core.domain.Site;
import com.course.core.domain.SysDeal;
import com.course.core.domain.SysDealGroups;
import com.course.core.domain.User;
import com.course.core.service.OperationLogService;
import com.course.core.service.SysDealGroupsService;
import com.course.core.service.SysDealService;
import com.course.core.support.CmsException;
import com.course.core.support.Context;
import com.course.core.support.Response;

/**
 * 
 * @Description 刷卡机组
 * @author wangfei
 * @date 2019年3月14日下午3:21:11
 * @version  1.0
 */
@Controller
@RequestMapping("/core/dealgroups")
public class SysDealGroupsController {
	private static final Logger logger=LoggerFactory.getLogger(SysDealGroupsController.class);
	
	/**
	 * 
	 * @Description 列表查询
	 * @author  wangfei
	 * @date  2019年3月14日下午4:05:35
	 * @param pageable
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequiresPermissions("core:dealgroups:list")
	@RequestMapping("list.do")
	public String list(@PageableDefault(sort="id",direction=Direction.DESC)Pageable pageable,
			HttpServletRequest request,HttpServletResponse response,org.springframework.ui.Model modelMap){
		Map<String,String[]> params=Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		Page<SysDealGroups> pagedList=service.findAll(params, pageable);
		modelMap.addAttribute("pagedList", pagedList);
		logService.operation("刷卡机组", "查询", null, null, request);
		logger.info("/core/sysdealgroups/list, username={}.", Context.getCurrentUserId());
		return "core/sysdealgroups/sysdealgroups_list";
	}
	
	/**
	 * 
	 * @Description 新增/修改
	 * @author  wangfei
	 * @date  2019年3月14日下午4:06:59
	 * @param id
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequiresPermissions("core:dealgroups:create")
	@RequestMapping("create.do")
	public String create(Integer id,HttpServletRequest request, org.springframework.ui.Model modelMap) {
		if (id != null) {
			SysDealGroups bean = service.get(id);
			modelMap.addAttribute("bean", bean);
			modelMap.addAttribute(OPRT, EDIT);
		}else{
			modelMap.addAttribute(OPRT, CREATE);	
		}
		logService.operation("刷卡机组", "新增", null, null, request);
		return "core/sysdealgroups/sysdealgroups_form";
	}

	/**
	 * 
	 * @Description 新增保存
	 * @author  wangfei
	 * @date  2019年3月14日下午4:11:42
	 * @param bean
	 * @param redirect
	 * @param request
	 * @param commonitemId
	 * @param ra
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequiresPermissions("core:dealgroups:save")
	@RequestMapping("save.do")
	public String save(SysDealGroups bean,String redirect, HttpServletRequest request,
			Integer commonitemId, RedirectAttributes ra, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Org org = Context.getCurrentOrg(request);
		bean.setOrg(org);

		User user = Context.getCurrentUser();
		bean.setCreator(user);
		bean.setCreated(new Date());
		service.save(bean);
		logService.operation("刷卡机组", "新增保存", null, null, request);
		logger.info("save SysDealGroups, ID={}.", bean.getId());
		return resp.post(1);
	}
	
	/**
	 * 
	 * @Description 修改保存
	 * @author  wangfei
	 * @date  2019年3月14日下午4:16:55
	 * @param bean
	 * @param position
	 * @param redirect
	 * @param commonitemId
	 * @param ra
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequiresPermissions("core:dealgroups:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") SysDealGroups bean,Integer position, String redirect,
			Integer commonitemId, RedirectAttributes ra, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Org org = Context.getCurrentOrg(request);
		if (!org.getTreeNumber().startsWith(orgTreeNumber)) {
			throw new CmsException("error.forbiddenData");
		}

		bean.setOrg(org);
		bean.setUpdated(new Date());
		User user = Context.getCurrentUser();
		bean.setUpdator(user);
		service.update(bean);
		logService.operation("刷卡机组", "修改保存", null, null, request);
		logger.info("save SysDealGroups, ID={}.", bean.getId());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return resp.post(1);
	}
	
	/**
	 * 删除
	 * 
	 * @param ids
	 * @param ra
	 * @return
	 */
	@RequiresPermissions("core:dealgroups:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,RedirectAttributes ra) {
		SysDealGroups[] beans = service.delete(ids);
		
		for (SysDealGroups bean : beans) {
			sysdealservice.deteleByGroupId(bean.getId());
			logService.operation("刷卡机组",bean.getName()+"删除", null, null, request);
			logger.info("delete SysDealGroups, ID={}.", bean.getId());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}
	
	/**
	 * 
	 * @Description 选择刷卡机
	 * @author  wangfei
	 * @date  2019年3月15日上午9:49:35
	 * @param id
	 * @param request
	 * @param ra
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("selectdeal.do")
	public String deal(Integer id, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap){
		List<SysDeal> sysdeallist=sysdealservice.getByGroupId(id);
		modelMap.addAttribute("sysdeallist", sysdeallist);
		String sql="select distinct StaName from xfls order by StaName";
		Connection conn=SQLServerDriver.getConnection();
		Statement stmxfl;
		try {
			stmxfl = conn.createStatement();
			ResultSet xfls=stmxfl.executeQuery(sql);
			ResultSetMetaData md=xfls.getMetaData();
			int count=md.getColumnCount();
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			while(xfls.next()){
				Map<String,Object> map=new HashMap<String,Object>();
				for(int i=1;i<=count;i++){
					map.put(md.getColumnName(i), xfls.getObject(i));
				}
				list.add(map);
			}
			modelMap.addAttribute("list", list);
		} catch (SQLException e) {
			e.printStackTrace();
			logService.operation(e.getMessage(),e.getMessage(), null, null, request);
			logger.info("deal, message={}.", e.getMessage());
		}
		
		return "core/sysdealgroups/deal_mapping";
	}
	
	/**
	 * 
	 * @Description 保存刷卡机
	 * @author  wangfei
	 * @date  2019年3月15日上午10:58:50
	 * @param dealgroupId
	 * @param dealnames
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("savedeal.do")
	public String	savedeal(Integer dealgroupId,String [] dealnames ,HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap){
		Response resp = new Response(request, response, modelMap);
		SysDealGroups sysdealgroups=service.get(dealgroupId);
		if(dealnames!=null){
			sysdealservice.deteleByGroupId(dealgroupId);
		}
		
		for(String dealname:dealnames){
			if(dealname.length()<=0)
				continue;
			SysDeal sysdeal=new SysDeal();
			sysdeal.setSysdealgroups(sysdealgroups);
			sysdeal.setName(dealname);
			sysdeal.setCreated(new Date());
			sysdealservice.save(sysdeal);
		}
		return resp.post(1);
	}	
	
	@ModelAttribute("bean")
	public SysDealGroups preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}
	@Autowired
	private SysDealGroupsService service;
	
	@Autowired
	private OperationLogService  logService;
	
	@Autowired
	private SysDealService sysdealservice;
}
