package com.yaoyue.buildingrecognition;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

public class UploadFragment extends Fragment {
    public static final int REQUEST_CODE_PHOTO = 1;
    public static final int REQUEST_CODE_ALBUM = 2;
    public static final int REQUEST_CODE_REQUEST_PERMISSION = 3;

    protected Button btnAlbum;
    protected Button btnPhoto;
    protected Button btnUpload;
    protected ImageView ivPreview;

    protected File mImageFile;

    protected View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_album:
                    album();
                    break;
                case R.id.btn_photo:
                    takePhoto();
                case R.id.btn_upload:
                    upload();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, null);
        initComponents(view);
        return view;
    }

    protected void initComponents(View view) {
        btnAlbum = view.findViewById(R.id.btn_album);
        btnPhoto = view.findViewById(R.id.btn_photo);
        btnUpload = view.findViewById(R.id.btn_upload);
        ivPreview = view.findViewById(R.id.iv_preview);

        btnAlbum.setOnClickListener(mOnClickListener);
        btnPhoto.setOnClickListener(mOnClickListener);
        btnUpload.setOnClickListener(mOnClickListener);
    }

    protected void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_REQUEST_PERMISSION);
    }

    protected boolean checkPermissions(String... permissions) {
        if (permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (getActivity().checkSelfPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void album() {
        if (checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            openAlbum();
        } else {
            requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    protected void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mImageFile = new File("/sdcard/temp/photo.png");
        Uri uri = FileProvider.getUriForFile(getActivity(), "com.yaoyue.buildingrecognition.fileprovider", mImageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    protected void takePhoto() {
        if (checkPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            startCamera();
        } else {
            requestPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    protected void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageFile = new File("/sdcard/temp/photo.png");
        Uri uri = FileProvider.getUriForFile(getActivity(), "com.yaoyue.buildingrecognition.fileprovider", mImageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }

    protected void upload() {
        if (mImageFile == null) {
            Toast.makeText(getActivity(), R.string.toast_image_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("test", "request");
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PHOTO:
                onActivityResultPhoto(requestCode, resultCode, data);
                break;
            case REQUEST_CODE_ALBUM:
                onActivityResultAlbum(requestCode, resultCode, data);
                break;
            case REQUEST_CODE_REQUEST_PERMISSION:
                break;
        }
    }

    protected void onActivityResultPhoto(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            setImage(mImageFile.getAbsolutePath());
        }
    }

    protected Bitmap bitmap;

    protected void setImage(String path) {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            bitmap.recycle();
        }
        bitmap = bmp;
        ivPreview.setImageBitmap(bitmap);
    }

    @Override
    public void onDestroy() {
        setImage(null);
        super.onDestroy();
    }

    protected void onActivityResultAlbum(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            setImage(imagePath);
            c.close();
        }
    }
}
