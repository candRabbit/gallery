package com.cn.gallery.activity;

import com.cn.gallery.model.PhotoDir;
import com.cn.gallery.model.PhotoItem;
import java.util.List;
import java.util.Set;
import rx.Observable;

/**
 * Created by linqinglv on 10/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public interface DataSourceDelegate {

  /**
   * 获取相册
   */
  Observable<List<PhotoItem>> getSysPhotos();

  /**
   * 获取相册列表
   */
  Observable<List<PhotoDir>> getPhotoDirs(List<PhotoItem> photoItems);

  /**
   * 获取选择的相片
   */
  List<String> getCheckedPhotos();

  Observable<List<PhotoItem>> getSysPhotosByDir(List<PhotoItem> photoItems, String dir);
}
