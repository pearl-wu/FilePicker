package com.bais.filepicker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.orleonsoft.android.simplefilechooser.Constants;

import am.armsoft.data.Category;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class CustomfileActivity extends ListActivity{
	
	private ArrayList<String> extensions = new ArrayList<>();
	Category currentCategory;
	String record = "";
	ListView listView;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		int main = getResources().getIdentifier("multiselectorfile", "layout",  getPackageName());
		int lists = this.getResources().getIdentifier("list", "id", this.getPackageName());
		int icong = this.getResources().getIdentifier("icon", "id", this.getPackageName());
		int sline = this.getResources().getIdentifier("secondLine", "id", this.getPackageName());
		int fline = this.getResources().getIdentifier("firstLine", "id", this.getPackageName());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(main);
				
		Bundle extras = getIntent().getExtras();	
		if (extras != null) {
			if (extras.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS) != null) {
				extensions = extras.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS);				
				Uri fileUri = Files.getContentUri("external");  
		        String[] projection = new String[]{  
		              FileColumns.DATA,FileColumns.TITLE  
		        };
		        String selection = "";  
		        for(int i=0;i<extensions.size();i++)  
		        {  

		            if(i!=0)  
		            {  
		                selection = selection+" OR ";  
		            }  
		            selection = selection+FileColumns.DATA+" LIKE '%"+extensions.get(i)+"'";
		        }  
		        String sortOrder = FileColumns.DATE_MODIFIED;  
		        ContentResolver resolver = this.getContentResolver();  
		        Cursor cursor = resolver.query(fileUri, projection, selection, null, sortOrder);  

		        if(cursor == null)  
		            return;  
		        if(cursor.moveToLast()){ 
		        	List<HashMap<String, Object>> fillMaps;
		        	String[] from = new String[] { "icon", "filename", "filesize" };
		            int[] to = new int[] {icong, fline, sline };		        	
		            do{
		                String data = cursor.getString(0);
		                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
		                File filePath=new File(data);
		                String filename = filePath.getName();
		                Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();
		                Toast.makeText(this, FormaetfileSize(filePath.length()), Toast.LENGTH_SHORT).show();
		                
		                
		                fillMaps = new ArrayList<HashMap<String, Object>>();

		                HashMap<String, Object> map = new HashMap<String, Object>();
		                map.put("filetapy", filename.substring(filename.lastIndexOf(".")+1));
		                map.put("filename", filename); 
		                map.put("filesize", FormaetfileSize(filePath.length())); 
		                map.put("url",data);
		                fillMaps.add(map);

		            }while(cursor.moveToPrevious()); 
		            
	              SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, lists, from, to);
	              setListAdapter(adapter);
	               
		        }  
		        cursor.close();
			}			
		}
	}
	



	public static String FormaetfileSize(long size){		
		 if (size <= 0)
		      return "0";
		    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

		    return String.format("%.2f", size / Math.pow(1024, digitGroups))
		        + " " + units[digitGroups];
	}
	
}
