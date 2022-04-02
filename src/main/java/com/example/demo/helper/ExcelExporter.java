package com.example.demo.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.entities.Attendance;
import com.example.demo.entities.User;

public class ExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
	private Map<User, List<Attendance>> recordMap;
	private Integer rowCount = 0;
	private Integer coloumnCount = 0;
    private Row row;
    private Date from;
    private Date to;
    public ExcelExporter(Map<User, List<Attendance>> map, Date from, Date to) {
        this.recordMap = map;
        this.from = from;
        this.to = to;
        workbook = new XSSFWorkbook();
    }

	private void writeHeaderLine() {
        sheet = workbook.createSheet("Report");
        CellStyle style = workbook.createCellStyle();
        CellStyle styleTitle = workbook.createCellStyle();
        CellStyle styleSubTitle = workbook.createCellStyle();
        
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeight(32);
        titleFont.setBold(true);
        titleFont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        styleTitle.setFont(titleFont);
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        
        XSSFFont subTitleFont = workbook.createFont();
        subTitleFont.setFontHeight(20);
        subTitleFont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        styleSubTitle.setFont(subTitleFont);
        styleSubTitle.setAlignment(HorizontalAlignment.CENTER);
        
        //Get the month to put in the title
		//get the month
        String month;
		int month1 = this.from.getMonth();
		int month2 = this.to.getMonth();
		HashMap<Integer, String> monthMap = getMonthMap();
		if (month1 == month2) {
			month = monthMap.get(month1);
		}
		else {
			month = monthMap.get(month1) + " - " + monthMap.get(month2);
		}
        row = sheet.createRow(rowCount++);
        createCell(row , 10 , "Pranesh Enterprises", styleTitle);
        
        row = sheet.createRow(rowCount++);
        createCell(row , 10 , "Attendance Status for " + month , styleSubTitle);
        
        //leave one row after subtitle
        rowCount++;
        row = sheet.createRow(rowCount++); 
        createCell(row, 0, "Sr No", style);  
        
        sheet.autoSizeColumn(0);
        
        //sheet.setColumnWidth(2, 40*256);
        createCell(row, 1, "Name", style);
        createCell(row , 2 , "Present Days" , style);
        sheet.autoSizeColumn(2);
        for (int i = 1; i <=31 ; i++) {
        	sheet.setColumnWidth(i+2, 5*256);
        	createCell(row,i+2,i,style); 
        }
    }
    
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        //sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if(value instanceof Float) {
        	cell.setCellValue((Float) value);
        }
        else if(value instanceof Double) {
        	cell.setCellValue((Double) value);
        }
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
    
    private void writeDataLines() {
        //int rowCount = 1;
 
    	CellStyle styleBlack = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        styleBlack.setFont(font);
        styleBlack.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle styleBlackName = workbook.createCellStyle();
        XSSFFont fontBlackName = workbook.createFont();
        fontBlackName.setFontHeight(14);
        fontBlackName.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        styleBlackName.setFont(fontBlackName);
        styleBlackName.setAlignment(HorizontalAlignment.LEFT);
        
        CellStyle styleHolidayBlack = workbook.createCellStyle();
        XSSFFont fontHolidayBlack = workbook.createFont();
        fontHolidayBlack.setFontHeight(14);
        fontHolidayBlack.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        styleHolidayBlack.setFont(fontHolidayBlack);
        styleHolidayBlack.setAlignment(HorizontalAlignment.CENTER);
        styleHolidayBlack.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
        styleHolidayBlack.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        CellStyle styleHolidayRed = workbook.createCellStyle();
        XSSFFont fontHolidayRed = workbook.createFont();
        fontHolidayRed.setFontHeight(14);
        fontHolidayRed.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        styleHolidayRed.setFont(fontHolidayRed);
        styleHolidayRed.setAlignment(HorizontalAlignment.CENTER);
        styleHolidayRed.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
        styleHolidayRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        CellStyle styleRed = workbook.createCellStyle();
        XSSFFont fontRed = workbook.createFont();
        fontRed.setFontHeight(14);
        fontRed.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        styleRed.setFont(fontRed);
        styleRed.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle styleDouble = workbook.createCellStyle();
        styleDouble.setDataFormat(
        		workbook.getCreationHelper().createDataFormat().getFormat("0.0"));
        styleDouble.setFont(font);
        styleDouble.setAlignment(HorizontalAlignment.CENTER);
        
        XSSFFont fontTotal = workbook.createFont();
        fontTotal.setBold(true);
        fontTotal.setFontHeight(14);
        CellStyle styleTotal = workbook.createCellStyle();
        styleTotal.setFont(fontTotal);
        styleTotal.setAlignment(HorizontalAlignment.CENTER);
        
        String status = "";
        int sr = 1;
        Double totalCount = 0.0;
        for (User key : recordMap.keySet()) {
            row = sheet.createRow(rowCount++);
            int columnCount = 0;
            Double count=0.0;
            createCell(row, columnCount++, sr++, styleBlack); 
            createCell(row, columnCount++, key.getName(), styleBlackName);
            
            List<Attendance> list = this.recordMap.get(key);	
            for(Attendance attendance : list) {
            	Date attendanceDate = attendance.getAttendanceDate();
            	int day= attendanceDate.getDate();
				if (attendance.getStatus().equals("present")) {
					status = "P";
					count++;
					createCell(row, columnCount + day, status, styleBlack);
				} else if (attendance.getStatus().equals("absent")) {
					status = "A";
					createCell(row, columnCount + day, status, styleRed);
				} else if (attendance.getStatus().equals("halfDay")) {
					status = "H";
					count+=0.5;
					createCell(row, columnCount + day, status, styleBlack);
				}
				//check if it is sunday
				if(attendanceDate.getDay() == 0) {
					if(attendance.getStatus().equals("absent")) {
						createCell(row, columnCount + day, status, styleHolidayRed);
					}
					else {
						createCell(row, columnCount + day, status, styleHolidayBlack);
					}
				}
            }
            if(count%1 == 0) {
            	createCell(row , columnCount , count, styleBlack);
            }else {
            createCell(row , columnCount , count , styleDouble);
            }
            totalCount += count;
        }
        
        Row row = sheet.createRow(rowCount);
        createCell(row , 1 , "Total = " , styleTotal);
        createCell(row , 2 , totalCount , styleTotal);
        sheet.autoSizeColumn(1);
        
    }
    
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
    
    public HashMap<Integer, String> getMonthMap(){
    	return new HashMap<Integer, String>(){{
    		put(0 , "January");
    		put(1 , "February");
    		put(2 , "March");
    		put(3 , "April");
    		put(4 , "May");
    		put(5 , "June");
    		put(6 , "July");
    		put(7 , "August");
    		put(8 , "September");
    		put(9 , "October");
    		put(10 , "November");
    		put(11 , "December");
    	}};
    }
    
}
