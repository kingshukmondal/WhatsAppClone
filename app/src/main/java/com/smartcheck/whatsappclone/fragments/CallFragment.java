package com.smartcheck.whatsappclone.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.smartcheck.whatsappclone.Const;
import com.smartcheck.whatsappclone.R;

import java.io.InputStream;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * create an instance of this fragment.
 */
public class CallFragment extends Fragment {

    static String link;
    View view;
    ImageView profileimage;
    TextView name, number;
    LinearLayout changename, changepropic, removepropic, deleteaccount, github;
    Uri filepath = Uri.parse("https://firebasestorage.googleapis.com/v0/b/whatsapp-clone-cb61b.appspot.com/o/defimage.png?alt=media&token=7daf9f94-6b7a-4d89-8d76-d194af0d5dc5");
    Bitmap bitmap;
    BottomSheetDialog dialog;
    Dialog dialog1;
    ImageView iv;
    private DatabaseReference mDatabase;

    public CallFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        view = inflater.inflate(R.layout.fragment_call, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setpropic();

        name = view.findViewById(R.id.name);
        number = view.findViewById(R.id.number);
        profileimage = view.findViewById(R.id.profileimage);
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog1 = new Dialog(getContext(), R.style.PauseDialog);
                dialog1.setContentView(R.layout.custom_image);
                dialog1.getWindow().setGravity(Gravity.TOP);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView iv = dialog1.findViewById(R.id.proimageienhanced);
                Glide.with(getContext())
                        .load(link)
                        .error(R.drawable.whatsappprofilephoto)
                        .into(iv);
                dialog1.show();
            }
        });
        changepropic = view.findViewById(R.id.changeprofilepicture);
        removepropic = view.findViewById(R.id.removeprofilepicture);
        removepropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You won't be able to recover this file!")
                        .setConfirmText("Delete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                rempp();
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        });
        changepropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity())
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
        deleteaccount = view.findViewById(R.id.deleteaccount);
        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You won't be able to recover this file!")
                        .setConfirmText("Delete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                delAcc();
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        });
        github = view.findViewById(R.id.github);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        dialog = new BottomSheetDialog(getContext());
        createDialogue();
        changename = view.findViewById(R.id.changename);
        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return view;

    }

    private void delAcc() {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("user").child(Const.phone_number);
        mPostReference.removeValue();
        SharedPreferences preferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        getActivity().finishAffinity();
    }

    private void rempp() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference uploader = storage.getReference("Image1" + new Random().nextInt(50));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user").child(Const.phone_number);
        myRef.child("iv").setValue("https://firebasestorage.googleapis.com/v0/b/whatsapp-clone-cb61b.appspot.com/o/defimage.png?alt=media&token=7daf9f94-6b7a-4d89-8d76-d194af0d5dc5");
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/whatsapp-clone-cb61b.appspot.com/o/defimage.png?alt=media&token=7daf9f94-6b7a-4d89-8d76-d194af0d5dc5")
                .error(R.drawable.whatsappprofilephoto)
                .into(profileimage);

        link = "https://firebasestorage.googleapis.com/v0/b/whatsapp-clone-cb61b.appspot.com/o/defimage.png?alt=media&token=7daf9f94-6b7a-4d89-8d76-d194af0d5dc5";


    }

    //////////////////////////////////////////////////////////////
    private void createDialogue() {
        View view = getLayoutInflater().inflate(R.layout.change_entername, null, false);
        TextView submit = view.findViewById(R.id.submitnewname);
        EditText et1 = view.findViewById(R.id.newname);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et1.getText().toString();
                if (!name.equals("")) {
                    changenameuser(name);
                    dialog.dismiss();
                } else {
                    et1.requestFocus();
                    et1.setError("Empty name");
                }
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    private void changenameuser(String name) {


        mDatabase.child("user").child(Const.phone_number).child("name").setValue(name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Sucessfully updated", Toast.LENGTH_SHORT).show();
                        setpropic();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
    }

    private void setpropic() {
        mDatabase.child("user").child(Const.phone_number).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase2", String.valueOf(task.getResult().getValue()));
                    name.setText(String.valueOf(task.getResult().child("name").getValue()));
                    number.setText(Const.phone_number);
                    link = (String.valueOf(task.getResult().child("iv").getValue()));
                    Glide.with(getContext())
                            .load(String.valueOf(task.getResult().child("iv").getValue()))
                            .error(R.drawable.whatsappprofilephoto)
                            .into(profileimage);
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////
    private void uploadtoDatabase() {

        final ProgressDialog dialog = new ProgressDialog(view.getContext());
        dialog.setTitle("File Uploader");
        dialog.show();
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
                                    dialog.dismiss();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("user").child(Const.phone_number);
                                    myRef.child("iv").setValue(uri.toString());
                                    link = uri.toString();
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", getActivity().MODE_PRIVATE);
                                } catch (Exception e) {

                                }
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :" + (int) percent + " %");
                    }
                })
        ;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {

            filepath = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                uploadtoDatabase();
                profileimage.setImageBitmap(bitmap);
            } catch (Exception ex) {
                profileimage.setImageResource(R.drawable.whatsappprofilephoto);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    ////////////////////////////////////////////////////////////
}