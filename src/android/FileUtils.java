package com.bais.filepicker;

import android.content.ContentResolver;  
import android.content.Context;  
import android.database.Cursor;  
import android.net.Uri;  
import android.provider.MediaStore.Files;  
import android.provider.MediaStore.Files.FileColumns;  
import android.util.Log; 
import android.widget.Toast;

public class FileUtils {
	 public static void getSpecificTypeOfFile(Context context,String[] extension){  

	        Uri fileUri=Files.getContentUri("external");  
	        String[] projection=new String[]{  
	                FileColumns.DATA,FileColumns.TITLE  
	        };  
	        String selection="";  
	        for(int i=0;i<extension.length;i++)  
	        {  
	            if(i!=0)  
	            {  
	                selection=selection+" OR ";  
	            }  
	            selection=selection+FileColumns.DATA+" LIKE '%"+extension[i]+"'";  
	        }  
	        String sortOrder=FileColumns.DATE_MODIFIED;  
	        ContentResolver resolver=context.getContentResolver();  
	        Cursor cursor=resolver.query(fileUri, projection, selection, null, sortOrder);  
	        if(cursor==null)  
	            return;  
	        if(cursor.moveToLast())  
	        {  
	            do{
	                String data=cursor.getString(0);  
	                Log.d("tag", data);  
	                //Toast.makeText(context, data, Toast.LENGTH_LONG).show();
	            }while(cursor.moveToPrevious());  
	        }  
	        cursor.close();  
	}  
}