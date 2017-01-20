package com.bais.filepicker;

import java.io.File;
import java.util.ArrayList;

import com.orleonsoft.android.simplefilechooser.Constants;

import am.armsoft.data.Category;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class CustomfileActivity extends Activity implements OnItemClickListener {
	
	private File currentFolder;
	private ArrayList<String> extensions;
	Category currentCategory;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		int main = getResources().getIdentifier("multiselectorfile", "layout",  getPackageName());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(main);
				
		Bundle extras = getIntent().getExtras();	
		if (extras != null) {
			if (extras.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS) != null) {
				extensions = extras.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS);
				//Toast.makeText(this, extensions.toString(), Toast.LENGTH_LONG).show();
				FileUtils.getSpecificTypeOfFile(this, new String[]{extensions.toString()});
			}			
		}
		
		currentFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		currentCategory = new Category();
		currentCategory.path = currentFolder.getAbsolutePath();		
			
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
}
