package com.smartscheduler_admin.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smartscheduler_admin.R;
import com.smartscheduler_admin.model.UserData;
import com.smartscheduler_admin.util.InternetDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private EditText editText_FullName, editText_Email, editText_password, editText_confirm_password;
    private CircleImageView imgCircular_Profile;
    private FirebaseAuth auth;
    private Uri filePath;
    private SweetAlertDialog pDialog;
    private InternetDialog internetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        internetDialog = new InternetDialog(SignUpActivity.this);

        //Firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();

        //Input
        editText_FullName = findViewById(R.id.full_name_input);
        editText_Email = findViewById(R.id.email_input);
        editText_password = findViewById(R.id.password_input);
        editText_confirm_password = findViewById(R.id.re_enter_password);
        TextView alreadyRegistered_TextView = findViewById(R.id.newUserTextView);
        imgCircular_Profile = findViewById(R.id.imgProfile);
        ImageView imgView_selectImage = findViewById(R.id.edit_Img);
        ImageView img_Sign_Up = findViewById(R.id.sign_up_imgView);

        img_Sign_Up.setOnClickListener(v -> {
            if (!internetDialog.getInternetStatus()) {
                return;
            }

            if (editText_FullName.getText().toString().trim().equals("")) {
                editText_FullName.setError("Full Name is required");
                editText_FullName.requestFocus();
                return;
            }

            if (editText_Email.getText().toString().trim().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(editText_Email.getText().toString().trim()).matches()) {
                editText_Email.setError("Email is required");
                editText_Email.requestFocus();
                return;
            }

            if (editText_password.getText().toString().equals("")) {
                editText_password.setError("Password is required");
                editText_password.requestFocus();
                return;
            }

            if (editText_password.getText().toString().length() < 8) {
                editText_password.setError("Password length should be greater or equal to 8 characters");
                editText_password.requestFocus();
                return;
            }

            if (!editText_password.getText().toString().equals(editText_confirm_password.getText().toString())) {
                editText_password.setError("Password did not match with confirm password");
                editText_password.requestFocus();
                return;
            }

            pDialog = new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Creating Admin ...");
            pDialog.setCancelable(false);
            pDialog.show();

            //create user
            auth.createUserWithEmailAndPassword(editText_Email.getText().toString().trim(), editText_password.getText().toString().trim())
                    .addOnCompleteListener(SignUpActivity.this, task -> {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            pDialog.setTitleText("Oops...");
                            pDialog.setContentText("Authentication failed , " + task.getException());
                            pDialog.setCancelable(true);
                            pDialog.show();
                        } else {
                            String UploadedFileLink;
                            if (filePath == null) {
                                UploadedFileLink = "default";
                                UserData user = new UserData(editText_FullName.getText().toString(), editText_Email.getText().toString(), UploadedFileLink);
                                uploadDB(user);
                            } else {
                                uploadImage();
                            }

                        }
                    });
        });


        imgCircular_Profile.setOnClickListener(v -> selectImage(SignUpActivity.this));

        imgView_selectImage.setOnClickListener(v -> selectImage(SignUpActivity.this));

        alreadyRegistered_TextView.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            SignUpActivity.this.finish();
        });
    }

    private void uploadDB(UserData user) {
        pDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Adding Admin Information to Database ...");
        pDialog.setCancelable(false);
        pDialog.show();

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("AppUsers").child("AppAdmins");
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            databaseReference.child(firebaseUser.getUid()).setValue(user);
        }

        pDialog.dismissWithAnimation();

        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    private void uploadImage() {
        pDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Uploading Profile Image...");
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setProgress(0.0f);
        pDialog.show();

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssZ", Locale.getDefault());
        String path = "IMG-" + df.format(Calendar.getInstance().getTime()) + "AP" + filePath.getLastPathSegment();

        StorageReference ref = storageReference.child("AdminImages/" + path);
        ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(pDialog::dismiss, 500);
            Task<Uri> result = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();
            result.addOnSuccessListener(uri -> {
                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                String urlToImage = uri.toString();
                UserData user = new UserData(editText_FullName.getText().toString(), editText_Email.getText().toString(), urlToImage);
                uploadDB(user);
            });
        }).addOnFailureListener(e -> {
            pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Oops...");
            pDialog.setContentText("Unable to Upload Image!");
            pDialog.setCancelable(true);
        }).addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
            pDialog.getProgressHelper().setProgress((float) progress);
            pDialog.setContentText("Uploaded : " + progress + "%");
        });

    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        filePath = data.getData();

                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imgCircular_Profile.setImageBitmap(selectedImage);
                    } else {
                        Toast.makeText(SignUpActivity.this, "No Image Selected or Data in null", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        filePath = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                imgCircular_Profile.setImageURI(filePath);
                                cursor.close();
                            }
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "No Image Selected or Data in null", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}