package com.bais.filepicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;



public class FileUtils {
    private String SDPATH;

    public String getSDPATH() {
        return SDPATH;
    }
    public FileUtils() {
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    public File creatSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }
    
    public File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdirs();
        return dir;
    }

    public boolean isFileExist(String fileName){
        File file = new File(SDPATH + fileName);
        Log.i(".......................................", SDPATH + fileName);  		
        return file.exists();
    }
    
	public File write2SDFromInput(String path,String fileName,InputStream input){
    	
        File file = null;
        OutputStream output = null;
        try{
            creatSDDir(path);
            file = creatSDFile(path + fileName);
            output = new FileOutputStream(file);   //寫入數據
            byte buffer [] = new byte[8 * 1024];
            int temp = 0;
            while((temp = input.read(buffer)) != -1){
                output.write(buffer,0, temp);
            }
            output.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
            	if(input!= null)
                output.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            try {
                if (output!=null)
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}