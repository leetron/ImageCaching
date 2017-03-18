package com.luclx.nab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.luclx.nab.R;
import com.luclx.nab.data.entities.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucLX on 3/18/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageViewHolder> {
    private Context mContext;
    private List<URL> urlList;

    public GalleryAdapter(Context mContext) {
        this.mContext = mContext;
        urlList = new ArrayList<>();
    }

    public void setData(List<URL> urlList) {
        this.urlList = urlList;
        Log.e("LUC", "Size " + urlList.size() + "");
        notifyDataSetChanged();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        Glide.with(mContext).load(urlList.get(position).getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

}
