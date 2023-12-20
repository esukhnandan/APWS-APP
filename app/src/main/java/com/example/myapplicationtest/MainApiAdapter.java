package com.example.myapplicationtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.net.Uri;
import android.graphics.Bitmap;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainApiAdapter extends FirebaseRecyclerAdapter<MainApiPlants,MainApiAdapter.myViewHolder> {

    public static String newselectedComName;
    public static String newselectedSciName;
    public static String newselectedSymbol;
    public static String newselectedWaterUsage;

    public MainApiAdapter(@NonNull FirebaseRecyclerOptions<MainApiPlants> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder2,final int position, @NonNull MainApiPlants model2){
        holder2.comname2.setText(model2.getComname());
        holder2.sciname2.setText(model2.getSciname());
        holder2.symbol2.setText(model2.getSymbol());
        holder2.waterusage2.setText(model2.getWaterusage());

        //Sets the click listener for btnSelect
        holder2.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newselectedComName = "";
                newselectedSciName = "";
                newselectedSymbol = "";
                newselectedWaterUsage = "";

                newselectedComName = model2.getComname();
                newselectedSciName = model2.getSciname();
                newselectedSymbol = model2.getSymbol();
                newselectedWaterUsage = model2.getWaterusage();

                Toast.makeText(v.getContext(), "Selection registered", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.api_main_item,parent,false);
        return new myViewHolder(view2);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView comname2,sciname2,symbol2,waterusage2;

        Button btnSelect;

        public myViewHolder(@NonNull View itemView2) {
            super(itemView2);
            comname2 = (TextView)itemView.findViewById(R.id.apicomnametext);
            sciname2 = (TextView)itemView.findViewById(R.id.apiscinametext);
            symbol2 = (TextView)itemView.findViewById(R.id.apisymboltext);
            waterusage2 = (TextView)itemView.findViewById(R.id.apiwaterusetext);

            btnSelect = (Button)itemView.findViewById(R.id.apibtnChoose);
        }
    }
}
