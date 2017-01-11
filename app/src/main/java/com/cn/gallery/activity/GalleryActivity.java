package com.cn.gallery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.cn.gallery.Constant;
import com.cn.gallery.R;
import com.cn.gallery.fragment.GalleryFragment;
import com.cn.gallery.model.PhotoDir;
import com.cn.gallery.model.PhotoItem;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by linqinglv on 10/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class GalleryActivity extends AppCompatActivity implements DataSourceDelegate {
  private List<String> checkPhotos;
  private Mode mode = Mode.SINGLE;

  public enum Mode {
    SINGLE, MULTI, CROP
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gallery);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fl_content, new GalleryFragment())
        .commit();
  }

  @Override public Observable<List<PhotoItem>> getSysPhotos() {
    return Observable.
        just(Constant.getSysPhotos(this))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io());
  }

  @Override public Observable<List<PhotoDir>> getPhotoDirs(List<PhotoItem> photoItems) {
    return Observable.just(photoItems)
        .flatMap(new Func1<List<PhotoItem>, Observable<List<PhotoDir>>>() {
          @Override public Observable<List<PhotoDir>> call(List<PhotoItem> photoItems) {
            Set<String> temps = new HashSet<>();
            List<PhotoDir> photoDirs = new ArrayList<>();
            for (PhotoItem photoItem : photoItems) {
              if (!temps.contains(photoItem.dir)) {
                temps.add(photoItem.dir);
                PhotoDir photoDir = new PhotoDir();
                photoDir.name = new File(photoItem.dir).getName();
                photoDir.thumbnailPath = photoItem.imgPath;
                photoDir.dir = photoItem.dir;
                photoDirs.add(photoDir);
              }
            }
            return Observable.just(photoDirs);
          }
        });
  }

  @Override public List<String> getCheckedPhotos() {
    if (null == checkPhotos) checkPhotos = new ArrayList<>();
    return checkPhotos;
  }

  @Override public Observable<List<PhotoItem>> getSysPhotosByDir(final List<PhotoItem> photoItems,
      final String dir) {
    return Observable.create(new Observable.OnSubscribe<List<PhotoItem>>() {
      @Override public void call(Subscriber<? super List<PhotoItem>> subscriber) {
        List<PhotoItem> photoItemList = new ArrayList<>();
        for (PhotoItem photoItem : photoItems) {
          if (dir.equals(photoItem.dir)) {
            photoItemList.add(photoItem);
          }
        }
        subscriber.onNext(photoItemList);
      }
    });
  }
}
