package com.example.skinify.View;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.skinify.Adapter.RecyclerAdapter;
import com.example.skinify.Model.Teacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import com.example.skinify.R;


public class ItemsActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Teacher> mTeachers;
    private String asd;

    private void openDetailActivity(String[] data){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("NAME_KEY",data[0]);
        intent.putExtra("DESCRIPTION_KEY",data[1]);
        intent.putExtra("IMAGE_KEY",data[2]);
        intent.putExtra("COLLECTION_KEY",data[3]);
        intent.putExtra("game",data[4]);
        intent.putExtra("point",data[5]);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_items );

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager (this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTeachers = new ArrayList<> ();
        mAdapter = new RecyclerAdapter(ItemsActivity.this, mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ItemsActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Skins").child("AvailableItems");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mTeachers.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Teacher upload = teacherSnapshot.getValue(Teacher.class);
                    assert upload != null;
                    upload.setKey(teacherSnapshot.getKey());
                    mTeachers.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ItemsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
    public void onItemClick(int position) {
        Teacher clickedTeacher=mTeachers.get(position);
        String[] teacherData={clickedTeacher.getitemName(),clickedTeacher.getgame(),clickedTeacher.getitemImage(),clickedTeacher.getCollection(),clickedTeacher.getgrade(), String.valueOf(clickedTeacher.getrequiredPoints())};
        openDetailActivity(teacherData);
    }

    @Override
    public void onShowItemClick(int position) {
        Teacher tech=mTeachers.get(position);
        CallUpdateAndDeleteDialog(tech.getKey(),tech.getitemName(),tech.getitemImage(),tech.getCollection(),tech.getgame(),tech.getgrade(),tech.getrequiredPoints());
        Log.d("check","name is "+tech.getitemImage());
        //  Teacher clickedTeacher=mTeachers.get(position);
        //String[] teacherData={clickedTeacher.getName(),clickedTeacher.getDescription(),clickedTeacher.getImageUrl()};
        //openDetailActivity(teacherData);
    }

    @Override
    public void onDeleteItemClick(int position) {
        Teacher selectedItem = mTeachers.get(position);
        final String selectedKey = selectedItem.getKey();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Skins").child("AvailableItems").child(selectedKey);
        databaseReference.removeValue();
        //  StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(selectedItem.getImageUrl());
       /* imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void> () {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ItemsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });*/

 /* imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
            mDatabaseRef.child(selectedKey).removeValue();
               Toast.makeText(ItemsActivity.this, "Deleted Success", Toast.LENGTH_SHORT).show();

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(ItemsActivity.this, "Failsed", Toast.LENGTH_SHORT).show();
           }
       });
*/


    }
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    private void CallUpdateAndDeleteDialog(final String key, String username, String email, String monumber, String img,String game,Long pnt) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
//Access Dialog views
        final EditText updateTextname = (EditText) dialogView.findViewById(R.id.updateTextname);
        final EditText imgurl = (EditText) dialogView.findViewById(R.id.imgurl);
        final EditText updateTextemail = (EditText) dialogView.findViewById(R.id.updateTextemail);
        final EditText updateTextmobileno = (EditText) dialogView.findViewById(R.id.updateTextmobileno);
        final EditText updategame = (EditText) dialogView.findViewById(R.id.game);
        final EditText updatePoints = (EditText) dialogView.findViewById(R.id.requiredPoints);
        updateTextname.setText(username);
        updateTextemail.setText(email);
        updateTextmobileno.setText(monumber);
        imgurl.setText(img);
        updategame.setText(game);
       updatePoints.setText(String.valueOf(pnt));
        Log.d("image","is "+img);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);
//username for set dialog title
        dialogBuilder.setTitle("Update Data");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
 //asd=updatePoints.getText().toString();
// Click  for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("pointxx","is "+updatePoints);
                String name = updateTextname.getText().toString().trim();
                String des = updateTextemail.getText().toString().trim();
                String img = imgurl.getText().toString().trim();
                String mobilenumber = updateTextmobileno.getText().toString().trim();
                String game=updategame.getText().toString();
                Long i=Long.parseLong(updatePoints.getText().toString());
//checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(des)) {
                        if (!TextUtils.isEmpty(mobilenumber)) {
//Method for update data
                            updateUser(key,name, des, mobilenumber, img,game,i);
                            Log.d("updata", "is " + des);
                            b.dismiss();
                        }

                    }
                }
            }
        });

    }
    private boolean updateUser(String key,String name, String img,String collec,String des,String grd,Long pnt) {
//getting the specified User reference
        String id = key;
        assert id != null;

        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("Skins").child("AvailableItems").child(id);

        Log.d("id","is "+id);
        Teacher User = new Teacher(name, img,collec,des,grd,pnt);
//update User to firebase
//mDatabaseRef.child(id).setValue(User);
        UpdateReference.setValue(User);
        // Log.d("user ","is "+User);
        Toast.makeText(getApplicationContext(), "Gun Updated", Toast.LENGTH_LONG).show();

        return true;
    }
}


