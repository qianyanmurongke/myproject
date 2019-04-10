package com.course.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.course.core.domain.SysDeal;

public interface SysDealDao extends Repository<SysDeal,Integer>{
	
	public SysDeal save(SysDeal bean);
	
	@Query("from SysDeal bean where bean.sysdealgroups.id=?1")
	public List<SysDeal> getByGroupId(Integer GroupId);
	
	@Modifying
	@Query("delete from SysDeal bean where bean.sysdealgroups.id=?1")
	public void deteleByGroupId(Integer GroupId);
}
