package File;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileDemo {

	public static void main(String[] args) throws IOException {
		String path = "D:/data";
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		File f1 = new File(path+"/1.txt"); 
		if (!f1.exists()) {
			f1.createNewFile();// 创建目标文件
        }
		//如果FileWriter的构造参数为true，那么就进行内容追加;
        //如果FileWriter的构造参数为false,那么就进行内容的覆盖;
		
		FileWriter writer = null;
		writer = new FileWriter(f1,true);
		writer.append("your content");
	    writer.flush();
	    
		
		/*
		FileWriter fw = new FileWriter(file,false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("今天是个非常好日子");
        bw.flush();  
        bw.close();
        fw.close();
		*/
		
	}

}
