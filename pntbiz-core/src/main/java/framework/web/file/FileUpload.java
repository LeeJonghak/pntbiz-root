package framework.web.file;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class FileUpload extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param charset 캐릭터셋
	 * @param savePath 파일저장경로
	 * @param fileSize 파일최대전송크기
	 * @return 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void upload(HttpServletRequest request, HttpServletResponse response, String charset, String savePath, int fileSize) throws ServletException, IOException {
		response.setContentType("text/html;charset="+charset);
		PrintWriter out = response.getWriter();		
		request.setCharacterEncoding(charset);
		int maxSize = fileSize * 1024 * 1024;
		
		try {
			MultipartRequest multi = new MultipartRequest(request, savePath, maxSize, charset, new DefaultFileRenamePolicy());
			Enumeration<?> params = multi.getParameterNames();
			while (params.hasMoreElements()) {
				String name = (String) params.nextElement();
				String value = multi.getParameter(name);
				out.println(name + " : " + value);
			}
			/*
			Enumeration<?> files = multi.getFileNames();
			while (files.hasMoreElements()) {
				String name = (String) files.nextElement();
				String fileName = multi.getFilesystemName(name);
				String type = multi.getContentType(name);
				File f = multi.getFile(name);
			}
			*/
		} catch (Exception e) {
		}
	}
	
}