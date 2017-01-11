package com.cn.gallery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by linqinglv on 11/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class GeometricView extends View {
  public GeometricView(Context context) {
    super(context);
  }

  public GeometricView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
  }
}
