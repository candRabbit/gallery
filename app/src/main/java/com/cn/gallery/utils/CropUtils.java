package com.cn.gallery.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import com.cn.gallery.Constant;
import com.cn.gallery.R;
import com.yalantis.ucrop.UCrop;
import java.io.File;

/**
 * Created by linqinglv on 11/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class CropUtils {

  public static void toCrop(Context context, String imagePath, int cropWidth, int cropHeight) {
    UCrop.Options options = new UCrop.Options();
    options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    options.setHideBottomControls(true);
    UCrop.of(Uri.fromFile(new File(imagePath)), Uri.fromFile(new File(CameraUtils.getPhotoPath())))
        .withMaxResultSize(cropWidth, cropHeight)
        .withAspectRatio(1, 1)
        .withOptions(options)
        .start((Activity) context);
  }
}
