package com.cn.gallery.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by linqinglv on 11/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class PictureUtils {

  /**
   * 批量压缩图片
   *
   * @param imagePaths 路径
   */
  public static Observable<List<byte[]>> compress(List<String> imagePaths) {
    return compress(imagePaths, 640, 960, 70);
  }

  /**
   * 批量压缩图片
   *
   * @param imagePaths 路径
   * @param compressWidth 缩放的宽
   * @param compressHeight 缩放的高
   * @param quality 质量值
   */
  public static Observable<List<byte[]>> compress(List<String> imagePaths, final int compressWidth,
      final int compressHeight, final int quality) {
    return Observable.just(imagePaths).flatMap(new Func1<List<String>, Observable<List<byte[]>>>() {
      @Override public Observable<List<byte[]>> call(List<String> strings) {
        List<byte[]> bytes = new ArrayList<byte[]>();
        for (String imagePath : strings) {
          bytes.add(compress(imagePath, compressWidth, compressHeight, quality));
        }
        return Observable.just(bytes);
      }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
  }

  private static byte[] compress(String imagePath, int compressWidth, int compressHeight,
      int quality) {
    Bitmap bitmap = getInSampleSizeBitmap(imagePath, compressWidth, compressHeight);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
    try {
      byteArrayOutputStream.flush();
      byteArrayOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    bitmap.recycle();
    return byteArrayOutputStream.toByteArray();
  }

  /**
   * @param filePath
   * @param reqWidth
   * @param reqHeight
   * @return
   */
  public static Bitmap getInSampleSizeBitmap(String filePath, int reqWidth, int reqHeight) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    //    options.inJustDecodeBounds = true;//

    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
    options.inJustDecodeBounds = false;

    Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
    //解决某些机型的图片倒立的问题 例如三星
    bitmap = roteBitmap(bitmap, readPictureDegree(filePath));

    return bitmap;
  }

  /**
   * 读取图片的旋转角度
   */
  public static int readPictureDegree(String path) {
    if (android.text.TextUtils.isEmpty(path)) {
      return 0;
    }
    int degree = 0;
    try {
      ExifInterface exifInterface = new ExifInterface(path);
      int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
          ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          degree = 90;
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          degree = 180;
          break;
        case ExifInterface.ORIENTATION_ROTATE_270:
          degree = 270;
          break;
      }
    } catch (Exception e) {
    }
    return degree;
  }

  /**
   * 旋转图片
   *
   * @param degree 旋转的角度
   */
  public static Bitmap roteBitmap(Bitmap bitmap, int degree) {
    if (0 == degree) {
      return bitmap;
    }
    Matrix matrix = new Matrix();
    matrix.postRotate(degree);
    Bitmap newBitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    return newBitmap;
  }

  /**
   * 比例缩放
   */
  public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
      int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 2;

    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }
}
