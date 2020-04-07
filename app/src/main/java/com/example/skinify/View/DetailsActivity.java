package com.example.skinify.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.example.skinify.R;

public class DetailsActivity extends AppCompatActivity {

    TextView nameDetailTextView,descriptionDetailTextView,dateDetailTextView,categoryDetailTextView,collectionGun,game;
    ImageView teacherDetailImageView;

    private void initializeWidgets(){
        nameDetailTextView= findViewById(R.id.nameDetailTextView);
        descriptionDetailTextView= findViewById(R.id.descriptionDetailTextView);
        dateDetailTextView= findViewById(R.id.dateDetailTextView);
        collectionGun= findViewById(R.id.collectionEditText);
        categoryDetailTextView= findViewById(R.id.categoryDetailTextView);
        teacherDetailImageView=findViewById(R.id.teacherDetailImageView);
        game=findViewById(R.id.gameEditText);
    }
    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }
    private String getRandomCategory(){
        String[] categories={"9mm","Bareeta","G3"};
        Random random=new Random();
        int index=random.nextInt(categories.length-1);
        return categories[index];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initializeWidgets();

        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT
        Intent i=this.getIntent();
        String name=i.getExtras().getString("NAME_KEY");
        String description=i.getExtras().getString("DESCRIPTION_KEY");
        String imageURL=i.getExtras().getString("IMAGE_KEY");
        String collection=i.getExtras().getString("COLLECTION_KEY");
        String gamea=i.getExtras().getString("game");


        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        nameDetailTextView.setText(name);
        collectionGun.setText(collection);
        Log.d("collection","is"+collection);
        game.setText(gamea);
        descriptionDetailTextView.setText(description);
        dateDetailTextView.setText("DATE: "+getDateToday());
        categoryDetailTextView.setText("CATEGORY: "+getRandomCategory());
       /* Picasso.with(this)
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(teacherDetailImageView);*/
        Glide.with(this).load(imageURL).placeholder(R.drawable.skiniadmin).into(teacherDetailImageView);
        //Log.d("image","is "+imageURL);
        // GlideUtil.loadProfileIcon(url, mIconView);

    }

}
