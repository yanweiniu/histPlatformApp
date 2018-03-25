package com.marchsoft.organization.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.R;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.Dynamic;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/2/25 0025.
 */
public class DynamicListAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Dynamic> mList;
    private ImageLoader mImageLoader;
    private ImageLoadingListener mImageLoadingListener;
    private int mPageIndex = 1;
    private int mPageSize = Constant.PAGE_SIZE;
    private int id;
    public DynamicListAdapter(Context context, List<Dynamic> List){
        this.mList = List;
        this.mContext = context;
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
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.listitem_dyamic,null);
            holder.circleImageView = (CircleImageView) convertView.findViewById(R.id.tv_pic);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.joinUs = (TextView) convertView.findViewById(R.id.tv_join_activity);
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.place = (TextView) convertView.findViewById(R.id.tv_place);
            holder.number = (TextView) convertView.findViewById(R.id.tv_number);
            holder.supervisor = (TextView) convertView.findViewById(R.id.tv_supervisor);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Dynamic dynamic = mList.get(position);
        id = dynamic.getId();
        mImageLoader.displayImage(dynamic.getIcon(), holder.circleImageView);
        holder.title.setText(dynamic.getName());
        holder.date.setText(dynamic.getStartTime());
        holder.place.setText(dynamic.getPlace());
        holder.number.setText(String.valueOf(dynamic.getRegistering()));
        holder.supervisor.setText(String.valueOf(dynamic.getSupervisorId()));
        if(dynamic.getIsJoin() == 0){
            holder.joinUs.setText("取消报名");
            holder.joinUs.setTextColor(mContext.getResources().getColor(R.color.main_text_disable_textcolor));
            holder.joinUs.setEnabled(false);

        }else{
            holder.joinUs.setText("参与报名");
            holder.joinUs.setTextColor(mContext.getResources().getColor(R.color.main_listitem_textcolor));
            holder.joinUs.setEnabled(true);
        }
        holder.joinUs.setClickable(true);
        holder.joinUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int activityId = mList.get(position).getId();
                int userId = 1;
                int associationId = mList.get(position).getAssociationId();
                RequestParams params = new RequestParams();
                params.put("activity_id", activityId);
                params.put("user_id", userId);
                params.put("association_id", associationId);
                RestClient.get(Constant.API_GET_MAIN_DYNAMIC_JOINUS, params, new AsyncHttpResponseHandler(mContext, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                                JSONObject jsonObject = response.getJSONObject("data");
                                int status = jsonObject.optInt("status");
                                if (status == 0) {
                                    System.out.println("==========取消报名");
                                    holder.joinUs.setText("取消报名");
                                    holder.joinUs.setTextColor(mContext.getResources().getColor(R.color.main_text_disable_textcolor));
                                    holder.joinUs.setEnabled(false);
                                } else {
                                    /*joinActivity.setText("参与报名");
                                    joinActivity.setTextColor(mContext.getResources().getColor(R.color.main_listitem_textcolor));*/
                                    //joinActivity.setEnabled(true);
                                }

                            } else {
                                String msg = response.getString("msg");
                                ToastUtil.make(mContext).show(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }));

            }
        });
        return convertView;
    }


    private static class ViewHolder{
        private CircleImageView circleImageView;
        private TextView title;
        private TextView joinUs;
        private TextView date;
        private TextView place;
        private TextView number;
        private TextView supervisor;
    }

    public void setDataSource(List<Dynamic> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public List<Dynamic> getDataSource() {
        return mList;
    }

    public void appendDataSource(List<Dynamic> datasource) {
        mList.addAll(datasource);
        notifyDataSetChanged();
    }

    public int getmPageIndex() {
        return mPageIndex;
    }

    public void setmPageIndex(int mPageIndex) {
        this.mPageIndex = mPageIndex;
    }

    public int getmPageSize() {
        return mPageSize;
    }

    public void setmPageSize(int mPageSize) {
        this.mPageSize = mPageSize;
    }

    public synchronized int increasePageIndex() {
        return ++mPageIndex;
    }

    public Dynamic getItemDataSource(int position) {
        return mList.get(position);
    }
}
