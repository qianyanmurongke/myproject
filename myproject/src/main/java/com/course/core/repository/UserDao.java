package com.course.core.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.course.common.orm.Limitable;
import com.course.core.domain.User;
import com.course.core.repository.plus.UserDaoPlus;

/**
 * UserDao
 * 
 * @author benfang
 * 
 * 2017年12月01日下午14:53 修改 chenchen 增加  getUserByrealName();
 */
public interface UserDao extends Repository<User, Integer>, UserDaoPlus {
	public Page<User> findAll(Specification<User> spec, Pageable pageable);

	public List<User> findAll(Specification<User> spec, Limitable limitable);

	public User findOne(Integer id);

	public User save(User bean);

	public void delete(User bean);

	// ------------------------------------

	/**
	 * 查询注册用户数量
	 * 
	 * @param siteId
	 *            站点ID
	 * @param beginDate
	 *            开始日期
	 * @return
	 */
	@Query("select count(*) from UserDetail bean where bean.creationDate >= ?1")
	public long countByDate(Date beginDate);

	public User findByUsername(String username);

	public User findByMobile(String mobile);

	public User findByWeixinOpenid(String weixinOpenid);

	public User findByQqOpenid(String qqOpenid);

	public User findByWeiboUid(String weiboUid);
	
	public User findByDingUserId(String dingUserId);

	public User findByValidationTypeAndValidationKey(String type, String key);

	@Query("select count(*) from User bean where bean.username=?1")
	public long countByUsername(String username);

	@Query("select count(*) from User bean where bean.org.id in ?1")
	public long countByOrgId(Collection<Integer> orgIds);

	@Query("select count(*) from User bean where bean.group.id in ?1")
	public long countByGroupId(Collection<Integer> groupIds);

	/**
	 * 根据用户名
	 * @param userName
	 * @return
	 * 2018年6月5日晚20:11 modify by chenchen
	 */
	@Query("from User bean where bean.username=?1")
	public User getUserByUserName(String userName);

	/**
	 * 根据用户名、姓名
	 * @param userName
	 * @param realName
	 * @return
	 * 2018年6月6日上午9:34 add by chenchen
	 */
	@Query("from User bean where bean.username=?1 and bean.realName=?2")
	public User getUserByUserNameAndRealName(String userName, String realName);

	/**
	 * get User By realName
	 * @param realName 姓名
	 * @return
	 */
	@Query("from User bean where bean.realName=?1")
	public User getUserByrealName(String realName);

	/**
	 * 通过学校id获取申请人（用于RepairManageController里）
	 * @param orgId
	 * @return
	 */
	@Query(" from User bean where bean.org.id=?1 and bean.type=0")
	public List<User> getUserListByOrgId(Integer orgId);
	
	/**
	 * getUser
	 * @param mobile
	 * @return
	 */
	@Query("from User bean where bean.mobile=?1")
	public User getUserByMobile(String mobile);
	
	
	
	
}
