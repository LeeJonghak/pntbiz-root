package framework.web.excel;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author jhlee
 * 
 * CSVWriter csv = new CSVUtil(fileName[파일명], fileSrc[경로], delemiter[구분자], charset[캐릭셋]);
 * String[] column = "column1#column2#column3".split("#");
 * csv.writeNext(column);
 *
 */
public class CSVUtil {
	
	public CSVUtil() {		
	}
	
	public CSVWriter create(String fileSrc, String fileName, char delimiter, String charset) {
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileSrc + fileName);
		} catch (IOException e) {
			System.out.println("csv create fail");
			e.printStackTrace();
		}	
		// CSV는 UTF-8일 경우 한글이 깨지므로 아래의 스트링을 붙인다.
		if("UTF-8".equals(charset)) {
			byte[] utf8Bom = { (byte)0xEF, (byte)0xBB, (byte)0xBF};
			String utf8String = new String(utf8Bom);
			try {
				fw.append(utf8String);
			} catch (IOException e) {
				System.out.println("utf8 append fail");
				e.printStackTrace();
			}
		}
		CSVWriter csv = new CSVWriter(fw, delimiter);
		return csv;
	}
}
