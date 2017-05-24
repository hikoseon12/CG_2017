package climbing;

import java.io.File;
import java.util.ArrayList;

public class _FileReader {
	
	/* @params current path(Relative or Absolute), File ext : txt or conf
	 * @return ArrayList of file names
	 * */
	public static ArrayList<String> findFiles(String path, String ext){
		File dirFile=new File(path);
		File []fileList=dirFile.listFiles();
		ArrayList<String> ret = new ArrayList<String>();
		for(File x : fileList) {
			if(x.isFile()&&x.getName().contains("."+ext)){
				String curFileName=x.getName();
				ret.add(curFileName);
			}
		}
		return ret;
	}
	public static ArrayList<String> findDirectory(String path){
		File dirFile=new File(path);
		File []dirList=dirFile.listFiles();
		ArrayList<String> ret = new ArrayList<String>();
		for(File x : dirList) {
			if(x.isDirectory()){
				String curDirectoryName=x.getName();
				ret.add(curDirectoryName);
			}
		}
		return ret;
	}
}
