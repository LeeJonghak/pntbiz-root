package framework.web.ftp;

import framework.web.util.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPClient {
	
	public String server = "";
	public int port = 0;
	public String id = "";
	public String password = "";
	public String privateKey = "";
	
	public JSch jsch;	
	public Properties config;
	
	public Session session = null;
	public Channel channel = null;
	public ChannelSftp c = null;

	public SFTPClient(String server, int port, String id, String password) {
		this.server = server;
		this.port = port;
		this.id = id;
		this.password = password;

		jsch = new JSch();
		config = new Properties();
	}
	
	public SFTPClient(String server, int port, String id, String password, String privateKey) {
		this.server = server;
		this.port = port;
		this.id = id;
		this.password = password;
		this.privateKey = privateKey;
		
		jsch = new JSch();		
		config = new Properties();
	}
	
	/**
	 * SFTP접속
	 * @return
	 * @throws IOException 
	 */
	public boolean connect()  throws JSchException, IOException {
		System.out.println("==> Connecting to : " + server);
		boolean isError = false;
		try {
			// 세션객체 생성 및 접속설정
			if(!("".equals(this.privateKey) || this.privateKey == null)) {
				jsch.addIdentity(this.privateKey);
			}
			session = jsch.getSession(id, server, port);
			session.setPassword(password);
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);			
			// 접속
			session.connect();
			// sftp 채널을 연다.
			channel = session.openChannel("sftp");
			// 채널에 연결
			channel.connect();
			isError = true;
		} catch (JSchException e) {
			e.printStackTrace();
			session.disconnect();
			channel.disconnect();
			isError = false;	
			System.err.println("Unable to connect to FTP server. "+e.toString());  
		}
		c = (ChannelSftp) channel;
		return isError;
	}
	
	/**
	 * disconnect
	 * @return
	 */
	public boolean disconnect()
	{
		boolean isError = false;
		try {
			session.disconnect();
			channel.disconnect();
			c.quit();
		} catch(Exception e) {
			isError = true;
			System.out.println("Unable to disconnect from FTP server. " + e.toString());
		}
		return isError;
	}	
	
	/**
	 * 경로변경
	 * @param path
	 */
	public void cd(String path)
	{
		try {
			System.out.println("Change directory : " + path);
			c.cd(path);
		} catch (SftpException se) {
			System.out.println("Change directory error " + se.toString());
		}
	}
	
	/**
	 * 업로드
	 * @param upFile
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public boolean upload(String upFile) throws SocketException, IOException {
		System.out.println("==> Uploading : " + server);		
		File uploadFile = new File(upFile);
		FileInputStream fis = null;
		boolean isError = false;
		try {
			fis = new FileInputStream(uploadFile);
			c.put(fis, uploadFile.getName());
			System.out.println("Upload Success");
		} catch(IOException ioe) {
			ioe.printStackTrace();
			isError = true;
		} catch(Exception e) {
			e.printStackTrace();
			isError = true;
			System.out.println("Error in Upload Attachments Module. Error Description : " + e.toString());
		} finally {
			if( fis!=null ) {
				try {
					fis.close();
				}catch(IOException ioe) {}
			}
		}
		return isError;
	}
	
	/**
	 * 파일삭제
	 * @param file
	 * @return
	 * @throws SftpException
	 */
	public boolean delete(String file) throws SftpException {
		try {
			c.rm(file);
			return true;
		} catch (SftpException e) {
			e.printStackTrace();
			System.out.println("Error in Delete file. Error Description : " + e.toString());
			return false;
		}
		
	}
	
	/**
	 * 전체목록
	 * @param path
	 * @return
	 * @throws SftpException
	 */
	@SuppressWarnings("rawtypes")
	public List<String> list(String path) throws SftpException {
		List<String> list = new ArrayList<String>();
		Vector flist = c.ls(path);
		for(int i=0; i<flist.size(); i++) {
			LsEntry file = (LsEntry) flist.get(i);
			String filename = file.getFilename();
			if(!(".".equals(filename) || "..".equals(filename))) {
				list.add(filename);
			}
		}
		return list;
	}
	
	/**
	 * 디렉토리목록
	 * @param path
	 * @return
	 * @throws SftpException
	 */
	@SuppressWarnings("rawtypes")
	public List<String> dirlist(String path) throws SftpException {
		List<String> list = new ArrayList<String>();
		Vector flist = c.ls(path);
		for(int i=0; i<flist.size(); i++) {
			LsEntry file = (LsEntry) flist.get(i);
			String filename = file.getFilename();
			String filename2 = file.getLongname(); 
			String dir = filename2.substring(0, 1);
			if("d".equals(dir)) {
				list.add(filename);
			}
		}
		return list;
	}
	
	/**
	 * 파일목록
	 * @param path
	 * @return
	 * @throws SftpException
	 */
	@SuppressWarnings("rawtypes")
	public List<String> filelist(String path) throws SftpException {
		List<String> list = new ArrayList<String>();		
		Vector flist = c.ls(path);		
		for(int i=0; i<flist.size(); i++) {
			LsEntry lsEntry = (LsEntry) flist.get(i);
			String filename = lsEntry.getFilename();
			String filename2 = lsEntry.getLongname();			
			String dir = filename2.substring(0, 1);
			if(!(".".equals(filename) || "..".equals(filename) || "d".equals(dir))) {
				list.add(filename);
			}
		}
		return list;
	}
	
	/**
	 * 파일체크
	 * @param path (절대경로)
	 * @param filename (확장자포함 파일명)
	 * @return
	 * @throws SftpException
	 */
	public boolean isFile(String path, String filename) throws SftpException {
		List<String> list = new ArrayList<String>();
		list = filelist(path);
		boolean chk = false;
		for(int i=0; i<list.size(); i++) {
			if(filename.equals(list.get(i))) {
				chk = true;
			}
		}
		return chk;
		
	}
	
	/**
	 * 디렉토리체크
	 * @param path (절대경로)
	 * @param dirname (디렉토리명)
	 * @return
	 * @throws SftpException
	 */
	public boolean isDir(String path, String dirname) throws SftpException {
		List<String> list = new ArrayList<String>();
		boolean chk = false;		
		list = this.dirlist(path);			
		for(int i=0; i<list.size(); i++) {
			System.out.println("isDir: "+ list.get(i)+","+dirname);
			if(dirname.equals(list.get(i))) {
				chk = true;
			}
		}
		return chk;
	}
	
	/**
	 * 디렉토리생성
	 * @param path
	 * @throws SftpException
	 */
	public void mkdir(String path) throws SftpException {
		try {
			c.mkdir(path);
		} catch (SftpException e) {
			e.printStackTrace();
			System.out.println("Make Directory Error Description : " + e.toString());
		}
	}
	
	/**
	 * 필수 디렉토리 생성(comuum > beacon, floor, images, sound)
	 * @param path
	 * @throws SftpException
	 */
	public void mkdirList(String path) throws SftpException {
		try {
			c.mkdir(path);
			c.mkdir(path + "/beacon");
			c.mkdir(path + "/floor");
			c.mkdir(path + "/images");
			c.mkdir(path + "/sound");
		} catch (SftpException e) {
			e.printStackTrace();
			System.out.println("Make Directory Error Description : " + e.toString());
		}
	}
	
	/**
	 * 날짜 디렉토리생성
	 * @param path
	 * @throws SftpException
	 */
	public String mkdirDate(String path) throws SftpException {
		String dir = "";
		
		try {		
			String today = DateUtil.getDate("yyyyMM");
			
			boolean isDirFlag = isDir(path, today);
			
			dir = path + "/" + today;
			
			if(!isDirFlag){
				c.mkdir(dir);
			}

		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Make Directory Error Description : " + e.toString());
		}
		
		return dir;
	}
	
	/**
	 * 디렉토리삭제
	 * @param path
	 * @throws SftpException
	 */
	public void rmdir(String path) throws SftpException {
		try {
			c.rmdir(path);
		} catch (SftpException e) {
			e.printStackTrace();
			System.out.println("Remove Directory Error Description : " + e.toString());
		}
	}
}