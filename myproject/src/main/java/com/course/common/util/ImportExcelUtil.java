package com.course.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 导入Excel公共方法
 * 
 * @author Ben.Fang
 *
 */
public class ImportExcelUtil {
	public int totalRows; // sheet中总行数
	public static int totalCells; // 每一行总单元格数

	public ImportExcelUtil() {

	}


	public List<Map<String, String>> readExcel(String filePath, String sheetName, Integer heardRow) throws IOException {
		File file = new File(filePath);

		return this.readExcel(file, sheetName, heardRow);
	}
	
	
	public List<Map<String, String>> readExcel(String filePath, String sheetName) throws IOException {
		File file = new File(filePath);

		return this.readExcel(file, sheetName);
	}
	
	/**
	 * read the Excel .xlsx,.xls
	 * 
	 * @param file
	 *            中的上传文件
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, String>> readExcel(File file, String sheetName, Integer heardRow) throws IOException {
		if (file == null || ExcelUtil.EMPTY.equals(file.getName().trim())) {
			return null;
		} else {
			String postfix = ExcelUtil.getPostfix(file.getName());
			if (!ExcelUtil.EMPTY.equals(postfix)) {
				if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(file, sheetName,heardRow);
				} else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsx(file, sheetName,heardRow);
				} else {
					return null;
				}
			}
		}
		return null;
	}


	/**
	 * read the Excel .xlsx,.xls
	 * 
	 * @param file
	 *            中的上传文件
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, String>> readExcel(File file, String sheetName) throws IOException {
		if (file == null || ExcelUtil.EMPTY.equals(file.getName().trim())) {
			return null;
		} else {
			String postfix = ExcelUtil.getPostfix(file.getName());
			if (!ExcelUtil.EMPTY.equals(postfix)) {
				if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(file, sheetName);
				} else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsx(file, sheetName);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * read the Excel .xlsx,.xls
	 * 
	 * @param file
	 *            中的上传文件
	 * @return
	 * @throws IOException
	 */
	public List<ArrayList<String>> readExcel(File file) throws IOException {
		if (file == null || ExcelUtil.EMPTY.equals(file.getName().trim())) {
			return null;
		} else {
			String postfix = ExcelUtil.getPostfix(file.getName());
			if (!ExcelUtil.EMPTY.equals(postfix)) {
				if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(file);
				} else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsx(file);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * read the Excel 2010 .xlsx
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public List<Map<String, String>> readXlsx(File file, String sheetName, Integer heardRow) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// IO流读取文件
		InputStream input = null;
		XSSFWorkbook wb = null;
		Map<String, String> rowList = null;
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new XSSFWorkbook(input);
			// 读取sheet(页)
			XSSFSheet xssfSheet = wb.getSheet(sheetName);
			if (xssfSheet == null) {
				return null;
			}
			totalRows = xssfSheet.getLastRowNum();
			XSSFRow headRow = xssfSheet.getRow(heardRow);
			if (headRow == null) {
				return null;
			}
			int headCells = headRow.getLastCellNum();
			// 读取Row,从第二行开始
			for (int rowNum =  (heardRow+1); rowNum <= totalRows; rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					rowList = new HashMap<String, String>();
					totalCells = xssfRow.getLastCellNum();
					// 读取列，从第一列开始
					for (int c = 0; c <= headCells + 1; c++) {
						String headname =  "head" + c;
//						XSSFCell headCell = headRow.getCell(c);
//						if (headCell == null) {
//							continue;
//						}
//						headname = ExcelUtil.getXValue(headCell).trim();
//						if (headname.isEmpty()) {
//							headname = "head" + c;
//						}
						XSSFCell cell = xssfRow.getCell(c);
						if (cell == null) {
							rowList.put(headname, ExcelUtil.EMPTY);
							continue;
						}
						rowList.put(headname, ExcelUtil.getXValue(cell).trim());
					}
					list.add(rowList);
				}
			}

			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}
	
	/**
	 * read the Excel 2010 .xlsx
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public List<Map<String, String>> readXlsx(File file, String sheetName) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// IO流读取文件
		InputStream input = null;
		XSSFWorkbook wb = null;
		Map<String, String> rowList = null;
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new XSSFWorkbook(input);
			// 读取sheet(页)
			XSSFSheet xssfSheet = wb.getSheet(sheetName);
			if (xssfSheet == null) {
				return null;
			}
			totalRows = xssfSheet.getLastRowNum();
			XSSFRow headRow = xssfSheet.getRow(0);
			if (headRow == null) {
				return null;
			}
			int headCells = headRow.getLastCellNum();
			// 读取Row,从第二行开始
			for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					rowList = new HashMap<String, String>();
					totalCells = xssfRow.getLastCellNum();
					// 读取列，从第一列开始
					for (int c = 0; c <= headCells + 1; c++) {
						String headname = "";
						XSSFCell headCell = headRow.getCell(c);
						if (headCell == null) {
							continue;
						}
						headname = ExcelUtil.getXValue(headCell).trim();
						if (headname.isEmpty()) {
							continue;
						}
						XSSFCell cell = xssfRow.getCell(c);
						if (cell == null) {
							rowList.put(headname, ExcelUtil.EMPTY);
							continue;
						}
						rowList.put(headname, ExcelUtil.getXValue(cell).trim());
					}
				}
			}

			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * read the Excel 2010 .xlsx
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public List<ArrayList<String>> readXlsx(File file) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		// IO流读取文件
		InputStream input = null;
		XSSFWorkbook wb = null;
		ArrayList<String> rowList = null;
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new XSSFWorkbook(input);
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				totalRows = xssfSheet.getLastRowNum();
				// 读取Row,从第二行开始
				for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow != null) {
						rowList = new ArrayList<String>();
						totalCells = xssfRow.getLastCellNum();
						// 读取列，从第一列开始
						for (int c = 0; c <= totalCells + 1; c++) {
							XSSFCell cell = xssfRow.getCell(c);
							if (cell == null) {
								rowList.add(ExcelUtil.EMPTY);
								continue;
							}
							rowList.add(ExcelUtil.getXValue(cell).trim());
						}
					}
				}
			}

			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}
	/**
	 * read the Excel 2003-2007 .xls
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	public List<Map<String, String>> readXls(File file, String sheetName) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// IO流读取文件
		InputStream input = null;
		HSSFWorkbook wb = null;
		Map<String, String> rowList = null;
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new HSSFWorkbook(input);
			// 读取sheet(页)
			HSSFSheet hssfSheet = wb.getSheet(sheetName);
			if (hssfSheet == null) {
				return null;
			}
			totalRows = hssfSheet.getLastRowNum();
			HSSFRow headRow = hssfSheet.getRow(0);
			int headCells = headRow.getLastCellNum();
			// 读取Row,从第二行开始
			for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					rowList = new HashMap<String, String>();
					totalCells = hssfRow.getLastCellNum();
					// 读取列，从第一列开始
					for (short c = 0; c <= headCells + 1; c++) {
						String headname = "";
						HSSFCell headCell = headRow.getCell(c);
						if (headCell == null) {
							continue;
						}
						headCell.setCellType(HSSFCell.CELL_TYPE_STRING);
						headname = ExcelUtil.getHValue(headCell).trim();
						if (headname.isEmpty()) {
							continue;
						}
						HSSFCell cell = hssfRow.getCell(c);
						if (cell == null) {
							rowList.put(headname, ExcelUtil.EMPTY);
							continue;
						}
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						rowList.put(headname, ExcelUtil.getHValue(cell).trim());
					}
					list.add(rowList);
				}
			}

			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * read the Excel 2003-2007 .xls
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	public List<Map<String, String>> readXls(File file, String sheetName, Integer heardRow) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// IO流读取文件
		InputStream input = null;
		HSSFWorkbook wb = null;
		Map<String, String> rowList = null;
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new HSSFWorkbook(input);
			// 读取sheet(页)
			HSSFSheet hssfSheet = wb.getSheet(sheetName);
			if (hssfSheet == null) {
				return null;
			}
			totalRows = hssfSheet.getLastRowNum();
			HSSFRow headRow = hssfSheet.getRow(heardRow);
			int headCells = headRow.getLastCellNum();
			// 读取Row,从第二行开始
			for (int rowNum = (heardRow+1); rowNum <= totalRows; rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					rowList = new HashMap<String, String>();
					totalCells = hssfRow.getLastCellNum();
					// 读取列，从第一列开始
					for (short c = 0; c <= headCells + 1; c++) {
						String headname = "head" + c;
//						HSSFCell headCell = headRow.getCell(c);
//						if (headCell == null) {
//							continue;
//						}
//						headCell.setCellType(HSSFCell.CELL_TYPE_STRING);
//						headname = ExcelUtil.getHValue(headCell).trim();
//						if (headname.isEmpty()) {
//							headname = "head" + c;
//						}
						HSSFCell cell = hssfRow.getCell(c);
						if (cell == null) {
							rowList.put(headname, ExcelUtil.EMPTY);
							continue;
						}
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						rowList.put(headname, ExcelUtil.getHValue(cell).trim());
					}
					list.add(rowList);
				}
			}

			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * read the Excel 2003-2007 .xls
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	public List<ArrayList<String>> readXls(File file) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		// IO流读取文件
		InputStream input = null;
		HSSFWorkbook wb = null;
		ArrayList<String> rowList = null;
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new HSSFWorkbook(input);
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				totalRows = hssfSheet.getLastRowNum();
				// 读取Row,从第二行开始
				for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						rowList = new ArrayList<String>();
						totalCells = hssfRow.getLastCellNum();
						// 读取列，从第一列开始
						for (short c = 0; c <= totalCells + 1; c++) {
							HSSFCell cell = hssfRow.getCell(c);
							if (cell == null) {
								rowList.add(ExcelUtil.EMPTY);
								continue;
							}
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							rowList.add(ExcelUtil.getHValue(cell).trim());
						}
						list.add(rowList);
					}
				}
			}

			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
