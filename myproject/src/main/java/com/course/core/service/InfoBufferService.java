package com.course.core.service;

import com.course.core.domain.Info;
import com.course.core.domain.InfoBuffer;

/**
 * InfoBufferService
 * 
 * @author benfang
 * 
 */
public interface InfoBufferService {
	public InfoBuffer get(Integer id);

	public InfoBuffer save(InfoBuffer bean, Info info);

	public int updateViews(Integer id);

	public int updateDownloads(Integer id);

	public int updateDiggs(Integer id, Integer userId, String ip, String cookie);

	public int updateBurys(Integer id, Integer userId, String ip, String cookie);

}
