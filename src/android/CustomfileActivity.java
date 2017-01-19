package com.bais.filepicker;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class CustomfileActivity extends ListActivity implements OnItemClickListener {
	
	Context _context;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		int main = this.getResources().getIdentifier("main", "layout", this.getPackageName());
		int internal = this.getResources().getIdentifier("internal", "string", this.getPackageName());
		int drawer_layout = this.getResources().getIdentifier("drawer_layout", "id", this.getPackageName());
		int letf_drawer = this.getResources().getIdentifier("left_drawer", "id", this.getPackageName());
		int drawer_shadow = this.getResources().getIdentifier("drawer_shadow", "drawable", this.getPackageName());
		int drawer_open = this.getResources().getIdentifier("drawer_open", "string", this.getPackageName());
		int drawer_close = this.getResources().getIdentifier("drawer_close", "string", this.getPackageName());
		
		setContentView(main);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		_context =  this;
		Bundle extras = getIntent().getExtras();
		if( extras != null ){
			Toast.makeText(_context, extras+"", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
}
