

package com.marchsoft.organization.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marchsoft.organization.R;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.model.PCD;
import com.marchsoft.organization.wheel.model.CityModel;
import com.marchsoft.organization.wheel.model.DistrictModel;
import com.marchsoft.organization.wheel.model.ProvinceModel;
import com.marchsoft.organization.wheel.service.XmlParserHandler;
import com.marchsoft.organization.wheel.widget.OnWheelChangedListener;
import com.marchsoft.organization.wheel.widget.WheelView;
import com.marchsoft.organization.wheel.widget.adapters.ArrayWheelAdapter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class WheelAlertDialog extends Dialog implements View.OnClickListener,OnWheelChangedListener {
    private LinearLayout lLayout;
    private Display display;
    private OnAlertDialogListener mButtonselcetor;
    private Context mContext;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView title;
    private Button sureButton;
    private Button cancelButton;
    private String mMessage;
    private String mOkButtonMessage;
    private String mCancelButtonMessage;

    public WheelAlertDialog(Context context) {
        super(context);
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public WheelAlertDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public void setOnButtonListener(OnAlertDialogListener buttonListener) {
        mButtonselcetor = buttonListener;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            provinceIndex = mViewProvince.getCurrentItem();
            updateCities();
            mCurrentDistrictName =  mDistrictDatasMap.get(mCurrentCityName)[0];
        } else if (wheel == mViewCity) {
            cityIndex = mViewCity.getCurrentItem();
            updateAreas();
            mCurrentDistrictName =  mDistrictDatasMap.get(mCurrentCityName)[0];
        } else if (wheel == mViewDistrict) {
            districtIndex = mViewDistrict.getCurrentItem();
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }

    }


    public static interface OnAlertDialogListener {
        void onOk(WheelAlertDialog alertDialogView, String content, PCD pcd);

        void onCancel(WheelAlertDialog alertDialogView, String content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wheel_alertdialog);
        lLayout = (LinearLayout) findViewById(R.id.lLayout_bg);
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        title = (TextView) findViewById(R.id.txt_title);
        sureButton = (Button) findViewById(R.id.btn_sure);
        cancelButton = (Button) findViewById(R.id.btn_cancle);
        title.setText(mMessage);
        sureButton.setText(mOkButtonMessage);
        cancelButton.setText(mCancelButtonMessage);
        sureButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);
        if (TextUtils.isEmpty(mOkButtonMessage)) {
            sureButton.setVisibility(View.GONE);
        } else {
            sureButton.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(mCancelButtonMessage)) {
            cancelButton.setVisibility(View.GONE);
        } else {
            cancelButton.setVisibility(View.VISIBLE);
        }
        setUpData();
        lLayout.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));

    }

    @Override
    public void onClick(View v) {
        String reslut = result();
        if (v.getId() == R.id.btn_sure) {
            PCD pcd = new PCD();
            pcd.setProvinceIndex(provinceIndex);
            pcd.setCityIndex(cityIndex);
            pcd.setDistrictIndex(districtIndex);
            pcd.setmCurrentProviceName(mCurrentProviceName);
            pcd.setmCurrentCityName(mCurrentCityName);
            pcd.setmCurrentDistrictName(mCurrentDistrictName);
            mButtonselcetor.onOk(this,reslut,pcd);
        } else {
            mButtonselcetor.onCancel(this,reslut);
        }
    }

    public static WheelAlertDialog build(Context context, String message,
                                         String sureButtonMessage, String cancelButtonMessage,
                                         OnAlertDialogListener onButtonSelectorListener) {
        WheelAlertDialog alertDialogView = new WheelAlertDialog(context,
                R.style.AlertDialog);
        alertDialogView.mMessage = message;
        alertDialogView.mOkButtonMessage = sureButtonMessage;
        alertDialogView.mCancelButtonMessage = cancelButtonMessage;
        alertDialogView.setOnButtonListener(onButtonSelectorListener);
        return alertDialogView;
    }


    public static WheelAlertDialog build(Context context, int resMsgId, int resOkId,
                                         int resCancelId, OnAlertDialogListener onAlertDialogListener) {
        return build(context, context.getString(resMsgId),
                resOkId != 0 ? context.getString(resOkId) : null,
                resCancelId != 0 ? context.getString(resCancelId) : null,
                onAlertDialogListener);
    }

    public String result(){
        return mCurrentProviceName+mCurrentCityName+mCurrentDistrictName;
    }



    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName ="";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode ="";
    //用来存储上次省市区的index,可以初始化弹出时弹出框的默认选项
    private int provinceIndex ;
    private int cityIndex ;
    private int districtIndex ;

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas()
    {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getContext().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k=0; k<districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
        PCD pcd = Preferences.getPCD();
        mViewProvince.setCurrentItem(pcd.getProvinceIndex());
        mViewCity.setCurrentItem(pcd.getCityIndex());
        mViewDistrict.setCurrentItem(pcd.getDistrictIndex());

    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }




}

