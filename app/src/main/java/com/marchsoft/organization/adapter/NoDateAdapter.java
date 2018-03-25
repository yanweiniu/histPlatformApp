package com.marchsoft.organization.adapter;

import java.util.ArrayList;

import com.marchsoft.organization.R;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NoDateAdapter extends BaseAdapter {
	private Activity mContext;
	
	public NoDateAdapter(Activity context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
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
		// TODO Auto-generated method stub
		convertView = mContext.getLayoutInflater().inflate(R.layout.no_data_view, null);
		return convertView;
	}
	
	
	

}
