package com.gammainfo.avatarpick;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DemoActivity extends Activity {
	private Context mCon;
	private String TAG = "InformationActivity";
	private RelativeLayout layout;
	private ImageView head;
	public static final String IMAGE_PATH = "My_weixin";
	private static String localTempImageFileName = "";
	private static final int FLAG_CHOOSE_IMG = 5;
	private static final int FLAG_CHOOSE_PHONE = 6;
	private static final int FLAG_MODIFY_FINISH = 7;
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
			"images/screenshots");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_setting_information);
		mCon = DemoActivity.this;
		head = (ImageView) findViewById(R.id.main_tab_setting_information_head);
		// 跳出选择界面
		layout = (RelativeLayout) findViewById(R.id.main_tab_setting_information_rl);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 调用选择那种方式的dialog
				ModifyAvatarDialog modifyAvatarDialog = new ModifyAvatarDialog(
						mCon) {
					// 选择本地相册
					@Override
					public void doGoToImg() {
						this.dismiss();
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_PICK);
						intent.setType("image/*");
						startActivityForResult(intent, FLAG_CHOOSE_IMG);
					}

					// 选择相机拍照
					@Override
					public void doGoToPhone() {
						this.dismiss();
						String status = Environment.getExternalStorageState();
						if (status.equals(Environment.MEDIA_MOUNTED)) {
							try {
								localTempImageFileName = "";
								localTempImageFileName = String
										.valueOf((new Date()).getTime())
										+ ".png";
								File filePath = FILE_PIC_SCREENSHOT;
								if (!filePath.exists()) {
									filePath.mkdirs();
								}
								Intent intent = new Intent(
										android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
								File f = new File(filePath,
										localTempImageFileName);
								// localTempImgDir和localTempImageFileName是自己定义的名字
								Uri u = Uri.fromFile(f);
								intent.putExtra(
										MediaStore.Images.Media.ORIENTATION, 0);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
								startActivityForResult(intent,
										FLAG_CHOOSE_PHONE);
							} catch (ActivityNotFoundException e) {
								//
							}
						}
					}
				};
				AlignmentSpan span = new AlignmentSpan.Standard(
						Layout.Alignment.ALIGN_CENTER);
				AbsoluteSizeSpan span_size = new AbsoluteSizeSpan(25, true);
				SpannableStringBuilder spannable = new SpannableStringBuilder();
				String dTitle = getString(R.string.gl_choose_title);
				spannable.append(dTitle);
				spannable.setSpan(span, 0, dTitle.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannable.setSpan(span_size, 0, dTitle.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				modifyAvatarDialog.setTitle(spannable);
				modifyAvatarDialog.show();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(mCon, "图片没找到", 0).show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();
					Log.i(TAG, "path=" + path);
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {
					Log.i(TAG, "path=" + uri.getPath());
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}
		} else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
			File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
			Intent intent = new Intent(this, CropImageActivity.class);
			intent.putExtra("path", f.getAbsolutePath());
			startActivityForResult(intent, FLAG_MODIFY_FINISH);
		} else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				final String path = data.getStringExtra("path");
				Log.i(TAG, "截取到的图片路径是 = " + path);
				Bitmap b = BitmapFactory.decodeFile(path);
				head.setImageBitmap(b);

			}
		}
	}

}
