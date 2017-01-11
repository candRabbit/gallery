package com.cn.gallery.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.cn.gallery.Constant;
import com.cn.gallery.R;
import com.cn.gallery.fragment.GalleryFragment;
import com.cn.gallery.fragment.PreviewFragment;
import com.cn.gallery.model.PhotoDir;
import com.cn.gallery.model.PhotoItem;
import com.cn.gallery.utils.CameraUtils;
import com.cn.gallery.utils.CropUtils;
import com.cn.gallery.utils.PhotoManager;
import com.cn.gallery.utils.PictureUtils;
import com.yalantis.ucrop.UCrop;
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
public class GalleryActivity extends AppCompatActivity
    implements DataSourceDelegate, GalleryPresenter {
  private GalleryFragment galleryFragment;
  private PreviewFragment previewFragment;

  private PhotoDir photoDir;
  private List<String> checkPhotos;
  private Mode mode = Mode.SINGLE;
  private PreviewMode previewMode = PreviewMode.GIRD;

  private int position;
  private int maxCount;
  private int cropWidth;
  private int cropHeight;
  private int compressWidth;
  private int compressHeight;
  private int quality;

  @Override public void toPreview(int position) {
    previewMode = PreviewMode.PREVIEW;
    this.position = position;
    updateView();
  }

  @Override public void toCrop(String imagePath) {
    CropUtils.toCrop(this, imagePath, cropWidth, cropHeight);
  }

  public enum Mode {
    SINGLE(0), MULTI(1), CROP(2);
    private int type;

    Mode(int type) {
      this.type = type;
    }

    public int getType() {
      return type;
    }

    public static Mode getMode(int type) {
      switch (type) {
        case 0:
          return SINGLE;
        case 1:
          return MULTI;
        case 2:
          return CROP;
      }
      return null;
    }
  }

  public enum PreviewMode {
    GIRD, PREVIEW
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gallery);
    initData();
    updateView();
  }

  private void initData() {
    Bundle bundle = getIntent().getExtras();
    maxCount = bundle.getInt(Constant.MAX_COUNT, 9);
    cropWidth = bundle.getInt(Constant.CROP_WIDTH, 240);
    cropHeight = bundle.getInt(Constant.CROP_HEIGHT, 240);
    compressWidth = bundle.getInt(Constant.COMPRESS_WIDTH, 640);
    compressHeight = bundle.getInt(Constant.COMPRESS_HEIGHT, 960);
    quality = bundle.getInt(Constant.QUALITY, 70);
    mode = Mode.getMode(bundle.getInt(Constant.MODE, 0));
  }

  private void updateView() {
    switch (previewMode) {
      case GIRD:
        if (null != previewFragment && previewFragment.isAdded()) {
          getSupportFragmentManager().beginTransaction().detach(previewFragment).commit();
        }
        if (null == galleryFragment) {
          galleryFragment = new GalleryFragment();
          getSupportFragmentManager().beginTransaction()
              .add(R.id.fl_content, galleryFragment)
              .commit();
        } else {
          if (null != previewFragment) {
            getSupportFragmentManager().beginTransaction().attach(galleryFragment).commit();
          }
        }
        break;
      case PREVIEW:
        if (galleryFragment.isAdded()) {
          getSupportFragmentManager().beginTransaction().detach(galleryFragment).commit();
        }
        if (null == previewFragment) {
          previewFragment = new PreviewFragment();
          Bundle bundle = new Bundle();
          bundle.putInt(Constant.POSITION, position);
          if (null != photoDir) {
            bundle.putString(Constant.DIR, photoDir.dir);
          }
          previewFragment.setArguments(bundle);
          getSupportFragmentManager().beginTransaction()
              .add(R.id.fl_content, previewFragment)
              .commit();
        } else {
          if (null != galleryFragment) {
            Bundle bundle = previewFragment.getArguments();
            if (null != photoDir) {
              bundle.putString(Constant.DIR, photoDir.dir);
            }
            bundle.putInt(Constant.POSITION, position);
            getSupportFragmentManager().beginTransaction().attach(previewFragment).commit();
          }
        }
        break;
    }
  }

  @Override public Observable<List<PhotoItem>> getSysPhotos() {
    return Observable.
        just(PhotoManager.getSysPhotos(this))
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

  @Override public int getMaxCount() {
    return maxCount;
  }

  @Override public Mode getMode() {
    return mode;
  }

  @Override public PhotoDir currentPhotoDir() {
    return photoDir;
  }

  @Override public void setPhotoDir(PhotoDir photoDir) {
    this.photoDir = photoDir;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case CameraUtils.CAMERA:
          //更新媒体库
          Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
          intent.setData(Uri.fromFile(new File(CameraUtils.getTakePhotoPath())));
          sendBroadcast(intent);
          //if (null != galleryFragment) galleryFragment.addNewPhoto(CameraUtils.getTakePhotoPath());
          if (mode == Mode.CROP) {
            CropUtils.toCrop(this, CameraUtils.getTakePhotoPath(), cropWidth, cropHeight);
          } else {
            toResult();
          }
          break;
        case UCrop.REQUEST_CROP:
          Uri resultUri = UCrop.getOutput(data);
          getCheckedPhotos().add(resultUri.getPath());
          toResult();
          break;
      }
    }
  }

  private void toResult() {
    Intent data = new Intent();
    data.putStringArrayListExtra(Constant.SELECT_PHOTOS, (ArrayList<String>) getCheckedPhotos());
    setResult(RESULT_OK, data);
    finish();
  }

  @Override public void onBackPressed() {
    if (previewMode == PreviewMode.PREVIEW) {
      previewMode = PreviewMode.GIRD;
      updateView();
      return;
    }
    super.onBackPressed();
  }
}
