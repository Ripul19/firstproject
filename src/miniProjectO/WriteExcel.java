
//-----------------------Only to use when to make a new file 
//-----------------------a file is already bein created
//------------------creating new file before deleting old file or before changing the name might cause error in automation.java file




package miniProjectO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel 
{
	public static void main(String[]args) throws IOException
	{
		//Blank Workbook
		XSSFWorkbook workbook=new XSSFWorkbook();
		
		//Blank Sheet
		XSSFSheet sheet=workbook.createSheet("Sheet1");
		
		//creating the data
		Map<String, Object[]>dataSet =new TreeMap<String, Object[]>();
		dataSet.put("1",new Object[] {"Firefox"});
		dataSet.put("2",new Object[] {"Chrome"});
		
		//Iterate over the Data
		Set<String> set = dataSet.keySet();
		int rownum =0;
		
		for(String key : set)
		{
			Row row = sheet.createRow(rownum++);
			
			Object[] data = dataSet.get(key);
			int cellNum =0;
			for(Object value:data) {
				Cell cell = row.createCell(cellNum++);
				
				if( value instanceof String)
					cell.setCellValue((String)value);
			}
		}
		FileOutputStream writeFile = new FileOutputStream("BrowserData.xlsx");
		workbook.write(writeFile);
		
		writeFile.close();
		System.out.println("Excel file is created");
		workbook.close();
	}
}
