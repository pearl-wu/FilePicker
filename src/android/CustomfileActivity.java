package com.bais.filepicker;

import java.io.File;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale.Category;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class CustomfileActivity extends Activity{
	
	private ArrayList<String> extensions = new ArrayList<>();
	Category currentCategory;
	String record = "";
	ListView listView;
	private String[] url;
	private long[] sz;
	GridView gridGallery;
	GalleryAdapter adapter;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    int main = this.getResources().getIdentifier("multiselectorfile", "layout", this.getPackageName());
		setContentView(main);		
				
		Bundle extras = getIntent().getExtras();
		extensions = extras.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS);	

		if (extras != null) {
			if (extras.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS) != null) {

		        String[] projection = new String[]{  
		        	MediaStore.Files.FileColumns.TITLE,
		        	MediaStore.Files.FileColumns.DATA,
		        	MediaStore.Files.FileColumns.DATE_ADDED,
		        	MediaStore.Files.FileColumns.SIZE,
		        	MediaStore.Files.FileColumns.DATE_MODIFIED
		        };
		        Uri uri = MediaStore.Files.getContentUri("external"); 
		        String selection = "";  
		        for(int i=0;i<extensions.size();i++)  
		        {  
		            if(i!=0)  
		            {  
		                selection = selection+" OR ";  
		            }  
		            selection = selection+MediaStore.Files.FileColumns.DATA+" LIKE '%"+extensions.get(i)+"' ";
		        }  
		        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";  
		        ContentResolver resolver = this.getContentResolver();  
		        Cursor cursor = resolver.query(uri, projection, selection, null, sortOrder);  
		        
		        
		       List<HashMap<String, Object>> fillMaps;
		       
		        url = new String[cursor.getCount()];
		        sz = new long[cursor.getCount()];
		        fillMaps = new ArrayList<HashMap<String, Object>>();
		        if(cursor == null)  
		            return;  
		        
		        if(cursor.moveToLast()){ 	

		        	for(int a=0; a<cursor.getCount(); a++){
		        	   cursor.moveToPosition(a);
		               String data = cursor.getString(1);
		               String[] tokens = data.split("/");
		               String filename = tokens[tokens.length-1];
		               long sx = Long.valueOf(cursor.getString(3));
		               long timestamp = Long.parseLong(cursor.getString(4));
		               File files = new File(data);
		               Calendar cal = Calendar.getInstance();
		               long time = files.lastModified();
		               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		               cal.setTimeInMillis(time);
		               //Toast.makeText(getApplication(), formatter.format(cal.getTime()), Toast.LENGTH_SHORT).show();
		               
		               HashMap<String, Object> map = new HashMap<String, Object>();
		               //Toast.makeText(this, String.valueOf(filename.substring(filename.lastIndexOf(".")+1).toLowerCase()), Toast.LENGTH_SHORT).show();
		               String at = String.valueOf(filename.substring(filename.lastIndexOf(".")+1).toLowerCase());
		               if(at=="7z") at = "z";
		               int flags = this.getResources().getIdentifier(at, "drawable", this.getPackageName());
		               map.put("filetapy", flags);
		               map.put("filename", filename);
		               map.put("filesize", FormaetfileSize(sx));
		               map.put("filetime", formatter.format(cal.getTime()));
		               fillMaps.add(map);
		               sz[a] = sx;
		               url[a] = data;     
		               //Toast.makeText(this, sz[a]+"", Toast.LENGTH_SHORT).show();
		            }
		        	 cursor.close();
		        	
		    	    int main_item = this.getResources().getIdentifier("multiselectorfile_item", "layout", this.getPackageName());
		    	    int lists = this.getResources().getIdentifier("list", "id", this.getPackageName());
		    	    int icong = this.getResources().getIdentifier("icon", "id", this.getPackageName());
		    	    int sline = this.getResources().getIdentifier("secondLine", "id", this.getPackageName());
		    	    int time = this.getResources().getIdentifier("timeLine", "id", this.getPackageName());
		    	    int fline = this.getResources().getIdentifier("firstLine", "id", this.getPackageName());
		        	String[] from = new String[] { "filetapy", "filename", "filesize", "filetime"};
		            int[] to = new int[] {icong, fline, sline, time };
		            listView = (ListView)findViewById(lists);
		            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), fillMaps, main_item, from, to);
		            listView.setAdapter(adapter);					
		        } 	
		       
			}		
		}	
		
		listView.setOnItemClickListener(new OnItemClickListener()
        {
             public void onItemClick(AdapterView<?> parent, View v, int position, long id)
             {            	 
                 //Toast.makeText(CustomfileActivity.this, "您點選了第 "+sz[position], Toast.LENGTH_SHORT).show();
            	 Intent data = new Intent();
            	 data.putExtra("single_path", url[position]);
            	 data.putExtra("sz", sz[position]);
            	 try{
     				setResult(RESULT_OK, data);
	     		 } catch (Exception e){
	     				e.printStackTrace();
	     		 }
	     		 finish();
             }
        }
       );
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
