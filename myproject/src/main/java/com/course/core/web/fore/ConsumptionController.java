package com.course.core.web.fore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.util.SQLServerDriver;
import com.course.core.domain.User;
import com.course.core.service.UserService;
import com.course.core.support.Response;

/**
 * E应用_消费记录
 * 创建人：chen-chen
 * 创建时间：2019年1月23日下午2:38:20
 * 描述：
 * 
 * 
 * 版本号：1.0
 */
@Controller
public class ConsumptionController {
	private static Connection conn;

	/**
	 * 获取消费记录
	 * @param authCode
	 * @param pageIndex
	 * @param request
	 * @param ra
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = { "/getconsumption" }, method = RequestMethod.POST)
	public String getConsumption(String authCode, Integer pageIndex, String date, String mobile,
			HttpServletRequest request, RedirectAttributes ra, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws SQLException {
		Response resp = new Response(request, response, modelMap);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");

		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);

		if (date != null) {
			year = date.split("-")[0];
			month = (date.split("-")[1]);
		}

		resp.addData("currentDate", date == null ? sdf.format(new Date()) : year + "年" + month + "月");

		if (mobile == null) {
			return resp.post(0);
		}

		String percode;

		User user = userService.getUserByMobile(mobile);
		if (user == null) {
			percode = "";
		} else {
			percode = user.getCarCode();
			if (percode == null) {
				return resp.post(0, "一卡通编号为空");
			}
		}

		pageIndex = pageIndex == null ? 1 : pageIndex;
		if (pageIndex < 1) {
			pageIndex = 1;
		}
		Integer pagesize = 20;

		String sql = "select * from ( select ROW_NUMBER() over(order by DealTime desc) as rownum,"
				+ " convert(varchar,datepart(yy,DealTime))+'年'+convert(varchar,datepart(mm,DealTime))+'月'+convert(varchar,datepart(dd,DealTime))+'日 '+convert(varchar(5), DealTime, 24) as deal_time ,"
				+ " convert(varchar,datepart(yy,RecTime))+'年'+convert(varchar,datepart(mm,RecTime))+'月'+convert(varchar,datepart(dd,RecTime))+'日 '+convert(varchar(5), RecTime, 24) as rec_time , * "
				+ " from xfls where xfls.PerCode='" + percode + "' and DATEPART(YYYY,DealTime)=" + year
				+ " and DATEPART(m,DealTime)=" + month + " ) as a where rownum between "
				+ ((pageIndex - 1) * pagesize + 1) + " and " + (pageIndex) * pagesize;
		conn = SQLServerDriver.getConnection();
		Statement stmtxfl = conn.createStatement();
		ResultSet xfls = stmtxfl.executeQuery(sql);
		ResultSetMetaData md = xfls.getMetaData();// 获取键名
		int columnCount = md.getColumnCount();// 获取行的数量
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (xfls.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				map.put(md.getColumnName(i), xfls.getObject(i));// 获取键名及值
			}
			list.add(map);
		}
		resp.addData("list", list);

		String sql2 = "select top 1 MonCard,DepName from xfls where PerCode='" + percode + "' order by RecTime desc";
		ResultSet xfls2 = stmtxfl.executeQuery(sql2);
		while (xfls2.next()) {
			resp.addData("monCard", xfls2.getObject(1));
			resp.addData("depName", xfls2.getObject(2));
		}

		String sql3 = "select SUM(MonDeal) as mondeal from xfls where PerCode='" + percode
				+ "' and MonDeal<0 and DATEPART(YYYY,DealTime)=" + year + " and DATEPART(m,DealTime)=" + month + "";
		ResultSet xfls3 = stmtxfl.executeQuery(sql3);
		while (xfls3.next()) {
			resp.addData("consumption", xfls3.getObject(1));
		}

		String sql4 = "select SUM(MonDeal) as mondeal from xfls where PerCode='" + percode
				+ "' and MonDeal>0 and DATEPART(YYYY,DealTime)=" + year + " and DATEPART(m,DealTime)=" + month + "";
		ResultSet xfls4 = stmtxfl.executeQuery(sql4);
		while (xfls4.next()) {
			resp.addData("income", xfls4.getObject(1));
		}

		if (list.size() == 0) {
			return resp.post(0);
		}

		return resp.post(1);
	}

	/**
	 * 获取统计数据
	 * @param date
	 * @param pickerDate
	 * @param request
	 * @param ra
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = { "/gettongji" }, method = RequestMethod.POST)
	public String getTongji(String date, String pickerDate, String mobile, HttpServletRequest request,
			RedirectAttributes ra, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws SQLException {
		Response resp = new Response(request, response, modelMap);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");

		if (null != pickerDate) {
			try {
				date = sdf.format(sdf1.parse(pickerDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String year = "";
		String month = "";
		String date2 = "";
		String date3 = "";

		try {
			Date ctuDate = sdf.parse(date);
			// 使用默认时区和区域设置获取日历
			Calendar cal = Calendar.getInstance();
			// 使用给定的Date设置此日历的时间
			cal.setTime(ctuDate);
			year = String.valueOf(cal.get(Calendar.YEAR));
			month = String.valueOf(cal.get(Calendar.MONTH) + 1);

			date3 = sdf1.format(cal.getTime());

			cal.add(Calendar.MONTH, 1);
			date2 = sdf1.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resp.addData("currentDate", date == null ? sdf.format(new Date()) : year + "年" + month + "月");

		if (mobile == null) {
			return resp.post(0);
		}

		String percode;

		User user = userService.getUserByMobile(mobile);
		if (user == null) {
			percode = "";
		} else {
			percode = user.getCarCode();
			if (percode == null) {
				return resp.post(0, "一卡通编号为空");
			}
		}

		String sql3 = "select SUM(MonDeal) as mondeal from xfls where PerCode='" + percode
				+ "' and MonDeal<0 and DATEPART(YYYY,DealTime)=" + year + " and DATEPART(m,DealTime)=" + month + "";
		conn = SQLServerDriver.getConnection();
		Statement stmtxfl = conn.createStatement();
		ResultSet xfls3 = stmtxfl.executeQuery(sql3);
		while (xfls3.next()) {
			resp.addData("consumption", xfls3.getObject(1));
		}

		String sql4 = "select SUM(MonDeal) as mondeal from xfls where PerCode='" + percode
				+ "' and MonDeal>0 and DATEPART(YYYY,DealTime)=" + year + " and DATEPART(m,DealTime)=" + month + "";
		ResultSet xfls4 = stmtxfl.executeQuery(sql4);
		while (xfls4.next()) {
			resp.addData("income", xfls4.getObject(1));
		}

		String sql5 = "Select sum( case when MonDeal<0 then 1 else 0 end ) as monDeal1 "
				+ ",sum( case when MonDeal>=0 then 1 else 0 end ) as monDeal2 " + " from xfls where PerCode='" + percode
				+ "' and DATEPART(YYYY,DealTime)=" + year + " and DATEPART(m,DealTime)=" + month + "";
		ResultSet xfls5 = stmtxfl.executeQuery(sql5);
		while (xfls5.next()) {
			resp.addData("consumptioncount", xfls5.getObject(1));
			resp.addData("incomecount", xfls5.getObject(2));
		}

		String sql6 = "Select top 6 CONVERT(varchar(7), dealtime, 23) year, ABS(sum(MonDeal)) sales " + " from xfls "
				+ "  where PerCode='" + percode + "' and dealtime<'" + date2 + "-01' and MonDeal<0 "
				+ "group by CONVERT(varchar(7), dealtime, 23) " + "order by CONVERT(varchar(7), dealtime, 23) desc";
		ResultSet xfls6 = stmtxfl.executeQuery(sql6);
		ResultSetMetaData md = xfls6.getMetaData();// 获取键名
		int columnCount = md.getColumnCount();// 获取行的数量
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		while (xfls6.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				map.put(md.getColumnName(i), xfls6.getObject(i));// 获取键名及值
			}
			list1.add(map);
		}
		resp.addData("list1", list1);

		String sql7 = "Select top 6 CONVERT(varchar(7), dealtime, 23) year, ABS(sum(MonDeal)) sales " + " from xfls "
				+ "  where PerCode='" + percode + "' and dealtime<'" + date2 + "-01' and MonDeal>=0 "
				+ "group by CONVERT(varchar(7), dealtime, 23) " + "order by CONVERT(varchar(7), dealtime, 23) desc";
		ResultSet xfls7 = stmtxfl.executeQuery(sql7);
		ResultSetMetaData md2 = xfls7.getMetaData();// 获取键名
		int columnCount2 = md2.getColumnCount();// 获取行的数量
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		while (xfls7.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount2; i++) {
				map.put(md2.getColumnName(i), xfls7.getObject(i));// 获取键名及值
			}
			list2.add(map);
		}
		resp.addData("list2", list2);

		resp.addData("currentDate", date == null ? sdf.format(new Date()) : year + "年" + month + "月");

		resp.addData("dateChart", date3);

		return resp.post(1);
	}

	@Autowired
	private UserService userService;
}
