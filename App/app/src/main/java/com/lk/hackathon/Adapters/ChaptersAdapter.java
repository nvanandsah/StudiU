package com.lk.hackathon.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lk.hackathon.Chapter;
import com.lk.hackathon.R;

import java.util.List;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.ChaptersAdapterViewHolder> {

    private List<Chapter> mData;
    private  Context mContext;
    private OnChapterClickListener clickListener;


    public ChaptersAdapter(Context context, List<Chapter>chapters,OnChapterClickListener listener) {
        mContext=context;
        mData=chapters;
        clickListener=listener;
    }

    public interface OnChapterClickListener{
        void OnChapterClick(String id);
    }

    public void swapData(List<Chapter> newData){
        mData=newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChaptersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.chapter_list_item,null);
        return new ChaptersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChaptersAdapterViewHolder holder, int position) {
        holder.chapterNameTextView.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(mData!=null)return mData.size();
        return 0;
    }

    public class ChaptersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView chapterNameTextView;

        public ChaptersAdapterViewHolder(View itemView) {
            super(itemView);
            chapterNameTextView=itemView.findViewById(R.id.chapter_name_text_view);
            chapterNameTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String id=mData.get(getAdapterPosition()).getId();
            clickListener.OnChapterClick(id);
        }
    }
}
