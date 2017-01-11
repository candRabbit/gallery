package com.cn.gallery.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by linqinglv on 10/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class CameraUtils {
  public static final int CAMERA = 998;
  public final static int GALLERY = 999;

  private static String takePhotoPath;

  public static void takePhotoByCamera(Context context) {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    takePhotoPath = getPhotoPath();
    intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoPath);
    ((Activity) context).startActivityForResult(intent, CAMERA);
  }

  public static void takePhotoByGallery(Context context) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_PICK);
    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    ((Activity) context).startActivityForResult(intent, GALLERY);
  }

  public static String resolvePhotoPathFromGallery(Context context, Intent data) {
    if (null == data) return null;
    Uri uri = data.getData();
    if (null == uri) return null;
    return getImagePath(context, data.getData());
  }

  public static String getImagePath(Context ctx, Uri uri) {
    String filePath = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      if (DocumentsContract.isDocumentUri(ctx, uri)) {
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };
        String sel = MediaStore.Images.Media._ID + "= ?";
        Cursor cursor = ctx.getContentResolver()
            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[] { id },
                null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
          filePath = cursor.getString(columnIndex);
          cursor.close();
          return filePath;
        }
      }
    }
    filePath = getRealImagePath(ctx, uri);
    return filePath;
  }

  public static String getRealImagePath(Context context, Uri uri) {
    String filePath = null;
    Cursor cu = context.getContentResolver().query(uri, null, null, null, null);
    if (cu != null && cu.getCount() > 0) {
      try {
        cu.moveToFirst();
        final int pathIndex = cu.getColumnIndex(MediaStore.Images.Media.DATA);
        filePath = cu.getString(pathIndex);
        cu.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return filePath;
  }

  public static String getTakePhotoPath() {
    return takePhotoPath;
  }

  public static String getPhotoPath() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.CHINA);
    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    return dir + "/" + simpleDateFormat.format(new Date()) + ".jpg";
  }
}
