package com.luclx.nab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.luclx.nab.R;
import com.luclx.nab.data.entities.URL;
import com.luclx.nab.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by LucLX on 3/18/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageViewHolder> {
    private Context mContext;
    private List<URL> urlList;
    private PublishSubject<Pair<Integer, URL>> onClickedItem;
    private ImageLoader mImageLoader;

    public GalleryAdapter(Context mContext, ImageLoader loader) {
        this.mContext = mContext;
        this.mImageLoader = loader;
        onClickedItem = PublishSubject.create();
        urlList = new ArrayList<>();
    }

    public void setData(List<URL> urlList) {
        this.urlList = urlList;
        notifyDataSetChanged();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        mImageLoader.loadImage(urlList.get(position).getUrl(), holder.imageView);
        holder.progressBar.setProgress(70);
        holder.imageView.setOnClickListener(view ->
                onClickedItem.onNext(Pair.create(position, getItem(position))));
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    @Override
    public void onViewRecycled(ImageViewHolder holder) {
        super.onViewRecycled(holder);
        //
    }

    private URL getItem(int position) {
        return urlList.get(position);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ProgressBar progressBar;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public Observable<Pair<Integer, URL>> onItemClickListener() {
        return onClickedItem.hide();
    }

}
