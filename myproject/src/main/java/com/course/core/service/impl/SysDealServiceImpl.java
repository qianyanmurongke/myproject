package com.course.core.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.core.domain.SysDeal;
import com.course.core.repository.SysDealDao;
import com.course.core.service.SysDealService;

@Service
@Transactional(readOnly = true)
public class SysDealServiceImpl implements SysDealService{
	
	@Override
	public List<SysDeal> getByGroupId(Integer GroupId) {
		return dao.getByGroupId(GroupId);
	}
	
	@Transactional
	public SysDeal save(SysDeal sysdeal) {
		sysdeal = dao.save(sysdeal);
		return sysdeal;
	}
	
	@Transactional
	public void deteleByGroupId(Integer GroupId){
		dao.deteleByGroupId(GroupId);
	}
	
	@Autowired
	private SysDealDao dao;


}
