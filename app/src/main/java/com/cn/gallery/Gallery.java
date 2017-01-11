package com.cn.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.cn.gallery.activity.GalleryActivity;

/**
 * Created by linqinglv on 11/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class Gallery {
  private Bundle bundle;

  private Gallery(Bundle bundle) {
    this.bundle = bundle;
  }

  public void start(Activity context, GalleryActivity.Mode mode, int requestCode) {
    bundle.putInt(Constant.MODE, mode.getType());
    Intent intent = new Intent(context, GalleryActivity.class);
    intent.putExtras(bundle);
    context.startActivityForResult(intent, requestCode);
  }

  public static class Builder {
    private Bundle bundle = new Bundle();

    public Builder setCrompressSize(int compressWidth, int compressHeight) {
      bundle.putInt(Constant.COMPRESS_WIDTH, compressWidth);
      bundle.putInt(Constant.COMPRESS_HEIGHT, compressHeight);
      return this;
    }

    public Builder setCropSize(int cropWidth, int cropHeight) {
      bundle.putInt(Constant.CROP_WIDTH, cropWidth);
      bundle.putInt(Constant.CROP_HEIGHT, cropHeight);
      return this;
    }

    public Builder setqQuality(int quality) {
      bundle.putInt(Constant.QUALITY, quality);
      return this;
    }

    public Builder setMaxCount(int maxCount) {
      bundle.putInt(Constant.MAX_COUNT, maxCount);
      return this;
    }

    public Gallery builder() {
      return new Gallery(bundle);
    }
  }
}
