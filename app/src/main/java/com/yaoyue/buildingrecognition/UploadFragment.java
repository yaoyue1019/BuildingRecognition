package com.yaoyue.buildingrecognition;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

public class UploadFragment extends Fragment {
    public static final int REQUEST_CODE_PHOTO = 1;
    public static final int REQUEST_CODE_ALBUM = 2;

    protected Button btnAlbum;
    protected Button btnPhoto;
    protected Button btnUpload;
    protected ImageView ivPreview;

    protected URI mImageUri;

    protected View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_album:
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

    protected void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
        File file = new File(Environment.getExternalStorageDirectory() + "/temp/photo.png");
        File parent = new File(file.getParent());
        if (!parent.exists()) {
            parent.mkdirs();
        }
        Uri uri = FileProvider.getUriForFile(
                getActivity(),
                "com.yaoyue.buildingrecognition.provider", //(use your app signature + ".provider" )
                file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }

    protected void upload() {
        if (mImageUri == null) {
            Toast.makeText(getActivity(), R.string.toast_image_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PHOTO:
                onActivityResultPhoto(requestCode, resultCode, data);
                break;
            case REQUEST_CODE_ALBUM:
                onActivityResultAlbum(requestCode, resultCode, data);
                break;
        }
    }

    protected void onActivityResultPhoto(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            ivPreview.setImageBitmap(bitmap);
        }
    }

    protected void onActivityResultAlbum(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            ivPreview.setImageBitmap(bitmap);
        }
    }
}
