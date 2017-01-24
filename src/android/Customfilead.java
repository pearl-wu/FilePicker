package com.bais.filepicker;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Customfilead extends BaseAdapter{
	private LayoutInflater inflater;
    String [] key;
    String [] value;
    
    public Customfilead(Context c,String [] key,String [] value){
        inflater = LayoutInflater.from(c);
        this.key = key;
        this.value = value;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return key.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			
		/*convertView = inflater.inflate(R.id.,false);
        TextView key2,value2;
        key2 = (TextView) convertView.findViewById(R.id.key);
        value2 = (TextView) convertView.findViewById(R.id.value);
        key2.setText(key[position]);
        value2.setText(value[position]);
        return convertView;*/
	}
  

}

