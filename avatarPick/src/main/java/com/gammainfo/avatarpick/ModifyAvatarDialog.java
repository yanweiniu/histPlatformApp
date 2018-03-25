package com.gammainfo.avatarpick;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ModifyAvatarDialog extends Dialog implements OnClickListener {

	private LayoutInflater factory;

	private Button mImg;

	private Button mPhone;

	private Button mCancel;

	public ModifyAvatarDialog(Context context) {
		super(context);
		factory = LayoutInflater.from(context);
	}

	public ModifyAvatarDialog(Context context, int theme) {
		super(context, theme);
		factory = LayoutInflater.from(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(factory.inflate(
				R.layout.gl_modify_avatar_choose_dialog, null));
		mImg = (Button) this.findViewById(R.id.gl_choose_img);
		mPhone = (Button) this.findViewById(R.id.gl_choose_phone);
		mCancel = (Button) this.findViewById(R.id.gl_choose_cancel);
		mImg.setOnClickListener(this);
		mPhone.setOnClickListener(this);
		mCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.gl_choose_img) {
			doGoToImg();
		} else if (id == R.id.gl_choose_phone) {
			doGoToPhone();
		} else if (id == R.id.gl_choose_cancel) {
			dismiss();
		}
	}

	public void doGoToImg() {
	}

	public void doGoToPhone() {
	}
}
