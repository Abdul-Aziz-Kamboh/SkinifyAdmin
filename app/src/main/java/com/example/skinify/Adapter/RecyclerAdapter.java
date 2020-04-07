package com.example.skinify.Adapter;


import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.module.AppGlideModule;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.skinify.Model.Teacher;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.skinify.R;


public  class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private Context mContext;
    private List<Teacher> teachers;
    private OnItemClickListener mListener;

    public RecyclerAdapter(Context context, List<Teacher> uploads) {
        mContext = context;
        teachers = uploads;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_model, parent, false);
        return new RecyclerViewHolder(v);
    }



    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final Teacher currentTeacher = teachers.get(position);
        holder.nameTextView.setText(currentTeacher.getitemName());
        holder.points.setText(currentTeacher.getCollection());
        holder.grade.setText(currentTeacher.getgrade());

        holder.requiredPoints.setText(String.valueOf(currentTeacher.getrequiredPoints()));
        holder.descriptionTextView.setText(currentTeacher.getgame());
        holder.dateTextView.setText(getDateToday());
Log.d("grade ","is "+currentTeacher.getgrade());
Log.d("points ","is "+currentTeacher.getrequiredPoints());
        // File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "skiniadmin.png");
        Glide.with(mContext).load(currentTeacher.getitemImage()).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e("IMAGE_EXCEPTION", "Exception " + e.toString());
                Log.d("url","is "+currentTeacher.getitemImage());
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                return false;
            }
        })
                .into(holder.teacherImageView);
        //Picasso.with(mContext).load("gs://skinify-admin.appspot.com/All_Image_Uploads").placeholder(R.drawable.placeholder).into(holder.teacherImageView);
        Log.d("url","is "+currentTeacher.getitemImage());
//Glide.with(holder.imageView.getContext())
        //      .load(new File(hotel.imageId2))
        //    .into(holder.imageView);

      /* Glide.with(mContext).load(currentTeacher.getImageUrl()).placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Drawable>() {
           @Override
           public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
               Log.e("IMAGE_EXCEPTION", "Exception " + e.toString());
               return false;
           }

           @Override
           public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
               return false;
           }
       })
               .into(holder.teacherImageView);*/

    }

//Glide.with(holder.teacherImageView.getContext()).load(new File(currentTeacher.getImageUrl())).placeholder(R.drawable.logo).into(holder.teacherImageView);


    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView nameTextView,descriptionTextView,dateTextView,points,grade,requiredPoints;
        public ImageView teacherImageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTextView =itemView.findViewById ( R.id.nameTextView );
            requiredPoints =itemView.findViewById ( R.id.gradeTextView);
            grade =itemView.findViewById ( R.id.gameTextView);
            points =itemView.findViewById ( R.id.points );
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            teacherImageView = itemView.findViewById(R.id.teacherImageView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Edit");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }

}

