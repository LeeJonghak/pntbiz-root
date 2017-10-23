package framework.web.file;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;

public class FileUploadRename implements FileRenamePolicy {
  
	private String time;  // point 14 'yyyyMMddHHmmss'
	private String userId;
	
	public FileUploadRename() {}
	public FileUploadRename(String userId , String time) {
		this.userId = userId;
		this.time = time;
	}  
	public File rename(File f) { 
		//  Get the parent directory path as in h:/home/user or /home/user
		String parentDir = f.getParent();
      
        //Get filename without its path location, such as 'index.txt'
        String fname = f.getName();
      
        //Get the extension if the file has one
        String fileExt = "";
        int i = -1;
        if(( i = fname.lastIndexOf(".")) != -1){      
            fileExt = fname.substring(i); // only file extension
            fname = fname.substring(0,i); // except file extension
        }      
        //add the timestamp
        fname =userId +"_" + fname + "_" + time;      
        //piece together the filename
        fname = parentDir + System.getProperty("file.separator") + fname + fileExt;      
        File file = new File(fname);
        return file;
	}
}