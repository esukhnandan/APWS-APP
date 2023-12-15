package com.example.myapplicationtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

public class MainAdapter extends FirebaseRecyclerAdapter<MainUserPlants,MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainUserPlants> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,final int position, @NonNull MainUserPlants model){
        holder.name.setText(model.getName());
        holder.comname.setText(model.getComname());
        holder.sciname.setText(model.getSciname());
        holder.symbol.setText(model.getSymbol());
        holder.waterusage.setText(model.getWaterusage());

        Glide.with(holder.img.getContext())
                .load(model.getSurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.btnEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,800)
                        .create();

                View view = dialogPlus.getHolderView();
                EditText name = view.findViewById(R.id.txtName);
                EditText surl = view.findViewById(R.id.txtSurl);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                name.setText(model.getName());
                surl.setText(model.getSurl());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //Map<String,Object> map = new HashMap<>();
                        //map.put("name",name.getText().toString());
                        //map.put("surl",surl.getText().toString());
                        // Update the model with the new values
                        model.setName(name.getText().toString());
                        model.setSurl(surl.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("UsersPlants")
                                //.child(getRef(position).getKey()).updateChildren(map)
                                .child(getRef(position).getKey()).setValue(model)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.name.getContext(),"Error Updating",Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be undone.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the reference to the Firebase item you want to delete
                        String itemId = getRef(position).getKey();
                        if (itemId != null) {
                            FirebaseDatabase.getInstance().getReference().child("UsersPlants").child(itemId)
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.name.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.name.getContext(), "Error Deleting", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.name.getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
            /* old code
           @Override
           public void onClick(View v){
               AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
               builder.setTitle("Are you Sure?");
               builder.setMessage("Deleted data can't be undone.");

               builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("students")
                                .child(getRef(position).getKey()).removeValue();
                   }
               });

               builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Toast.makeText(holder.name.getContext(),"Canceled",Toast.LENGTH_SHORT).show();
                   }
               });
               builder.show();

           }
           */
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView name,comname,sciname,symbol,waterusage;

        Button btnEdit,btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView)itemView.findViewById(R.id.img1);
            name = (TextView)itemView.findViewById(R.id.nametext);
            comname = (TextView)itemView.findViewById(R.id.comnametext);
            sciname = (TextView)itemView.findViewById(R.id.scinametext);
            symbol = (TextView)itemView.findViewById(R.id.symboltext);
            waterusage = (TextView)itemView.findViewById(R.id.waterusetext);

            btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
            btnDelete =(Button)itemView.findViewById(R.id.btnDelete);

            //Click listener for the image NEW 3
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagePopup(img.getContext(), img);
                }
            });
        }

        //NEW 3
        /*
        private void showImagePopup(final Context context, CircleImageView imageView) {
            // Get the Drawable from the ImageView
            Drawable drawable = imageView.getDrawable();

            // Check if the drawable is not null
            if (drawable != null) {
                // Create a Dialog for the image viewer
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true); // Dismiss the dialog when touched outside
                dialog.setContentView(R.layout.popup_image_viewer);

                // Set the background of the dialog to transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Load the drawable into the ImageView in the popup
                ImageView popupImageView = dialog.findViewById(R.id.popupImageView);

                // Set the desired size (you can adjust these values)
                int desiredWidth = 50; // in pixels
                int desiredHeight = 50; // in pixels

                // Resize the drawable
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true);

                // Set the resized bitmap to the ImageView
                popupImageView.setImageBitmap(resizedBitmap);

                // Show the dialog
                dialog.show();
            } else {
                // Handle the case where the drawable is null
                Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show();
            }
        }
        */

        private void showImagePopup(final Context context, CircleImageView imageView) {
            // Get the Drawable from the ImageView
            Drawable drawable = imageView.getDrawable();

            // Check if the drawable is not null
            if (drawable != null) {
                // Create a Dialog for the image viewer
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true); // Dismiss the dialog when touched outside
                dialog.setContentView(R.layout.popup_image_viewer);

                // Set the background of the dialog to transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Load the drawable into the ImageView in the popup
                ImageView popupImageView = dialog.findViewById(R.id.popupImageView);
                popupImageView.setImageDrawable(drawable);

                // Show the dialog
                dialog.show();
            } else {
                // Handle the case where the drawable is null
                Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
