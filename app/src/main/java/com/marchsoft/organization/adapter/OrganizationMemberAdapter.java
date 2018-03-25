package com.marchsoft.organization.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.marchsoft.organization.R;
import com.marchsoft.organization.model.Organization;
import com.marchsoft.organization.model.OrganizationMember;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.reflect.Member;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wm on 16-2-27.
 */
public class OrganizationMemberAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<OrganizationMember> mOrganizationMemberList;
    private ImageLoader mImageLoader;
    private ImageLoadingListener mImageLoadingListener;
    private int mPageIndex = 1;
    private int mPageSize = Constant.PAGE_SIZE;

    public OrganizationMemberAdapter(Context context, List<OrganizationMember> mOrganizationMemberList){
        this.mContext = context;
        this.mOrganizationMemberList = mOrganizationMemberList;
        mLayoutInflater = LayoutInflater.from(context);
        mImageLoader = ImageLoader.getInstance();
        mImageLoadingListener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        };
    }
    @Override
    public int getCount() {
        return mOrganizationMemberList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrganizationMemberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.listitem_vice_president, null);
            viewHolder.icon = (CircleImageView)convertView.findViewById(R.id.org_vice_president_logo);
            viewHolder.nickname = (TextView)convertView.findViewById(R.id.organization_member_nickname);
            viewHolder.title = (TextView)convertView.findViewById(R.id.organization_member_title);
            viewHolder.phone = (TextView)convertView.findViewById(R.id.organization_member_phone);
            viewHolder.sex = (ImageView)convertView.findViewById(R.id.org_vice_president_sex);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        OrganizationMember organizationMember = mOrganizationMemberList.get(position);
        mImageLoader.displayImage(organizationMember.getIconAddress(), viewHolder.icon);
        viewHolder.nickname.setText(getData(organizationMember.getNickname()));
        viewHolder.phone.setText(getData(organizationMember.getPhone()));
        viewHolder.title.setText(getPosition(organizationMember.getPositionId()));
        viewHolder.sex.setImageResource(getSex(organizationMember.getSex()));
        return convertView;
    }
    class ViewHolder{
        private CircleImageView icon;
        private TextView nickname;
        private TextView title;
        private TextView phone;
        private ImageView sex;
    }


    private int getSex(int sex) {
        if (sex == 0){
            return R.mipmap.ic_mine_sex_man;
        }else {
            return R.mipmap.ic_mine_sex_womman;
        }
    }



    private String getPosition(int positionId){
        String positionStr = "";
        switch (positionId){
            case 1:
                positionStr = "会长";
                break;
            case 2:
                positionStr = "副会长";
                break;
            case 3:
                positionStr = "干事";
                break;
            case 8:
                positionStr = "会员";
                break;
            case 9:
                positionStr = "学生";
                break;
            default:
                break;
        }
        return positionStr;
    }

    private String getData(String data){
        if (data == null || data.equals("null") || data == "0" || data.equals("0")){
            return "未知";
        }else {
            return data;
        }
    }


    public void setDataSource(List<OrganizationMember> mList) {
        this.mOrganizationMemberList = mList;
        notifyDataSetChanged();
    }

    public List<OrganizationMember> getDataSource() {
        return mOrganizationMemberList;
    }

    public void appendDataSource(List<OrganizationMember> datasource) {
        mOrganizationMemberList.addAll(datasource);
        notifyDataSetChanged();
    }



    public OrganizationMember getItemDataSource(int position) {
        return mOrganizationMemberList.get(position);
    }
}
