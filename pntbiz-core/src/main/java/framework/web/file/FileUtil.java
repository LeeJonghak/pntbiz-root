package framework.web.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.util.FileCopyUtils;

/**
 * 
 * @author jhlee
 * 
 *
 */
public class FileUtil {
	
	public FileUtil() {		
	}
	
	public boolean isFile(String fileName) {
		File f = new File(fileName);
		if(f.isFile()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 다운로드
	 * @param filesrc
	 * @param filename
	 * @param charset
	 * @param response
	 */
	public static void download(String filesrc, String filename, String charset, HttpServletResponse response) {	
		
		if(filesrc.length() == 0) {
			System.out.println("파일경로정보 오류");
		}
		try {
			File file = new File(filesrc+"/"+filename);
			if(!file.exists()) {
				throw new Exception();
			}			
			response.setContentType("application/octet-stream; charset="+charset);
			response.setHeader("Content-Disposition", "attachment;filename="+filename);				
			int read;
			byte readByte[] = new byte[4096];
			BufferedInputStream BIS = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream BOS = new BufferedOutputStream(response.getOutputStream());
			while((read=BIS.read(readByte, 0, 4096)) != -1) {
				BOS.write(readByte, 0, read);
			}
			BOS.flush();
			BOS.close();
			BIS.close();
		} catch (Exception e) {
			System.out.println("파일처리오류");
		}
	}
	
	public static void downloadURL(HttpServletRequest request, HttpServletResponse response, String url, String filename) throws IOException {
		File file = new File(filename);
		String userAgent = request.getHeader("User-Agent");
		boolean ie = userAgent.indexOf("MSIE") > -1;
		if(ie){
			filename = URLEncoder.encode(filename, "utf-8");
		} else {
			filename = new String(filename.getBytes("utf-8"));
        }
		FileUtils.copyURLToFile(new URL(url), file);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		} catch(Exception e) {
			e.printStackTrace();
        } finally {
        	if(fis != null){
        		try {
        			fis.close();
        		} catch(Exception e) {}
        	}        	
        }
		out.flush();
	}
	
	/**
	 * 파일명
	 * @param filename
	 * @return
	 */
	public static String getFileName(String filename) {
		int i = -1;
		String fname = "";
		if(( i = filename.lastIndexOf(".")) != -1){      
	        fname = filename.substring(0,i); // except file extension
	    }  
		return fname;
	}
	
	/**
	 * 확장자명
	 * @param filename
	 * @return
	 */
	public static String getFileExt(String filename) {
		int i = -1;
		String ext = "";
		if(( i = filename.lastIndexOf(".")) != -1){      
	        ext = filename.substring(i); // only file extension
	    }  
		return ext;
	}
	
	/**
	 * file1 > file2로 이름변경 / 경로 포함
	 * @param file1
	 * @param file2
	 */
	public static boolean rename(String file1, String file2) {
		File fromFile = new File(file1);
		File toFile = new File(file2);
		if(toFile.exists()) {
			System.out.println("이미 존재하는 파일");
			return false;
		} else {
			fromFile.renameTo(toFile);
			return true;
		}
	}
	
	public static boolean delete(String fileName) {
		File f = new File(fileName);
		if(f.isFile()) {
			f.delete();
			return true;
		} else {
			return false;
		}
	}
}
