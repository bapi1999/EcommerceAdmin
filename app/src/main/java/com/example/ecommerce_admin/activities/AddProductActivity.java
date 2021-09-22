package com.example.ecommerce_admin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommerce_admin.R;
import com.example.ecommerce_admin.adapters.UploadImageAdapter;
import com.example.ecommerce_admin.models.UploadImageModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    //1st layout
    private TextInputLayout bookName,publisherName,writerName,languagE,pageCount,isbnNo,dimension,description;
    //2n layout
    private TextInputLayout bookPrice,discountPrice;
    private LinearLayout bookConditionlayout,container2;
    private RadioGroup bookCondition;
    private RadioGroup radioGroup;
    private RadioButton radioButton1,radioButton2;
    private LinearLayout stockLayout;
    private EditText stockQuantity,printDate1,printDate2;
    private ImageButton stockUp,stockDown;
    //3rd layout
    private LinearLayout categoryContainer;
    private CheckBox checkAction,checkAdult,checkArt,checkBiography,checkBusiness,checkChild,checkComics,checkDiary,checkDictionary
            ,checkDrama,checkEducational,checkEncyclopedia,checkGk,checkHealth,checkHorror,checkJournal,checkJob,checkMagazine,checkNovels
            ,checkReference,checkReligion,checkRomance,checkScience,checkStories,checkSupernatural,checkThriller,checkTravel,checkOthers;
    private EditText bookTags;
    //4th layout
    private ImageView productThambnail;
    private Button cameraBtn,galleryBtn;
    private AppCompatButton selectBtn;
    private RecyclerView recyclerView;
    public static List<UploadImageModel> imageModelList = new ArrayList<>();
    public static UploadImageAdapter imageAdapter;



    //mainlayout
    private Button adddBtn;
    private LinearLayout  cancelBtn ;

    //others variable
    public static Uri thumbUri;
    public String thumbName;
    private Uri imageUri;
    private String pattern="[0-9]+";

    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference1;

    public static int totalpic;
    private boolean edit = false;
    private Dialog loadingDialog;
    private Dialog choseingDialog;
    public static List<Uri> uriList = new ArrayList<Uri>();
    public static List<String> nameList = new ArrayList<String>();
    public static final List<String> Already_added_Uri_List = new ArrayList<String >();
    private String listsize;
    private int totalProduct;
    private String databaseProductID;
    private boolean inStock;
    private List<String> categories;
    private List<String> categoryList = new ArrayList<>() ;
    private List<String> tagList ;
    public boolean printDateMandatory = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference1 = storage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        bookName =findViewById(R.id.bookName);
        publisherName=findViewById(R.id.publisherName);
        writerName=findViewById(R.id.writerName);
        languagE=findViewById(R.id.bookLanguage);
        pageCount=findViewById(R.id.pageCount);
        isbnNo=findViewById(R.id.isbnNumber);
        dimension=findViewById(R.id.bookDimension);
        description=findViewById(R.id.bookDescription);
        bookPrice  =findViewById(R.id.bookPrice);
        discountPrice = findViewById(R.id.discountPrice);

        bookConditionlayout = findViewById(R.id.linearLayout9);
        container2 = findViewById(R.id.linearLayout10);
        bookCondition = findViewById(R.id.book_condition_toggle);
//        radioButton1 = bookCondition.findViewById(bookCondition.getCheckedRadioButtonId());
        printDate1 = findViewById(R.id.editTextDate1);
        printDate2 = findViewById(R.id.editTextDate2);

        radioGroup = findViewById(R.id.in_stock_toggle);
//        radioButton2  = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
        stockLayout = findViewById(R.id.stockquantity_layout);
        stockQuantity = findViewById(R.id.stock_quantity);
        stockUp = findViewById(R.id.positive_Btn);
        stockDown = findViewById(R.id.negative_Btn);
        bookTags = findViewById(R.id.edit_tags);
        productThambnail = findViewById(R.id.product_thumbnail);
        selectBtn = findViewById(R.id.select_image_btn);

        ////////////////////////////
        categoryContainer = findViewById(R.id.linearLayout11);
        checkAction = findViewById(R.id.check_action);
        checkAdult = findViewById(R.id.check_adult);
        checkArt = findViewById(R.id.check_art);
        checkBiography = findViewById(R.id.check_biography);
        checkBusiness = findViewById(R.id.check_business);
        checkChild = findViewById(R.id.check_child);
        checkComics = findViewById(R.id.check_comics);
        checkDiary = findViewById(R.id.check_diary);
        checkDictionary = findViewById(R.id.check_dictionary);
        checkDrama = findViewById(R.id.check_drama);
        checkEducational = findViewById(R.id.check_educational);
        checkEncyclopedia = findViewById(R.id.check_encyclopedia);
        checkGk = findViewById(R.id.check_gk);
        checkHealth = findViewById(R.id.check_health);
        checkHorror = findViewById(R.id.check_horror);
        checkJournal = findViewById(R.id.check_journal);
        checkJob = findViewById(R.id.check_job);
        checkMagazine = findViewById(R.id.check_Magazine);
        checkNovels = findViewById(R.id.check_novels);
        checkReference = findViewById(R.id.check_reference);
        checkReligion = findViewById(R.id.check_religion);
        checkRomance = findViewById(R.id.check_Romance);
        checkScience = findViewById(R.id.check_science);
        checkStories = findViewById(R.id.check_Stories);
        checkSupernatural = findViewById(R.id.check_supernatural);
        checkThriller = findViewById(R.id.check_thriller);
        checkTravel = findViewById(R.id.check_travel);
        checkOthers = findViewById(R.id.check_Others);


/////////////////////////////////////



        adddBtn = findViewById(R.id.add_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
//        databaseProductID = UUID.randomUUID().toString().substring(0,5);
//        databaseProductID = "productTitle";
        databaseProductID = getSaltString();

        Already_added_Uri_List.clear();
        nameList.clear();
        uriList.clear();
        imageModelList.clear();


        //loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.le_loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.s_bigslider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog

        //Chose dialog
        choseingDialog = new Dialog(this);
        choseingDialog.setContentView(R.layout.le_camera_or_gallery_dialog);
        choseingDialog.setCancelable(true);
        choseingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.s_bigslider_background));
        choseingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cameraBtn = choseingDialog.findViewById(R.id.select_camera);
        galleryBtn = choseingDialog.findViewById(R.id.select_gallery);
        //Chose dialog



        recyclerView = findViewById(R.id.upload_image_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        listsize = getIntent().getStringExtra("listSize");
        totalProduct = Integer.parseInt(listsize);

        productThambnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddProductActivity.this)
                        .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                        .compress(100)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(512, 512)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(1);

            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseingDialog.show();
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseingDialog.dismiss();
                ImagePicker.with(AddProductActivity.this)
                        .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                        .compress(800)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(800, 800)
                        .cameraOnly() //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(3);

            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseingDialog.dismiss();
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), 2);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        bookCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radioButton1 =(RadioButton) bookCondition.findViewById(bookCondition.getCheckedRadioButtonId());

                switch (checkedId){
                    case R.id.radioButton:
                    case R.id.radioButton2:
                        printDateMandatory = true;
                        Toast.makeText(AddProductActivity.this, radioButton1.getText().toString(), Toast.LENGTH_SHORT).show();
                        printDate1.setVisibility(View.VISIBLE);
                        printDate2.setVisibility(View.GONE);

                        break;
                    case R.id.radioButton3:
                    case R.id.radioButton4:
                        printDateMandatory = false;
                        Toast.makeText(AddProductActivity.this, radioButton1.getText().toString(), Toast.LENGTH_SHORT).show();
                        printDate1.setVisibility(View.GONE);
                        printDate2.setVisibility(View.VISIBLE);

                        break;

                    default:
                        break;
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton2  = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                switch (checkedId){
                    case R.id.no_Button:
                        Toast.makeText(AddProductActivity.this, "no", Toast.LENGTH_SHORT).show();
                        inStock = false;
                        stockLayout.setVisibility(View.GONE);
                        break;
                    case R.id.yes_Button:
                        Toast.makeText(AddProductActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        inStock = true;
                        stockLayout.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }
        });

        stockUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(stockQuantity.getText().toString());
                String num = String.valueOf(quantity+1);
                stockQuantity.setText(num);
            }
        });
        stockDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(stockQuantity.getText().toString());
                if (quantity>1) {
                    String num = String.valueOf(quantity-1);
                    stockQuantity.setText(num);
                }else {
                    Toast.makeText(AddProductActivity.this, " can't be less than 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Addproduct();
//                Toast.makeText(AddProductActivity.this, "++++", Toast.LENGTH_SHORT).show();

                CheckAllDetails(adddBtn);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Already_added_Uri_List.clear();
                nameList.clear();
                uriList.clear();
                imageModelList.clear();
                finish();
            }
        });
    }
//TODO- Main  methods   #################################################################################

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        imageModelList.clear();
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    thumbUri = data.getData();
                    thumbName = GetFileName(thumbUri);
                    Glide.with(this).load(thumbUri).apply(new RequestOptions().placeholder(R.drawable.as_square_placeholder)).into(productThambnail);


                }
            }
        }else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    imageUri = data.getData();
                    String imagename = GetFileName(imageUri);
                    if (uriList.size() == 0){
                        uriList.add(imageUri);
                        nameList.add(imagename);
                    }else {
                        uriList.clear();
                        nameList.clear();
                        uriList.add(imageUri);
                        nameList.add(imagename);
                    }

                    UploadImageModel imageModel = new UploadImageModel(imageUri.toString(), imagename);
                    imageModelList.add(imageModel);
                    imageAdapter = new UploadImageAdapter(imageModelList);
                    recyclerView.setAdapter(imageAdapter);
                }
            }

        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                if (data.getClipData() != null) {
                    int totalImage = data.getClipData().getItemCount();

                    for (int i = 0; i < totalImage; i++) {
                        imageUri = data.getClipData().getItemAt(i).getUri();
                        String imagename = GetFileName(imageUri);

                        uriList.add(imageUri);
                        nameList.add(imagename);

                        UploadImageModel imageModel = new UploadImageModel(imageUri.toString(),imagename);
                        imageModelList.add(imageModel);
                        imageAdapter = new UploadImageAdapter(imageModelList);
                        recyclerView.setAdapter(imageAdapter);

                    }

                }
                else if (data.getData() != null) {
                    imageUri = data.getData();
                    String imagename = GetFileName(imageUri);

                    if (uriList.size() == 0){
                        uriList.add(imageUri);
                        nameList.add(imagename);
                    }else {
                        uriList.clear();
                        nameList.clear();
                        uriList.add(imageUri);
                        nameList.add(imagename);
                    }
                    UploadImageModel imageModel = new UploadImageModel(imageUri.toString(),imagename);
                    imageModelList.add(imageModel);
                    imageAdapter = new UploadImageAdapter(imageModelList);
                    recyclerView.setAdapter(imageAdapter);

                }

            }
        }
    }



//TODO- Image upload method#################################################################################
    public void UploadeProductImages(String productID ){

        for (int i= 0; i<uriList.size();i++) {

            int x = totalpic;
            StorageReference mRef = storageReference1.child("image").child(nameList.get(i));
            int finalI = i;
            mRef.putFile(uriList.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String, Object> updateData = new HashMap<>();
                                updateData.put("product_img_" + (x), uri.toString());

                                firebaseFirestore.collection("PRODUCTS")
                                        .document(productID)
                                        .update(updateData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AddProductActivity.this, "Successfully updated "+(finalI+1), Toast.LENGTH_SHORT).show();
                                                    Already_added_Uri_List.add(uri.toString());


                                                    if (finalI==uriList.size()-1){
                                                        uriList.clear();
                                                        loadingDialog.dismiss();
                                                        finish();
                                                        //DashboardFragment.ReloadPage(AddProductActivity.this);
                                                        Toast.makeText(AddProductActivity.this, "Swap to refresh", Toast.LENGTH_SHORT).show();

                                                    }

                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddProductActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                            }
                        });
                    } else {
                        //error toast
                    }
                }
            });
            totalpic = totalpic+1;
        }
        for (int k=0;k<= totalpic ;k++){

        }

        Map<String, Object> dataup = new HashMap<>();
        dataup.put("no_of_img",totalpic);
        FirebaseFirestore.getInstance().collection("PRODUCTS")
                .document(productID).update(dataup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                /////////////////////
            }
        });


    }
    public void UploadThumbnail(String productID){
        StorageReference mRef = storageReference1.child("image").child(thumbName);

        mRef.putFile(thumbUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String, Object> uploadThumbMap = new HashMap<>();
                            uploadThumbMap.put("product_thumbnail", uri.toString());
                            firebaseFirestore.collection("PRODUCTS")
                                    .document(productID)
                                    .update(uploadThumbMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                //
                                            }

                                        }
                                    });
                        }
                    });
                }else {
                    //
                }
            }
        });



    }

    public String GetFileName(Uri uri) { // for image names
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
//TODO- Product upload method#################################################################################
    public void AddNewProduct(){
        TagsToList();

        Map <String,Object> addProductMap = new HashMap<>();
        addProductMap.put("book_title",bookName.getEditText().getText().toString());
        addProductMap.put("book_writer",writerName.getEditText().getText().toString());
        addProductMap.put("book_publisher",publisherName.getEditText().getText().toString());
        addProductMap.put("book_details",description.getEditText().getText().toString());
        addProductMap.put("book_ISBN",isbnNo.getEditText().getText().toString());
        addProductMap.put("book_language",languagE.getEditText().getText().toString());
        addProductMap.put("book_pageCount",pageCount.getEditText().getText().toString());
        addProductMap.put("price_Rs",bookPrice.getEditText().getText().toString());
        if (!discountPrice.getEditText().getText().toString().isEmpty()){
            addProductMap.put("price_offer",discountPrice.getEditText().getText().toString());
        }
        addProductMap.put("book_condition",radioButton1.getText().toString());

        if (printDateMandatory){
            addProductMap.put("book_printed_ON",printDate1.getText().toString().trim());
        }else {
            if (!printDate2.getText().toString().isEmpty()){
                addProductMap.put("book_printed_ON",printDate2.getText().toString().trim());
            }else {
                addProductMap.put("book_printed_ON","Not available");
            }
        }

        addProductMap.put("in_stock",inStock);
        addProductMap.put("in_stock_quantity",Integer.parseInt(stockQuantity.getText().toString()));
        addProductMap.put("categories", categoryList);
        addProductMap.put("tags",tagList);
        addProductMap.put("rating_total",(long)0);
        addProductMap.put("rating_avg","");
        addProductMap.put("rating_Star_5",(long)0);
        addProductMap.put("rating_Star_4",(long)0);
        addProductMap.put("rating_Star_3",(long)0);
        addProductMap.put("rating_Star_2",(long)0);
        addProductMap.put("rating_Star_1",(long)0);
        addProductMap.put("PRODUCT_CREATED_ON", FieldValue.serverTimestamp());
        addProductMap.put("PRODUCT_SELLER_ID",firebaseAuth.getUid());



        loadingDialog.show();
        firebaseFirestore.collection("PRODUCTS")
                .document(databaseProductID)
                .set(addProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    UploadeProductImages(databaseProductID);
                    UploadThumbnail(databaseProductID);


                    Map <String,Object> userMyproductMap = new HashMap<>();
                    userMyproductMap.put(totalProduct+"_product_id",databaseProductID);
                    userMyproductMap.put("listSize",totalProduct+1);
                    firebaseFirestore.collection("USERS")
                            .document(firebaseAuth.getUid())
                            .collection("SELLER_DATA").document("6_ALL_PRODUCT")
                            .update(userMyproductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }
                        }
                    });

                }
            }
        });
    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.check_action:
                categoryList.add(checkAction.getText().toString());
                Toast.makeText(this, checkAction.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_adult:
                categoryList.add(checkAdult.getText().toString());
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;

            case R.id.check_art:
                categoryList.add(checkArt.getText().toString());
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_biography:
                categoryList.add(checkBiography.getText().toString());
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_business:
                categoryList.add(checkBusiness.getText().toString());
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_child:
                categoryList.add(checkChild.getText().toString());
                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_comics:
                categoryList.add(checkComics.getText().toString());
                Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_diary:
                categoryList.add(checkDiary.getText().toString());
                Toast.makeText(this, "7", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_dictionary:
                categoryList.add(checkDictionary.getText().toString());
                Toast.makeText(this, "8", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_drama:
                categoryList.add(checkDrama.getText().toString());
                Toast.makeText(this, "9", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_educational:
                categoryList.add(checkEducational.getText().toString());
                Toast.makeText(this, "10", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_encyclopedia:
                categoryList.add(checkEncyclopedia.getText().toString());
                Toast.makeText(this, "11", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_gk:
                categoryList.add(checkGk.getText().toString());
                Toast.makeText(this, "12", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_health:
                categoryList.add(checkHealth.getText().toString());
                Toast.makeText(this, "13", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_horror:
                categoryList.add(checkHorror.getText().toString());
                Toast.makeText(this, "14", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_job:
                categoryList.add(checkJob.getText().toString());
                Toast.makeText(this, "15", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_journal:
                categoryList.add(checkJournal.getText().toString());
                Toast.makeText(this, "16", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_Magazine:
                categoryList.add(checkMagazine.getText().toString());
                Toast.makeText(this, "17", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_novels:
                categoryList.add(checkNovels.getText().toString());
                Toast.makeText(this, "18", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_reference:
                categoryList.add(checkReference.getText().toString());
                Toast.makeText(this, "19", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_religion:
                categoryList.add(checkReligion.getText().toString());
                Toast.makeText(this, "20", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_Romance:
                categoryList.add(checkRomance.getText().toString());
                Toast.makeText(this, "21", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_science:
                categoryList.add(checkScience.getText().toString());
                Toast.makeText(this, "22", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_Stories:
                categoryList.add(checkStories.getText().toString());
                Toast.makeText(this, "23", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_supernatural:
                categoryList.add(checkSupernatural.getText().toString());
                Toast.makeText(this, "24", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_thriller:
                categoryList.add(checkThriller.getText().toString());
                Toast.makeText(this, "25", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_travel:
                categoryList.add(checkTravel.getText().toString());
                Toast.makeText(this, "26", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_Others:
                categoryList.add(checkOthers.getText().toString());
                Toast.makeText(this, "27", Toast.LENGTH_SHORT).show();
                break;



        }


    }

    private boolean checkName(){
        String nameInput = bookName.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()){
            bookName.setErrorEnabled(true);
            bookName.setError("Field can't be empty");
            bookName.requestFocus();
            return false;
        }else {
            bookName.setErrorEnabled(false);
            bookName.setError(null);
            return true;
        }
    }
    private boolean checkPublisher(){
        String nameInput = publisherName.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()){
            publisherName.setErrorEnabled(true);
            publisherName.setError("Field can't be empty");
            publisherName.requestFocus();
            return false;
        }else {
            publisherName.setErrorEnabled(false);
            publisherName.setError(null);
            return true;
        }
    }
    private boolean checkWriter(){
        String nameInput = writerName.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()){
            writerName.setErrorEnabled(true);
            writerName.setError("Field can't be empty");
            return false;
        }else {
            writerName.setErrorEnabled(false);
            writerName.setError(null);
            return true;
        }
    }
    private boolean checkLanguage(){
        String nameInput = languagE.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()){
            languagE.setErrorEnabled(true);
            languagE.setError("Field can't be empty");
            return false;
        }else {
            languagE.setErrorEnabled(false);
            languagE.setError(null);
            return true;
        }
    }
    private boolean checkPagecount(){
        String nameInput = pageCount.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()){
            pageCount.setErrorEnabled(true);
            pageCount.setError("Field can't be empty");
            return false;
        }else {
            pageCount.setErrorEnabled(false);
            pageCount.setError(null);
            return true;
        }
    }
    private boolean checkIsbn(){
        String nameInput = isbnNo.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()){
            isbnNo.setErrorEnabled(true);
            isbnNo.setError("Field can't be empty");
            return false;
        }else {
            isbnNo.setErrorEnabled(false);
            isbnNo.setError(null);
            return true;
        }
    }
    private boolean checkDimension(){
        String input = dimension.getEditText().getText().toString().trim();
        if (input.isEmpty()){
            dimension.setErrorEnabled(true);
            dimension.setError("Field can't be empty");
            return false;
        }else {
            dimension.setErrorEnabled(false);
            dimension.setError(null);
            return true;
        }
    }
    private boolean checkDescription(){
        String input = description.getEditText().getText().toString().trim();
        if (input.isEmpty()){
            description.setErrorEnabled(true);
            description.setError("Field can't be empty in description");
            return false;
        }else {
            description.setErrorEnabled(false);
            description.setError(null);
            return true;
        }
    }
    private boolean checkPrice(){
        String input = bookPrice.getEditText().getText().toString().trim();
        if (input.isEmpty()){
            bookPrice.setErrorEnabled(true);
            bookPrice.setError("Field can't be empty");
            return false;
        }else {
            bookPrice.setErrorEnabled(false);
            bookPrice.setError(null);
            return true;
        }
    }
    private boolean checkDiscount(){

        String input = discountPrice.getEditText().getText().toString().trim();
        if (input.isEmpty()){
            discountPrice.setErrorEnabled(true);
            discountPrice.setError("Field can't be empty");
            return false;
        }else {
            discountPrice.setErrorEnabled(false);
            discountPrice.setError(null);
            return true;
        }
    }
    private boolean checkCondition(){

        if (radioButton1==null){
            bookConditionlayout.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.red));
            return false;
        }else {
            bookConditionlayout.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));
            return true;
        }
    }
    private boolean checkCategory(){

        if (categoryList.size()==0){
            categoryContainer.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.red));

            return false;
        }else {
            categoryContainer.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));
            return true;
        }
    }
    private boolean checkTags(){
        String tags = bookTags.getText().toString();
        if (tags.isEmpty()){
            bookTags.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.red));

            return false;
        }else {
            bookTags.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.purple_500));
            return true;
        }
    }
    private boolean checkStock(){

        if (radioButton2==null){
            container2.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.red));

            return false;
        }else {
            container2.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));
            return true;
        }
    }
    private boolean checkThumbnail(){

        if (thumbUri==null){
            productThambnail.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.red));
            productThambnail.requestFocus();
            return false;
        }else {
            productThambnail.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.white));
            return true;
        }
    }
    private boolean checkProductImage(){

        if (uriList.size()==0){
            selectBtn.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.red));
            selectBtn.requestFocus();
            return false;
        }else {
            selectBtn.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.purple_500));
            return true;
        }
    }
    private boolean validePrintdate1(){
        if (printDateMandatory){
            if (printDate1.getText().toString().isEmpty()){
                printDate1.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.red));
                return false;
            }else {
                return true;
            }

        }else {
            return true;
            // some bug in this method
        }

    }

    public void CheckAllDetails(View v){
        if (!checkName() | !checkPublisher() | !checkWriter() | !checkLanguage()| !checkPagecount()| !checkIsbn()
        | !checkDimension()| !checkDescription() | !checkPrice() | !checkDiscount() | !checkCondition() | !checkCategory()
        | !checkTags() | !checkStock() | !checkThumbnail()| !checkProductImage() | !validePrintdate1()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }else {
            AddNewProduct();
            Toast.makeText(this, "Product is added", Toast.LENGTH_SHORT).show();
        }

    }

    private void TagsToList(){
        String tagString = bookTags.getText().toString().trim();
        String[] tagArray = tagString.split("\\s*,\\s*");
        tagList = Arrays.asList(tagArray);




    }

    private String getSaltString() {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }






}