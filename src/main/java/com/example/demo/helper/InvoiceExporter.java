package com.example.demo.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.entities.Invoice;
import com.example.demo.entities.ItemEntry;

public class InvoiceExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private Integer rowCount = 0;
	private Integer coloumnCount = 0;
	private Row row;
	private FileInputStream file;
	Invoice invoice;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//NumToWords ntw;
	
	public InvoiceExporter(Invoice invoice) throws IOException {
		this.invoice = invoice;
		workbook = new XSSFWorkbook();
		file = new FileInputStream(new File(
				"C:\\Users\\p.subhash.nangare\\Documents\\Pranesh Enterprises\\AttendanceStart\\src\\main\\java\\com\\example\\demo\\helper\\inv.xlsx"));
		workbook = new XSSFWorkbook(file);
	}

	private void substitute() {
		CellStyle styleBlack = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(11);
		font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
		styleBlack.setFont(font);
		styleBlack.setAlignment(HorizontalAlignment.LEFT);
		styleBlack.setVerticalAlignment(VerticalAlignment.TOP);
		styleBlack.setBorderBottom(BorderStyle.THIN);
		styleBlack.setBorderTop(BorderStyle.THIN);
		styleBlack.setBorderLeft(BorderStyle.THIN);
		styleBlack.setBorderRight(BorderStyle.THIN);
		
		CellStyle styleNum = workbook.createCellStyle();
//		styleNum = styleBlack;
		styleNum.setFont(font);
		styleNum.setVerticalAlignment(VerticalAlignment.TOP);
		styleNum.setBorderBottom(BorderStyle.THIN);
		styleNum.setBorderTop(BorderStyle.THIN);
		styleNum.setBorderLeft(BorderStyle.THIN);
		styleNum.setBorderRight(BorderStyle.THIN);
		styleNum.setAlignment(HorizontalAlignment.CENTER);
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		// Buyer Name
		row = sheet.getRow(7);
		createCell(row, 0, invoice.getCustomer().getName(), styleBlack);
		// Address
		row = sheet.getRow(8);
		createCell(row, 0, invoice.getCustomer().getAddress(), styleBlack);
		// Invoice Number
		row = sheet.getRow(6);
		createCell(row, 4, invoice.getInvNo(), styleBlack);
		// Vendor Code
		row = sheet.getRow(7);
		createCell(row, 4, invoice.getCustomer().getVendorCode(), styleBlack);
		// Date
		row = sheet.getRow(8);
		createCell(row, 4, sdf.format(invoice.getDate()), styleBlack);
		// GST Number
		row = sheet.getRow(11);
		createCell(row, 1, invoice.getCustomer().getGstNumber(), styleBlack);

		int rownumber = 13;
		int srno = 1;
		int rate = 0;
		int qty = 0;

		List<ItemEntry> list = this.invoice.getItemList();
		for (ItemEntry entry : list) {
			rate = entry.getRate();
			qty = entry.getQty();
			if (srno > 7) {
				row = sheet.createRow(rownumber);
			} else {
				row = sheet.getRow(rownumber);
			}
			createCell(row, 0, srno, styleBlack);
			createCell(row, 1, entry.getItem().getItemName(), styleBlack);
			createCell(row, 2, entry.getItem().getHsnCode(), styleNum);
			createCell(row, 3, qty, styleNum);
			createCell(row, 4, rate, styleNum);
			createCell(row, 5, rate * qty, styleNum);
			rownumber++;
			srno++;
		}
		if(srno<=7) {
			rownumber = 20;
		}
		
		Integer excGST = invoice.getTotalAmount();
		Double gst = excGST*0.09;
		Double incGST = excGST + gst*2;
		Double roundOff = incGST - incGST.intValue();
		Integer grandTotal = (int) (incGST - roundOff);
		//Total
		row = sheet.getRow(rownumber++);
		createCell(row,5 , excGST , styleNum);
		//SGST
		row = sheet.getRow(rownumber++);
		createCell(row,5 , gst , styleNum);
		//CGST
		row = sheet.getRow(rownumber++);
		createCell(row,5 , gst , styleNum);
		//roundoff
		row = sheet.getRow(rownumber++);
		createCell(row,5 , roundOff , styleNum);
		//Grand Total
		row = sheet.getRow(rownumber++);
		createCell(row,5 , grandTotal , styleNum);
		//amount in words
		rownumber++;
		row = sheet.getRow(rownumber++);
		createCell(row,1 , NumToWords.convert(grandTotal) , styleNum);
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		// TODO Auto-generated method stub
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Float) {
			cell.setCellValue((Float) value);
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	public void export(HttpServletResponse response , String mailId) throws IOException {
//        writeHeaderLine();
//        writeDataLines();
		substitute();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

}
