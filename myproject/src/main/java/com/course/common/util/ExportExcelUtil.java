package com.course.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

import com.course.common.util.ExcelHead.FieldRow;
import com.course.common.util.ExcelHead.HeadRow;
import com.course.common.util.ExcelHead.HeadRowList;

/**
 * 将数据导出到excel的公共方法
 * 
 * @author Ben Fang
 * 
 */
public class ExportExcelUtil {

	public static void ExportExcel(LinkedHashMap<String, String> fields, List<?> list, String sheetName,
			HttpServletResponse response) {
		@SuppressWarnings("resource")
		HSSFWorkbook excel = new HSSFWorkbook();
		HSSFSheet sheet = excel.createSheet(sheetName);
		sheet.autoSizeColumn(1); 
		HSSFRow firstRow = sheet.createRow(0);
		int cellindex = 0;

		HSSFCellStyle cellStyle = excel.createCellStyle();
		cellStyle.setWrapText(true);
		Font headerFont = excel.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cellStyle.setFont(headerFont);
		for (String map : fields.values()) {
			HSSFCell cell = firstRow.createCell(cellindex);
			cell.setCellValue(map);
			cell.setCellStyle(cellStyle);
			cellindex++;
		}
		for (int j = 0; j < list.size(); j++) {
			HSSFRow hssfrow = sheet.createRow(j + 1);
			Object object = list.get(j);

			cellindex = 0;
			for (Map.Entry<String, String> map : fields.entrySet()) {
				HSSFCell cellss = hssfrow.createCell(cellindex);
				cellindex++;
				String paramKey = map.getKey().toString();
				String[] paramKeys = paramKey.split("\\.");
				Object paramValue = null;
				for (int i = 0; i < paramKeys.length; i++) {
					if (paramKeys[i].length() <= 0)
						continue;

					if (i != 0 && i < paramKeys.length) {
						if (paramValue == null)
							break;
						paramValue = Reflections.getPerperty(paramValue, paramValue.getClass(), paramKeys[i]);
					} else {
						paramValue = Reflections.getPerperty(object, paramKeys[i]);
					}

				}

				if (paramValue == null) {
					cellss.setCellValue("");

				} else {
					if (paramValue instanceof String) {
						cellss.setCellValue((String) paramValue.toString());
					} else if (paramValue instanceof Double) {
						cellss.setCellValue((Double) paramValue);
					} else {
						cellss.setCellValue((String) paramValue.toString());
					}
				}
			}
		}
		String fileName = sheetName;
		try {
			fileName = new String(sheetName.getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ".xls");

		ServletOutputStream out;

		try {
			out = response.getOutputStream();

			excel.write(out);
			out.close();
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 平局分对比报表导出格式
	 * 
	 * @param headrowlists
	 * @param fileds
	 * @param list
	 * @param sheetName
	 * @param response
	 */
	public static void ExportExcel(List<HeadRowList> headrowlists, List<FieldRow> fields, List<?> list,
			String sheetName, HttpServletResponse response) {
		HSSFWorkbook excel = new HSSFWorkbook();
		HSSFSheet sheet = excel.createSheet(sheetName);
		int rowIndex = 0;
		for (HeadRowList headrowlist : headrowlists) {
			HSSFRow firstRow = sheet.createRow(rowIndex);
			rowIndex++;
			int cellindex = 0;
			for (HeadRow headrow : headrowlist.getHeadrows()) {
				HSSFCell cell = firstRow.createCell(cellindex);
				cell.setCellValue(headrow.getDisplayName());
				if (headrow.getColspan() != null) {
					sheet.addMergedRegion(
							new CellRangeAddress(0, 0, cellindex, (cellindex + headrow.getColspan() - 1)));
					cellindex += headrow.getColspan();
				} else {
					cellindex++;
				}
			}
		}
		HSSFCellStyle cellStyle = excel.createCellStyle();
		cellStyle.setWrapText(true);

		for (int j = 0; j < list.size(); j++) {
			HSSFRow hssfrow = sheet.createRow(j + rowIndex);
			Object object = list.get(j);
			@SuppressWarnings("rawtypes")
			Map row = (Map) object;

			int cellindex = 0;
			for (FieldRow filed : fields) {
				HSSFCell cellss = hssfrow.createCell(cellindex);
				cellStyle = excel.createCellStyle();
				cellStyle.setWrapText(true);

				if (filed.getColorVal() != null) {

					cellStyle.setWrapText(true);

					Font font = cellStyle.getFont(excel);

					font.setColor(filed.getColorVal());

					cellStyle.setFont(font);
				}

				cellss.setCellStyle(cellStyle);

				if (row.get(filed.getFieldName()) != null) {
					if (row.get(filed.getFieldName()) instanceof String) {
						cellss.setCellValue((String) row.get(filed.getFieldName()).toString());
					} else if (row.get(filed) instanceof Double) {
						cellss.setCellValue((Double) row.get(filed.getFieldName()));
					} else {
						cellss.setCellValue((String) row.get(filed.getFieldName()).toString());
					}
				}

				cellindex++;
			}
		}

		String fileName = sheetName;
		try {
			fileName = new String(sheetName.getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ".xls");
		ServletOutputStream out;

		try {
			out = response.getOutputStream();
			excel.write(out);
			out.close();
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void toExcel(LinkedHashMap<String, String> fileds, List<?> list, String sheetName,
			HttpServletResponse response) {
		@SuppressWarnings("resource")
		HSSFWorkbook excel = new HSSFWorkbook();
		HSSFSheet sheet = excel.createSheet(sheetName);
		HSSFRow firstRow = sheet.createRow(0);
		int cellindex = 0;
		for (String map : fileds.values()) {
			HSSFCell cell = firstRow.createCell(cellindex);
			cell.setCellValue(map);
			cellindex++;
		}
		HSSFCellStyle cellStyle = excel.createCellStyle();
		cellStyle.setWrapText(true);
		for (int j = 0; j < list.size(); j++) {
			HSSFRow hssfrow = sheet.createRow(j + 1);
			Object object = list.get(j);
			@SuppressWarnings("rawtypes")
			Map row = (Map) object;

			cellindex = 0;
			for (Map.Entry<String, String> map : fileds.entrySet()) {
				HSSFCell cellss = hssfrow.createCell(cellindex);
				cellss.setCellStyle(cellStyle);
				if (row.get(map.getKey()) != null) {
					if (row.get(map.getKey()) instanceof String) {
						cellss.setCellValue((String) row.get(map.getKey()).toString());
					} else if (row.get(map.getKey()) instanceof Double) {
						cellss.setCellValue((Double) row.get(map.getKey()));
					} else {
						cellss.setCellValue((String) row.get(map.getKey()).toString());
					}
				}
				cellindex++;
			}
			// HSSFCell cell = row.createCell(0);
			// cell.setCellValue((String) obj[0].toString());
			// cell = row.createCell(1);
			// cell.setCellValue((String) obj[1].toString());
			// cell = row.createCell(2);
			// cell.setCellValue((String) obj[2].toString());
			// cell = row.createCell(3);
			// cell.setCellValue((String) obj[3].toString());
			// cell = row.createCell(4);
			// cell.setCellValue((Double) obj[4]);
			// cell = row.createCell(5);
			// cell.setCellValue((Double) obj[5]);
			// for(int k = 0;k<titles.length;k++){
			// HSSFCell cellss = row.createCell(k);
			// if(obj[k] instanceof String){
			// cellss.setCellValue((String) obj[k].toString());
			// }else if(obj[k] instanceof Double){
			// cellss.setCellValue((Double) obj[k]);
			// }else{
			// cellss.setCellValue((String) obj[k].toString());
			// }
			// }
		}

		String fileName = sheetName;
		try {
			fileName = new String(sheetName.getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ".xls");
		ServletOutputStream out;
		try {
			out = response.getOutputStream();
			excel.write(out);
			out.close();
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
