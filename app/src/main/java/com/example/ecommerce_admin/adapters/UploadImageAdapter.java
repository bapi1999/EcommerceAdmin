package com.example.ecommerce_admin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.activities.AddProductActivity;
import com.example.ecommerce_admin.models.UploadImageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {

    private List<UploadImageModel> list;
    private  FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageReference1;

    public UploadImageAdapter(List<UploadImageModel> list) {
        this.list = list;
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.le_uploaded_image_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        String images = list.get(position).getImages();
        String imageName = list.get(position).getImageName();
        holder.setImages(images,imageName,position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.uploaded_image);
        }
        private void setImages(String images,String imageName, final int position){
            Glide.with(itemView.getContext()).load(images).apply(new RequestOptions().placeholder(R.drawable.as_square_placeholder)).into(imageView);
            //Glide.with(imageView.getContext()).load(R.drawable.as_square_placeholder).into(imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position< AddProductActivity.Already_added_Uri_List.size()){


                        Toast.makeText(itemView.getContext(), "Number "+position+" is uploaded", Toast.LENGTH_SHORT).show();
//                        UploadImageActivity.Already_added_name_List.remove(position);

                        AddProductActivity.imageModelList.remove(position);
                        //UploadImageActivity.Already_added_name_List.remove(position);
                        AddProductActivity.Already_added_Uri_List.remove(position);

                        AddProductActivity.totalpic = AddProductActivity.Already_added_Uri_List.size();

                        Map<String,Object> updateTotalpic = new HashMap<>();
                        updateTotalpic.put("no_of_img",AddProductActivity.Already_added_Uri_List.size());
                        FirebaseFirestore.getInstance().collection("USERS")
                                .document(user.getUid()).update(updateTotalpic).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){


                                    for (int i=0;i<AddProductActivity.Already_added_Uri_List.size();i++){
                                        Map<String,Object> updateimageUri = new HashMap<>();
                                        updateimageUri.put("product_img_"+ (i),AddProductActivity.Already_added_Uri_List.get((i)));
                                        //updateimageUri.put("uploadpicName"+(i) , UploadImageActivity.Already_added_name_List.get((i)));

//                                        FirebaseFirestore.getInstance().collection("PRODUCTS")
//                                                .document("productName").update(updateimageUri).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//                                                    //UploadImageActivity.Already_added_name_List.remove(position);
//                                                    Toast.makeText(itemView.getContext(), "succesfull", Toast.LENGTH_SHORT).show();
//                                                    AddProductActivity.imageAdapter.notifyDataSetChanged();
//
//
//                                                }else {
//                                                    String error = task.getException().getMessage();
//                                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
                                    }



                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });





                    }else {
                        Toast.makeText(itemView.getContext(), "Number "+position+" offline", Toast.LENGTH_SHORT).show();
                        AddProductActivity.uriList.remove(position);
                        AddProductActivity.nameList.remove(position);
                        notifyDataSetChanged();
//                        Glide.with(itemView.getContext()).load("").into(imageView);

                    }

                }
            });
        }
    }
}
