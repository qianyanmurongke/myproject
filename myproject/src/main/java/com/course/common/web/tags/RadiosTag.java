package com.course.common.web.tags;

/**
 * RadiosTag
 * 
 * @author benfang
 * 
 */
public class RadiosTag extends AbstractMultiCheckedElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getInputType() {
		return "radio";
	}
}
