package com.marchsoft.organization.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marchsoft.organization.R;
import com.marchsoft.organization.model.Dynamic;
import com.marchsoft.organization.model.News;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/2/25 0025.
 */
public class NewAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<News> mList;
    private ImageLoader mImageLoader;
    private ImageLoadingListener mImageLoadingListener;
    private int mPageIndex = 1;
    private int mPageSize = Constant.PAGE_SIZE;
    public NewAdapter(Context context, List<News> mList){
        this.mList = mList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.listitem_news,null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.tv_pic);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        News news = mList.get(position);
        mImageLoader.displayImage(news.getPhoto(),holder.imageView);
        holder.title.setText(news.getTitle());
        holder.content.setText(Utils.stripHtml(Html.fromHtml(news.getContent()).toString()));
        holder.nickname.setText(news.getStudentName());
        holder.date.setText(news.getPublishDate());
        return convertView;
    }

    private static class ViewHolder{
        private ImageView imageView;
        private TextView title;
        private TextView content;
        private TextView nickname;
        private TextView date;
    }

    public void setDataSource(List<News> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public List<News> getDataSource() {
        return mList;
    }

    public void appendDataSource(List<News> datasource) {
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

    public News getItemDataSource(int position) {
        return mList.get(position);
    }
}
