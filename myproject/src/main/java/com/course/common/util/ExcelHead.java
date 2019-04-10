package com.course.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 平均分对比报表-Excel导出
 *  
 * 创 建 人：chenchen
 * 日    期：2017年12月9日上午11:28:25
 * 修 改 人：
 * 日   期：
 * 描   述：
 * 		2017年12月9日上午11:28:25  增加-平均分对比报表 Excel导出-管理类    by chenchen
 * 
 *
 * 版 本 号：1.0
 */
public class ExcelHead {

	public class FieldRow {
		private Short colorVal;

		private String fieldName;

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public Short getColorVal() {
			return colorVal;
		}

		public void setColorVal(Short colorVal) {
			this.colorVal = colorVal;
		}

	}

	public class HeadRowList {
		private List<HeadRow> headrows = new ArrayList<HeadRow>(0);

		public List<HeadRow> getHeadrows() {
			return headrows;
		}

		public void setHeadrows(List<HeadRow> headrows) {
			this.headrows = headrows;
		}

	}

	public class HeadRow {

		private String displayName;

		private Integer colspan;

		private Integer rowspan;

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public Integer getColspan() {
			return colspan;
		}

		public void setColspan(Integer colspan) {
			this.colspan = colspan;
		}

		public Integer getRowspan() {
			return rowspan;
		}

		public void setRowspan(Integer rowspan) {
			this.rowspan = rowspan;
		}

	}
}
