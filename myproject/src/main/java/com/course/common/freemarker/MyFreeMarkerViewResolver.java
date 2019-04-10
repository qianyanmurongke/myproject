package com.course.common.freemarker;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

/**
 * FreeMarker视图
 * 
 * @author benfang
 * 
 */
public class MyFreeMarkerViewResolver extends AbstractTemplateViewResolver {
	public MyFreeMarkerViewResolver() {
		setViewClass(requiredViewClass());
	}

	protected Class<MyFreeMarkerView> requiredViewClass() {
		return MyFreeMarkerView.class;
	}
}
