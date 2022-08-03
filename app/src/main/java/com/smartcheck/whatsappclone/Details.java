package com.smartcheck.whatsappclone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

public class Details extends AppCompatActivity {

    LinearLayout ll1;
    TextView count, name;
    ImageView iv_profile;
    Uri filepath = Uri.parse("https://firebasestorage.googleapis.com/v0/b/whatsapp-clone-cb61b.appspot.com/o/defimage.png?alt=media&token=7daf9f94-6b7a-4d89-8d76-d194af0d5dc5");
    Bitmap bitmap;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ll1 = findViewById(R.id.next);
        name = findViewById(R.id.name);
        count = findViewById(R.id.count);

        pd = new ProgressDialog(getApplicationContext());
        pd.setMessage("uploading");
        iv_profile = findViewById(R.id.image);
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(Details.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image File"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadtoDatabase();

            }
        });


        name.addTextChangedListener(new TextWatcher() {
            int c = 30;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = name.getText().toString();
                int k = s.length();
                ;
                if (30 - k <= 0) {
                    name.setError("MAX LIMIT REACHED");
                }
                count.setText("" + (int) (30 - k));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void uploadtoDatabase() {

        //     pd.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference uploader = storage.getReference("Image1" + new Random().nextInt(50));

        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                try {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("user");
                                    ModalSendingRegistdetails reg = new ModalSendingRegistdetails(name.getText().toString(), uri.toString(), Const.phone_number);
                                    Toast.makeText(Details.this, name.getText().toString() + "" + Const.phone_number, Toast.LENGTH_SHORT).show();
                                    myRef.child(reg.phonenumber.toString()).setValue(reg);
                                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                    myEdit.putString("name", name.getText().toString());
                                    myEdit.putString("number", reg.phonenumber.toString());
                                    myEdit.commit();
                                    Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                                    startActivity(i);
                                } catch (Exception e) {
                                    Toast.makeText(Details.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                iv_profile.setImageBitmap(bitmap);
            } catch (Exception ex) {
                iv_profile.setImageResource(R.drawable.whatsappprofilephoto);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}