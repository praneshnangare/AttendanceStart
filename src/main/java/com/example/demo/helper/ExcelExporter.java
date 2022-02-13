package com.example.demo.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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
     
    public ExcelExporter(Map<User, List<Attendance>> map) {
        this.recordMap = map;
        workbook = new XSSFWorkbook();
    }

	private void writeHeaderLine() {
        sheet = workbook.createSheet("Report");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
         
        createCell(row, 0, "Sr No", style);  
        sheet.autoSizeColumn(0);
        
        //sheet.setColumnWidth(2, 40*256);
        createCell(row, 1, "Name", style);
        createCell(row , 2 , "Present Days" , style);
        sheet.autoSizeColumn(2);
        for (int i = 1; i <31 ; i++) {
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
        int rowCount = 1;
 
        CellStyle styleBlack = workbook.createCellStyle();
        CellStyle styleRed = workbook.createCellStyle();
        CellStyle styleDouble = workbook.createCellStyle();
        styleDouble.setDataFormat(
        		workbook.getCreationHelper().createDataFormat().getFormat("0.0"));
        
        XSSFFont font = workbook.createFont();
        XSSFFont fontRed = workbook.createFont();
        
        font.setFontHeight(14);
        fontRed.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        
        fontRed.setFontHeight(14);
        fontRed.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        
        styleBlack.setFont(font);
        styleBlack.setAlignment(HorizontalAlignment.CENTER);
        
        styleDouble.setFont(font);
        styleDouble.setAlignment(HorizontalAlignment.CENTER);
        
        styleRed.setFont(fontRed);
        styleRed.setAlignment(HorizontalAlignment.CENTER);
        
        String status = "";
        
        for (User key : recordMap.keySet()) {
            Row row = sheet.createRow(rowCount);
            int columnCount = 0;
            Double count=0.0;
            createCell(row, columnCount++, rowCount++, styleBlack); 
            createCell(row, columnCount++, key.getName(), styleBlack);
            

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
            }
            if(count%1 == 0) {
            	createCell(row , columnCount , count , styleBlack);
            }else {
            createCell(row , columnCount , count , styleDouble);
            }
        }
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
    
}
