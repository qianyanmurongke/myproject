package com.course.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.course.common.orm.RowSide;
import com.course.core.domain.GlobalMail;
import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.domain.UserDetail;

/**
 * UserService
 * 
 * @author benfang
 * 
 * 2017年12月01日下午14:53 修改 chenchen 增加  getUserByrealName();
 */
public interface UserService {
	public Page<User> findPage(Integer rank, Integer[] type, String orgTreeNumber, Map<String, String[]> params,
			Pageable pageable);

	public RowSide<User> findSide(Integer rank, Integer[] type, String orgTreeNumber, Map<String, String[]> params,
			User bean, Integer position, Sort sort);

	public List<User> findByUsername(String[] usernames);

	public User findByUsername(String username);

	public User findByMobile(String mobile);

	public User findByValidation(String type, String key);

	public User findByDingUserId(String dingUserId);

	public long countByDate(Date beginDate);

	public boolean usernameExist(String username);

	public User getAnonymous();

	public Integer getAnonymousId();

	public User get(Integer id);

	public void updatePassword(Integer userId, String rawPassword);

	public void updateEmail(Integer userId, String email);

	public void sendVerifyEmail(Site site, User user, GlobalMail mail, String subject, String text);

	public User verifyMember(User user);

	public void sendPasswordEmail(Site site, User user, GlobalMail mail, String subject, String text);

	public User passwordChange(User user, String rawPassword);

	public User save(User bean, UserDetail detail, Integer[] roleIds, Integer[] orgIds, Integer[] groupIds,
			Map<String, String> customs, Map<String, String> clobs, Integer orgId, Integer groupId, String ip);

	public User register(String ip, int groupId, int orgId, int status, String username, String password, String email,
			String qqOpenid, String weiboUid, String weixinOpenid, String gender, Date birthDate, String bio,
			String comeFrom, String qq, String msn, String weixin);

	public User update(User bean, UserDetail detail, Integer[] roleIds, Integer[] groupIds, Map<String, String> customs,
			Map<String, String> clobs, Integer orgId, Integer groupId, Integer topOrgId, Integer siteId);

	public User update(User user, UserDetail detail);

	public User deletePassword(Integer id);

	public User[] deletePassword(Integer[] ids);

	public User check(Integer id);

	public User[] check(Integer[] ids);

	public User lock(Integer id);

	public User[] lock(Integer[] ids);

	public User unlock(Integer id);

	public User[] unlock(Integer[] ids);

	public User delete(Integer id);

	public User[] delete(Integer[] ids);

	/**
	 * 根据用户名
	 * @param userName
	 * @return
	 * 2018年6月5日晚20:11 modify by chenchen
	 */
	public User getUserByUserName(String userName);

	/**
	* 根据用户名、姓名
	* @param userName
	* @param realName
	* @return
	* 2018年6月6日上午9:34 add by chenchen
	*/
	public User getUserByUserNameAndRealName(String userName, String realName);

	/**
	 * get User By realName
	 * @param realName 姓名
	 * @return
	 */
	public User getUserByrealName(String realName);

	// 查询申请人（用于RepairManageController里）
	public List<User> getUserListByOrgId(Integer orgId);

	/**
	 * 根据用户名以及班级id来查询User表
	 * @param realName
	 * @param classId
	 * @return
	 *  ADD xuewei
	 */
	public User getUserByrealNameAndClassId(String realName, Integer classId);

	/**
	 * getUser
	 * @param mobile
	 * @return
	 */
	public User getUserByMobile(String mobile);

}
