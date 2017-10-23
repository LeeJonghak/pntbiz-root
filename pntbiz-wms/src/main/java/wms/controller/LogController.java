package wms.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import framework.web.file.FileUtil;
import framework.web.util.DateUtil;
 
@Controller
public class LogController {		
		
	@RequestMapping(value="/log/tomcat", method=RequestMethod.GET)
	@ResponseBody
	public String tomcat() throws IOException, InterruptedException{
		String sh = System.getenv().get("HOME") + "/bin/tomcat_catalina.sh";
		Runtime run = Runtime.getRuntime();
		Process process = run.exec(sh);
		StringBuffer sb = new StringBuffer(300000);
        process.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";        
        while ((line = reader.readLine())!= null) {
        	sb.append(line + "\n");
        }
        process.destroy();
        return sb.toString();
	}	
	
	@RequestMapping(value="/log/acc", method=RequestMethod.GET)
	@ResponseBody
	public String acc() throws IOException, InterruptedException{
		String sh = System.getenv().get("HOME") + "/bin/apache_acc.sh";
		Runtime run = Runtime.getRuntime();
		Process process = run.exec(sh);
		StringBuffer sb = new StringBuffer(300000);
        process.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";        
        while ((line = reader.readLine())!= null) {
        	sb.append(line + "\n");
        }
        process.destroy();
        return sb.toString();
	}	
	
	@RequestMapping(value="/log/ecc", method=RequestMethod.GET)
	@ResponseBody
	public String ecc() throws IOException, InterruptedException{
		String sh = System.getenv().get("HOME") + "/bin/apache_ecc.sh";
		Runtime run = Runtime.getRuntime();
		Process process = run.exec(sh);
		StringBuffer sb = new StringBuffer(300000);
        process.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";        
        while ((line = reader.readLine())!= null) {
        	sb.append(line + "\n");
        }
        process.destroy();
        return sb.toString();
	}	
	
	@RequestMapping(value="/logdown/tomcat", method=RequestMethod.GET)
	public void logdownTomcat(HttpServletResponse response) throws IOException, InterruptedException{
		String date = DateUtil.getDate("yyyy-MM-dd");
		String filename = "catalina." + date + ".log";
		FileUtil.download("/data/logs/pntbiz_wms/catalina-log", filename, "UTF-8", response);		
	}	
	
	@RequestMapping(value="/logdown/acc", method=RequestMethod.GET)
	public void logdownAcc(HttpServletResponse response) throws IOException, InterruptedException{
		String date = DateUtil.getDate("yyyyMMddHH");
		String filename = "ssl_access_log." + date;
		FileUtil.download("/data/logs/pntbiz_wms/access-log", filename, "UTF-8", response);		
	}	
	@RequestMapping(value="/logdown/ecc", method=RequestMethod.GET)
	public void logdownEcc(HttpServletResponse response) throws IOException, InterruptedException{
		String date = DateUtil.getDate("yyyy-MM-dd");
		String filename = "ssl_error_log." + date;
		FileUtil.download("/data/logs/pntbiz_wms/error-log", filename, "UTF-8", response);		
	}	

}