package com.bais.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CustomfileActivity extends Activity{
	
	private ArrayList<String> extensions = new ArrayList<String>();
	String record = "";
	ListView listView;
	private String[] url;
	private long[] sz;
	GridView gridGallery;
	GalleryAdapter adapter;
	private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
	private static final int REQUEST_CONTACTS = 1;

	@RequiresApi(api = Build.VERSION_CODES.N)
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    int main = this.getResources().getIdentifier("multiselectorfile", "layout", this.getPackageName());
		setContentView(main);
		nameinfo();
	}


	@RequiresApi(api = Build.VERSION_CODES.N)
	public void nameinfo(){
		Bundle extras = getIntent().getExtras();
		ArrayList<String> exts = new ArrayList<String>(extras.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS));
		extensions = extras.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS);
		if (extras != null) {
			final List<HashMap<String, Object>> fillMaps = new ArrayList<HashMap<String, Object>>();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();

			//內存路徑
			Collection<File> founcFiles = FileUtils.listFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath()),TrueFileFilter.INSTANCE, DirectoryFileFilter.DIRECTORY);
			for (File f : founcFiles) {
				for(int a=0;a<exts.size();a++) {
					if (f.toString().endsWith(exts.get(a))){
						File files = new File(f.toString());
						long sizeInBytes = files.length();
						c.setTimeInMillis(f.lastModified());
						String tape = exts.get(a);
						if (f.toString().endsWith("7z")) tape = "z";
						int flags = this.getResources().getIdentifier(tape, "drawable", this.getPackageName());
						HashMap<String, Object> map = new HashMap<String, Object>();
						String[] tokens = f.toString().split("/");
						map.put("filetapy", flags);
						map.put("filename",tokens[tokens.length-1]);
						map.put("filesize", FormaetfileSize(sizeInBytes));
						map.put("filetime", formatter.format(c.getTime()));
						map.put("fileurl", f.toString());
						map.put("sizeall", sizeInBytes);
						fillMaps.add(map);
						//Log.d("-----Found file-----", "Found file:" + f.toString()+"--"+sizeInBytes);
					}
				}
			}

			//SD外存路徑
			/*if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可
				Log.d("-----Found file-----", "Found file:" + this.getExternalFilesDir("/").getAbsolutePath());
				this.getExternalFilesDir("/").getAbsolutePath();
			}*/

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

					listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
						public void onItemClick(AdapterView<?> parent, View v, int position, long id)
						{
							Toast.makeText(CustomfileActivity.this, "您點選了第 "+fillMaps.get(0).get("fileurl"), Toast.LENGTH_SHORT).show();
							Intent data = new Intent();
							data.putExtra("single_path", fillMaps.get(0).get("fileurl").toString());
							data.putExtra("sz", fillMaps.get(0).get("sizeall").toString());
							try{
								setResult(RESULT_OK, data);
							} catch (Exception e){
								e.printStackTrace();
							}
							finish();
						}
					});
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
