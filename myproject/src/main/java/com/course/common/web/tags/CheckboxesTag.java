package com.course.common.web.tags;

/**
 * CheckboxesTag
 * 
 * @author benfang
 * 
 */
public class CheckboxesTag extends AbstractMultiCheckedElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getInputType() {
		return "checkbox";
	}
}
