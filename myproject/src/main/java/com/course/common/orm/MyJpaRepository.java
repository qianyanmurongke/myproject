package com.course.common.orm;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository 接口
 * 
 * @author benfang
 * 
 * @param <T>
 * @param <ID>
 */
public interface MyJpaRepository<T, ID extends Serializable> extends
		JpaRepository<T, ID> {
	List<T> findAll(Limitable limitable);
}
