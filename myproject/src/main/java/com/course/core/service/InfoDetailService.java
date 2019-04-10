package com.course.core.service;

import com.course.core.domain.Info;
import com.course.core.domain.InfoDetail;

/**
 * InfoDetailService
 * 
 * @author benfang
 * 
 */
public interface InfoDetailService {
	public InfoDetail get(Integer id);

	public InfoDetail save(InfoDetail detail, Info info);

	public InfoDetail update(InfoDetail bean, Info info);
}
