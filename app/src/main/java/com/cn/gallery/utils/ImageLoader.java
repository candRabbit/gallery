package com.cn.gallery.utils;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

/**
 * Created by linqinglv on 10/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class ImageLoader {

  /**
   * 加载图片 你可以替换其它的图片框架
   * @param context
   * @param path
   * @param imageView
   */
  public static void load(Context context, String path, ImageView imageView) {
    Glide.with(context).load(path).crossFade().into(imageView);
  }
}
