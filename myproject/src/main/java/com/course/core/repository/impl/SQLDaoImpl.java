package com.course.core.repository.impl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.course.common.orm.JpqlBuilder;
import com.course.common.orm.JpqlBuilder.Parameter;
import com.course.core.repository.SQLDao;

/**
 * SQLDaoImpl
 * 
 * @author benfang
 * 
 */
@Repository
public class SQLDaoImpl implements SQLDao {
	public List<?> query(String sql, Integer maxRows, Integer startRow) {
		Query query = em.createNativeQuery(sql);
		if (maxRows != null) {
			query.setMaxResults(maxRows);
		}
		if (startRow != null) {
			query.setFirstResult(startRow);
		}
		return query.getResultList();
	}

	public List<?> queryByMap(String sql, Integer maxRows, Integer startRow) {
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (maxRows != null) {
			query.setMaxResults(maxRows);
		}
		if (startRow != null) {
			query.setFirstResult(startRow);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Page<T> pageQuery(String sql, final Pageable pageable) {
		final int pageSize = pageable.getPageSize();
		int start = (pageable.getPageNumber()) * pageSize;
		final Query query = em.createNativeQuery(sql);
		final int total = query.getResultList().size();
		double paramIndex = (double) total / (double) pageSize;
		final int pageindex = (int) Math.ceil(paramIndex);
		if (start < total && pageSize > 0) {
			query.setFirstResult(start);
			query.setMaxResults(pageSize);
		}
		final List<T> paramQuery = query.getResultList();
		Page<T> pageResult = new Page<T>() {

			@Override
			public int getNumber() {
				return pageable.getPageNumber();
			}

			@Override
			public int getSize() {
				return pageSize;
			}

			@Override
			public int getNumberOfElements() {
				return 0;
			}

			@Override
			public List<T> getContent() {
				return paramQuery;
			}

			@Override
			public boolean hasContent() {
				return getNumber() > 0;
			}

			@Override
			public Sort getSort() {
				return pageable.getSort();
			}

			@Override
			public boolean isFirst() {
				return pageable.getPageNumber() <= 0;
			}

			@Override
			public boolean isLast() {
				return pageable.getPageNumber() == (pageindex - 1);
			}

			@Override
			public boolean hasNext() {
				return pageable.getPageNumber() < (pageindex - 1);
			}

			@Override
			public boolean hasPrevious() {
				// TODO 自动生成的方法存根
				return false;
			}

			@Override
			public Pageable nextPageable() {
				// TODO 自动生成的方法存根
				return null;
			}

			@Override
			public Pageable previousPageable() {
				// TODO 自动生成的方法存根
				return null;
			}

			@Override
			public Iterator<T> iterator() {
				// TODO 自动生成的方法存根
				return null;
			}

			@Override
			public int getTotalPages() {
				return pageindex;
			}

			@Override
			public long getTotalElements() {
				return total;
			}

			@Override
			public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
				// TODO 自动生成的方法存根
				return null;
			}

		};

		return pageResult;

	}

	public int update(String sql) {
		return em.createNativeQuery(sql).executeUpdate();
	}

	/**
	 * 带查询参数的分页查询
	 * 
	 * @param sql
	 * @param parameters
	 * @param pageable
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Page page(String sql, List<Parameter> parameters, Pageable pageable) {
		JpqlBuilder jpq = new JpqlBuilder("SELECT * FROM (" + sql + ") as beanC");
		jpq.setParameters(parameters);
		return jpq.nativePageByMap(em, pageable);
	}

	@SuppressWarnings("rawtypes")
	public List list(String sql, List<Parameter> parameters, Sort sort) {

		JpqlBuilder jpq = new JpqlBuilder("SELECT * FROM (" + sql + ") as beanC");
		jpq.setParameters(parameters);
		return jpq.listNative(em, sort);
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
