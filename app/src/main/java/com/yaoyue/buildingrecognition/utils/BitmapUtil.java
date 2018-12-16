package com.yaoyue.buildingrecognition.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class BitmapUtil {
    private static final int IMAGE_TYPE_HOR = 1;
    private static final int IMAGE_TYPE_VER = 2;
    public static Bitmap compress(Bitmap bmp,float width,float height){
        int nWidth;
        int nHeight;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        float rate = width / height;
//        int type;
//        if(rate >1){
//            type = IMAGE_TYPE_HOR;
//        }else{
//            type = IMAGE_TYPE_VER;
//        }
//        float bmpRate = ((float)bmp.getWidth()) / (float)bmp.getHeight();
//        if(bmpRate > 1){
//            if(rate > bmpRate){
//                // keep width
//                nWidth = (int)width;
//                nHeight = (int)(height / rate);
//
//            }else{
//                // keep height
//                nHeight = (int) height;
//                nWidth = (int)(width / rate);
//            }
//        }else{
//            if(rate > bmpRate){
//                // keep height
//                nHeight = (int)height;
//                nWidth = (int)(width/rate);
//            }else {
//                nHeight = (int )(height * rate);
//                nWidth = (int)(width);
//            }
//        }
        bmp.compress(Bitmap.CompressFormat.JPEG,60,baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
//        return BitmapFactory.decodeFile("/sdcard/temp/photo.png", options);
    }

    public static Bitmap stringtoBitmap(String string){
        try {
            byte[]bitmapArray;
            bitmapArray=Base64.decode(string, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String bitmaptoString(Bitmap bitmap){
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }


}
