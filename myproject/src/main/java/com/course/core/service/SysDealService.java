package com.course.core.service;


import java.util.List;

import com.course.core.domain.SysDeal;

public interface SysDealService {
	public SysDeal save(SysDeal sysdeal);
	
	public List<SysDeal> getByGroupId(Integer GroupId);
	
	void deteleByGroupId(Integer GroupId);
}
