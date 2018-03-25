/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.marchsoft.organization.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.marchsoft.organization.DynamicDetailsActivity;
import com.marchsoft.organization.NewsDetailsActivity;
import com.marchsoft.organization.R;
import com.marchsoft.organization.model.Banner;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.viewpager.RecyclingPagerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * ImagePagerAdapter
 */
public class BannerPagerAdapter extends RecyclingPagerAdapter implements View.OnClickListener {

    private Context mContext;
    private List<Banner> mList;

    private boolean isInfiniteLoop;
    private LayoutInflater mLayoutInflater;
    private ImageLoader mImageLoader;
    private ImageLoadingListener mImageLoadingListener;
    private OnItemClickListener mOnItemClickListener;

    public BannerPagerAdapter(Context context, List<Banner> list) {
        this.mContext = context;
        mList = list;
        isInfiniteLoop = true;
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
        // Infinite loop
        return mList.size() > 0 ? (isInfiniteLoop ? Integer.MAX_VALUE : mList.size()) : 0;
    }


    private int getPosition(int position) {
        return isInfiniteLoop && mList.size() > 0 ? position % mList.size() : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = (ImageView) mLayoutInflater.inflate(
                    R.layout.listitem_banner, null);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Banner banner = mList.get(getPosition(position));
        mImageLoader.displayImage(banner.getPhoto(), holder.imageView,
                mImageLoadingListener);
        holder.imageView.setId(position);
        holder.imageView.setClickable(true);
        holder.imageView.setOnClickListener(this);
        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }


    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }


    public BannerPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    @Override
    public void onClick(View v) {
        int position = getPosition(v.getId());
        if (mOnItemClickListener == null) {

            try {
                Banner banner = mList.get(position);
                //TODO
                Intent intent = new Intent(mContext, NewsDetailsActivity.class);
                intent.putExtra("id",banner.getId());
                mContext.startActivity(intent);


            } catch (NumberFormatException e) {
                if (Constant.DEBUG) {
                    e.printStackTrace();
                }
            }
        } else {
            mOnItemClickListener.onItemClick(v, position);
        }
    }

    public List<Banner> getDatasource() {
        return mList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDatasource(List<Banner> datasource) {
        mList = datasource;
        notifyDataSetChanged();
    }

    public void appendDataSource(List<Banner> datasource) {
        mList.addAll(datasource);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
