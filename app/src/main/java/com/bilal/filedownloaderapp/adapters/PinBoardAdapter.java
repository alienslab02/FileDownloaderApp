package com.bilal.filedownloaderapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilal.filedownloader.ImageDownloader;
import com.bilal.filedownloaderapp.R;
import com.bilal.filedownloaderapp.models.PinBoard;

import java.util.ArrayList;

/**
 * Created by applepc on 06/08/2017.
 */

public class PinBoardAdapter extends RecyclerView.Adapter<PinBoardAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            this.mTextView = v.findViewById(R.id.categories);
            this.mImageView = v.findViewById(R.id.boardImage);
        }
    }

    private ArrayList<PinBoard> boards;
    private ImageDownloader imageLoader;
    private OnItemClickListener onItemClickListener;

    public PinBoardAdapter(Context context, ArrayList<PinBoard> boards) {
        this.boards = boards;
        // init and do some settings for library
        imageLoader = new ImageDownloader(context);
        imageLoader.setRequiredSize(300);
        imageLoader.setPlaceHolderResId(R.mipmap.ic_launcher_round);
        imageLoader.setMemoryLimit(24*1024*1024); // 24MB
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pinboard, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PinBoard board = boards.get(position);
        holder.mTextView.setText("Likes: " + board.getLikes());
        imageLoader.loadImage(board.getImage().getSmall(), holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCallBack(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    public void onDestroy() {
        imageLoader.clearCache();
    }

    private void sendCallBack(View view, int pos){
        if(this.onItemClickListener != null){
            this.onItemClickListener.onItemClick(view, pos);
        }
    }

    public void cancelLoading(PinBoard board) {
        imageLoader.cancel(board.getImage().getSmall());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
