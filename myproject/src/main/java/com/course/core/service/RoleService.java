package com.course.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.course.common.orm.RowSide;
import com.course.core.domain.Role;

/**
 * RoleService
 * 
 * @author benfang
 * 
 */
public interface RoleService {
	public List<Role> findList(Integer siteId, Map<String, String[]> params,
			Sort sort);

	public RowSide<Role> findSide(Integer siteId, Map<String, String[]> params,
			Role bean, Integer position, Sort sort);

	public List<Role> findList(Integer siteId);

	public Role get(Integer id);

	public Role save(Role bean, Integer[] infoPermIds, Integer[] nodePermIds,
			Integer siteId);

	public Role update(Role bean, Integer[] infoPermIds, Integer[] nodePermIds);

	public List<Role> batchUpdate(Integer[] id, String[] name, Integer[] rank,
			String[] description);

	public Role delete(Integer id);

	public Role[] delete(Integer[] ids);
	
	public Role getByName(String rolename);
}
