package com.cn.gallery.activity;

import com.cn.gallery.model.PhotoDir;

/**
 * Created by linqinglv on 11/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public interface GalleryPresenter {

  void toPreview(int position);

  void toCrop(String imagePath);

  GalleryActivity.Mode getMode();

  PhotoDir currentPhotoDir();

  void setPhotoDir(PhotoDir photoDir);
}
