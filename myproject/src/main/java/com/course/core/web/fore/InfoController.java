package com.course.core.web.fore;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.course.common.file.FileHandler;
import com.course.common.file.LocalFileHandler;
import com.course.common.util.JsonMapper;
import com.course.common.web.PathResolver;
import com.course.common.web.Servlets;
import com.course.core.constant.Constants;
import com.course.core.domain.Info;
import com.course.core.domain.MemberGroup;
import com.course.core.domain.Node;
import com.course.core.domain.Org;
import com.course.core.domain.PublishPoint;
import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.service.InfoBufferService;
import com.course.core.service.InfoQueryService;
import com.course.core.support.Context;
import com.course.core.support.ForeContext;
import com.course.core.support.Response;
import com.course.core.support.SiteResolver;
import com.course.core.support.TitleText;

/**
 * InfoController
 * 
 * @author benfang
 * 
 */
@Controller
public class InfoController {
	@RequestMapping("/info/{id:[0-9]+}")
	public String info(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return info(null, id, 1, request, response, modelMap);
	}

	@RequestMapping("/info/{id:[0-9]+}_{page:[0-9]+}")
	public String info(@PathVariable Integer id, @PathVariable Integer page, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return info(null, id, page, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info/{id:[0-9]+}")
	public String info(@PathVariable String siteNumber, @PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return info(siteNumber, id, 1, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info/{id:[0-9]+}_{page:[0-9]+}")
	public String info(@PathVariable String siteNumber, @PathVariable Integer id, @PathVariable Integer page,
			HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Info info = query.get(id);
		siteResolver.resolveSite(siteNumber, info);
		Response resp = new Response(request, response, modelMap);
		if (info == null) {
			return resp.badRequest("Info not found: " + id);
		}
		if (!info.isNormal()) {
			return resp.forbidden();
		}
		Collection<MemberGroup> groups = Context.getCurrentGroups(request);
		Collection<Org> orgs = Context.getCurrentOrgs(request);
		if (!info.isViewPerm(groups, orgs)) {
			User user = Context.getCurrentUser();
			if (user != null) {
				return resp.forbidden();
			} else {
				return resp.unauthorized();
			}
		}
		if (info.isLinked()) {
			return "redirect:" + info.getLinkUrl();
		}
		Node node = info.getNode();
		List<TitleText> textList = info.getTextList();
		TitleText infoText = TitleText.getTitleText(textList, page);
		String title = infoText.getTitle();
		String text = infoText.getText();
		modelMap.addAttribute("info", info);
		modelMap.addAttribute("node", node);
		modelMap.addAttribute("title", title);
		modelMap.addAttribute("text", text);

		Page<String> pagedList = new PageImpl<String>(Arrays.asList(text), new PageRequest(page - 1, 1),
				textList.size());
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page, info, pagedList);

		String template = Servlets.getParam(request, "template");
		if (StringUtils.isNotBlank(template)) {
			return template;
		} else {
			return info.getTemplate();
		}
	}

	@RequestMapping("/info_download")
	public String download(Integer id, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		return download(null, id, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_download")
	public String download(@PathVariable String siteNumber, Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) throws IOException {
		Info info = query.get(id);
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		infoBufferService.updateDownloads(id);
		String path = info.getFile();
		if (StringUtils.isBlank(path)) {
			return resp.notFound();
		}
		String normalPath = FilenameUtils.normalize(path, true);
		PublishPoint point = info.getSite().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String urlPrefix = point.getUrlPrefix();
		if (StringUtils.startsWith(normalPath, urlPrefix) && fileHandler instanceof LocalFileHandler) {
			LocalFileHandler lfHandler = (LocalFileHandler) fileHandler;
			normalPath = normalPath.substring(urlPrefix.length());
			File file = lfHandler.getFile(normalPath);
			if (!file.exists()) {
				return null;
			}
			Servlets.setDownloadHeader(response, file.getName());
			response.setContentLength((int) file.length());
			OutputStream output = response.getOutputStream();
			FileUtils.copyFile(file, output);
			output.flush();
			return null;
		} else {
			response.sendRedirect(path);
			return null;
		}
	}

	@RequestMapping(value = "/info_views")
	public void views(Integer id, HttpServletRequest request, HttpServletResponse response) {
		views(null, id, request, response);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_views")
	public void views(@PathVariable String siteNumber, Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		siteResolver.resolveSite(siteNumber);
		if (id == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		if (query.get(id) == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		String result = Integer.toString(bufferService.updateViews(id));
		Servlets.writeHtml(response, result);
	}

	@RequestMapping(value = "/info_views/{id:[0-9]+}")
	public void views(@PathVariable Integer id, @RequestParam(defaultValue = "true") boolean isUpdate,
			HttpServletRequest request, HttpServletResponse response) {
		views(null, id, isUpdate, request, response);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_views/{id:[0-9]+}")
	public void views(@PathVariable String siteNumber, @PathVariable Integer id,
			@RequestParam(defaultValue = "true") boolean isUpdate, HttpServletRequest request,
			HttpServletResponse response) {
		siteResolver.resolveSite(siteNumber);
		Info info = query.get(id);
		if (info == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		Integer views;
		if (isUpdate) {
			views = bufferService.updateViews(id);
		} else {
			views = info.getBufferViews();
		}
		String result = Integer.toString(views);
		Servlets.writeHtml(response, result);
	}

	@RequestMapping("/info_comments/{id:[0-9]+}")
	public void comments(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		comments(null, id, request, response);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_comments/{id:[0-9]+}")
	public void comments(@PathVariable String siteNumber, @PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		siteResolver.resolveSite(siteNumber);
		Info info = query.get(id);
		int comments;
		if (info != null) {
			comments = info.getBufferComments();
		} else {
			comments = 0;
		}
		String result = Integer.toString(comments);
		Servlets.writeHtml(response, result);
	}

	@RequestMapping("/info_downloads/{id:[0-9]+}")
	public void downloads(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		downloads(null, id, request, response);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_downloads/{id:[0-9]+}")
	public void downloads(@PathVariable String siteNumber, @PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		siteResolver.resolveSite(siteNumber);
		Info info = query.get(id);
		int downloads;
		if (info != null) {
			downloads = info.getBufferDownloads();
		} else {
			downloads = 0;
		}
		String result = Integer.toString(downloads);
		Servlets.writeHtml(response, result);
	}

	@RequestMapping("/info_digg")
	public void digg(Integer id, HttpServletRequest request, HttpServletResponse response) {
		digg(null, id, request, response);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_digg")
	public void digg(@PathVariable String siteNumber, Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		siteResolver.resolveSite(siteNumber);
		if (id == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		Info info = query.get(id);
		if (info == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		Integer userId = Context.getCurrentUserId();
		String ip = Servlets.getRemoteAddr(request);
		String cookie = Site.getIdentityCookie(request, response);

		String result = Integer.toString(bufferService.updateDiggs(id, userId, ip, cookie));
		Servlets.writeHtml(response, result);
	}

	@RequestMapping("/info_bury")
	public void bury(Integer id, HttpServletRequest request, HttpServletResponse response) {
		bury(null, id, request, response);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_bury")
	public void bury(@PathVariable String siteNumber, Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		siteResolver.resolveSite(siteNumber);
		if (id == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		Info info = query.get(id);
		if (info == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		Integer userId = Context.getCurrentUserId();
		String ip = Servlets.getRemoteAddr(request);
		String cookie = Site.getIdentityCookie(request, response);

		String result = Integer.toString(bufferService.updateBurys(id, userId, ip, cookie));
		Servlets.writeHtml(response, result);
	}

	@RequestMapping("/info_diggs/{id:[0-9]+}")
	public void diggs(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		diggs(null, id, request, response);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_diggs/{id:[0-9]+}")
	public void diggs(@PathVariable String siteNumber, @PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		siteResolver.resolveSite(siteNumber);
		Info info = query.get(id);
		int diggs;
		int burys;
		if (info != null) {
			diggs = info.getBufferDiggs();
			burys = info.getBufferBurys();
		} else {
			diggs = 0;
			burys = 0;
		}
		String result = "[" + diggs + "," + burys + "]";
		Servlets.writeHtml(response, result);
	}

	@RequestMapping("/info_scoring")
	public void scoring(Integer id, Integer itemId, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		scoring(null, id, itemId, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_scoring")
	public void scoring(@PathVariable String siteNumber, Integer id, Integer itemId, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		if (id == null || itemId == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		Info info = query.get(id);
		if (info == null ) {
			Servlets.writeHtml(response, "0");
			return;
		}
		Integer userId = Context.getCurrentUserId();
		String ip = Servlets.getRemoteAddr(request);
		String cookie = Site.getIdentityCookie(request, response);
		int score = 0;
		String result = String.valueOf(score);
		Servlets.writeHtml(response, result);
	}

	@RequestMapping("/info_score/{id:[0-9]+}")
	public void score(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		score(null, id, request, response);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/info_score/{id:[0-9]+}")
	public void score(@PathVariable String siteNumber, @PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		siteResolver.resolveSite(siteNumber);
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		JsonMapper mapper = new JsonMapper();
		String result = mapper.toJson(map);
		Servlets.writeHtml(response, result);
	}

	@RequestMapping(path = { "/info_favorites/{id:[0-9]+}",
			Constants.SITE_PREFIX_PATH + "/info_favorites/{id:[0-9]+}" })
	public void favorites(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		Info info = query.get(id);
		if (info == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		String result = Integer.toString(info.getFavorites());
		Servlets.writeHtml(response, result);
	}

	@RequestMapping(path = { "/info_favorite", Constants.SITE_PREFIX_PATH + "/info_favorite" })
	public void favorite(Integer id, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		if (user == null) {
			Servlets.writeHtml(response, "-1");
			return;
		}
		Info info = query.get(id);
		if (info == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		String result = Integer.toString(info.getFavorites());
		Servlets.writeHtml(response, result);
	}

	@RequestMapping(path = { "/info_unfavorite", Constants.SITE_PREFIX_PATH + "/info_unfavorite" })
	public void unfavorite(Integer id, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		if (user == null) {
			Servlets.writeHtml(response, "-1");
			return;
		}
		Info info = query.get(id);
		if (info == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		String result = Integer.toString(info.getFavorites());
		Servlets.writeHtml(response, result);
	}

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private InfoBufferService bufferService;
	@Autowired
	private InfoQueryService query;
	@Autowired
	private InfoBufferService infoBufferService;
	@Autowired
	private PathResolver pathResolver;
}
