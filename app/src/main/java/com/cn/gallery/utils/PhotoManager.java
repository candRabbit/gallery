package com.cn.gallery.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.cn.gallery.model.PhotoItem;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by linqinglv on 11/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class PhotoManager {
  /**
   * 获取系统的相册
   */
  public static List<PhotoItem> getSysPhotos(Context context) {
    Cursor cursor = context.getContentResolver()
        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
    if (null == cursor) return null;
    List<PhotoItem> photoItems = new ArrayList<>();
    while (cursor.moveToNext()) {
      PhotoItem photoItem = new PhotoItem();
      photoItem.imgPath =
          cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
      photoItem.imgthumbnail = cursor.getString(MediaStore.Images.Thumbnails.MINI_KIND);
      photoItem.addTime = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
      File file = new File(photoItem.imgPath);
      photoItem.dir = file.getParent();
      photoItems.add(photoItem);
    }
    cursor.close();
    Collections.sort(photoItems, new Comparator<PhotoItem>() {
      @Override public int compare(PhotoItem o1, PhotoItem o2) {
        if (o1.addTime > o2.addTime) {
          return -1;
        }
        if (o1.addTime < o2.addTime) {
          return 1;
        }
        return 0;
      }
    });
    return photoItems;
  }

  /**
   * 获取系统的相册
   */
  public static PhotoItem getPhoto(Context context, String imagePath) {
    Cursor cursor = context.getContentResolver()
        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
            MediaStore.Images.ImageColumns.DATA + " =? ", new String[] { imagePath }, null);
    if (null == cursor) return null;
    cursor.moveToFirst();
    PhotoItem photoItem = new PhotoItem();
    photoItem.imgPath =
        cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
    photoItem.imgthumbnail = cursor.getString(MediaStore.Images.Thumbnails.MINI_KIND);
    photoItem.addTime = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
    File file = new File(photoItem.imgPath);
    photoItem.dir = file.getParent();
    cursor.close();
    return photoItem;
  }
}
