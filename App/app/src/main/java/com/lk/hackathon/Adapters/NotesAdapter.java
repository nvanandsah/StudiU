package com.lk.hackathon.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lk.hackathon.Notes;
import com.lk.hackathon.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesAdapterViewHolder> {

    public Context mContext;
    List<Notes> mData;
    OnDownloadClickListener mClickListener;
    public NotesAdapter(Context context, List<Notes> data, OnDownloadClickListener clickListener) {
        mContext=context;
        mData=data;
        mClickListener=clickListener;
    }

    public interface OnDownloadClickListener{
        void OnDownloadClick(String url,String brandName);
    }

    @NonNull
    @Override
    public NotesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.notes_list_item,null);
        return new  NotesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapterViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getLogo()).into(holder.logoImageView);
        holder.brandTextView.setText(mData.get(position).getName());
        holder.nameTextView.setText(mData.get(position).getName()+" "+
                                    mData.get(position).getSubject()+" "+
                                    mData.get(position).getStudentClass());

        setStars(holder,position);

    }

    private void setStars(NotesAdapterViewHolder holder, int position) {
        int stars=mData.get(position).getStars();
        switch (stars){
            case 4:holder.star_5.setVisibility(View.GONE);
            break;
            case 3:{
                holder.star_4.setVisibility(View.GONE);
                holder.star_5.setVisibility(View.GONE);
            }
            break;
            case 2:{
                holder.star_4.setVisibility(View.GONE);
                holder.star_5.setVisibility(View.GONE);
                holder.star_3.setVisibility(View.GONE);
            }
            break;
            case 1:{
                holder.star_4.setVisibility(View.GONE);
                holder.star_5.setVisibility(View.GONE);
                holder.star_3.setVisibility(View.GONE);
                holder.star_2.setVisibility(View.GONE);
            }
            break;
        }

    }

    @Override
    public int getItemCount() {
        if(mData!=null)
            return mData.size();
        return 0;
    }

    public void swapData(List<Notes> newData){
        mData=newData;
        notifyDataSetChanged();
    }


    public class NotesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        TextView brandTextView;
        ImageView logoImageView;
        ImageView downLoadImageView;

        ImageView star_5;
        ImageView star_4;
        ImageView star_3;
        ImageView star_2;
        ImageView star_1;


        public NotesAdapterViewHolder(View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.notes_name);
            brandTextView=itemView.findViewById(R.id.brand_name);
            logoImageView=itemView.findViewById(R.id.brand_logo);
            downLoadImageView=itemView.findViewById(R.id.download_article_image_view);
            downLoadImageView.setOnClickListener(this);

            star_1=itemView.findViewById(R.id.star_1);
            star_1.setTag("star1");
            star_2=itemView.findViewById(R.id.star_2);
            star_3=itemView.findViewById(R.id.star_3);
            star_4=itemView.findViewById(R.id.star_4);
            star_5=itemView.findViewById(R.id.star_5);
        }

        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch (id){
                case R.id.download_article_image_view:{
                    String url=mData.get(getAdapterPosition()).getUrl();
                    String brandName=mData.get(getAdapterPosition()).getName();
                    mClickListener.OnDownloadClick(url,brandName);
                }
            }
        }
    }
}
