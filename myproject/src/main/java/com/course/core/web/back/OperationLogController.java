package com.course.core.web.back;

import static com.course.core.constant.Constants.DELETE_SUCCESS;
import static com.course.core.constant.Constants.MESSAGE;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.course.common.util.JsonMapper;
import com.course.common.web.Servlets;
import com.course.core.constant.Constants;
import com.course.core.domain.OperationLog;
import com.course.core.service.OperationLogService;
import com.course.core.support.Context;

/**
 * 操作日志
 * 
 * @author Fang YinLang
 * 
 */
@Controller
@RequestMapping("/core/operation_log")
public class OperationLogController {
	private static final Logger logger = LoggerFactory.getLogger(OperationLogController.class);

	@RequiresPermissions("core:operation_log:list")
	@RequestMapping("list.do")
	public String list(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		params.put("EQ_type", new String[] { "1" });
		Page<OperationLog> pagedList = service.findAll(siteId, params, pageable);
		modelMap.addAttribute("pagedList", pagedList);

		return "core/operation_log/operation_log_list";
	}

	@RequiresPermissions("core:operation_log:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		List<OperationLog> beans = service.delete(ids);
		for (OperationLog bean : beans) {
			logger.info("delete OperationLog, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);

		Integer siteId = Context.getCurrentSiteId();
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		String ip = Servlets.getRemoteAddr(request);
		Integer userId = Context.getCurrentUserId();
		String userAgent = request.getHeader("user-agent");
		service.operation("operation_log.delete", (new JsonMapper()).toJson(params), null, null, ip, userId, siteId,
				userAgent);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public OperationLog preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	@Autowired
	private OperationLogService service;
}
