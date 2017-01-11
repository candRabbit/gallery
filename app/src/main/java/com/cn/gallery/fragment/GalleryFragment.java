package com.cn.gallery.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.cn.gallery.Constant;
import com.cn.gallery.R;
import com.cn.gallery.activity.DataSourceDelegate;
import com.cn.gallery.activity.GalleryPresenter;
import com.cn.gallery.adapter.GalleryAdapter;
import com.cn.gallery.adapter.PhotoDirAdapter;
import com.cn.gallery.callback.OnItemClickListener;
import com.cn.gallery.model.PhotoDir;
import com.cn.gallery.model.PhotoItem;
import com.cn.gallery.utils.PhotoManager;
import com.cn.gallery.view.PopMenu;
import java.util.List;
import rx.functions.Action1;

/**
 * Created by linqinglv on 10/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class GalleryFragment extends BaseFragment {
  private RecyclerView recyclerView;
  private RecyclerView recyclerViewPhotoDir;
  private PopMenu popMenu;
  private TextView tvSelectPhotoDir;

  private PhotoDirAdapter photoDirAdapter;
  private GalleryAdapter galleryAdapter;

  private DataSourceDelegate dataSourceDelegate;
  private GalleryPresenter galleryPresenter;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    dataSourceDelegate = (DataSourceDelegate) context;
    galleryPresenter = (GalleryPresenter) context;
  }

  @Override protected int getContentLayoutId() {
    return R.layout.fragment_gallery;
  }

  @Override protected void initView(View view) {
    recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
    recyclerView.setLayoutManager(gridLayoutManager);
    recyclerView.setHasFixedSize(true);

    recyclerViewPhotoDir = (RecyclerView) view.findViewById(R.id.recyclerView_dir);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerViewPhotoDir.setLayoutManager(linearLayoutManager);

    popMenu = (PopMenu) view.findViewById(R.id.popMenu);
    tvSelectPhotoDir = (TextView) view.findViewById(R.id.tv_check_photo_dir);
    tvSelectPhotoDir.setText("全部");
    tvSelectPhotoDir.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (popMenu.isOpen) {
          popMenu.close();
        } else {
          popMenu.show();
        }
      }
    });
  }

  @Override protected void setDataToView() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(getActivity(),
          Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        loadData();
      } else {
        requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
            Constant.REQUEST_STORAGE_PERMISSION);
      }
    }
  }

  private void loadData() {
    dataSourceDelegate.getSysPhotos().subscribe(new Action1<List<PhotoItem>>() {
      @Override public void call(List<PhotoItem> photoItems) {
        setDataToPhotoDir(photoItems);
      }
    });
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (null != galleryAdapter) {
      galleryAdapter.notifyDataSetChanged();
    }
  }

  private void setDataToGallery(List<PhotoItem> photoItems) {
    if (null != photoItems && !photoItems.isEmpty()) {
      if (!photoItems.get(0).imgthumbnail.equals("file:///android_asset/ic_camera.png")) {
        PhotoItem photoItem = new PhotoItem();
        photoItem.imgthumbnail = "file:///android_asset/ic_camera.png";
        photoItems.add(0, photoItem);
      }
    }
    if (null == galleryAdapter) {
      galleryAdapter = new GalleryAdapter(photoItems, getActivity());
      recyclerView.setAdapter(galleryAdapter);
    } else {
      galleryAdapter.replace(photoItems);
    }
  }

  public void addNewPhoto(String imagePath) {
    PhotoItem photoItem = PhotoManager.getPhoto(getContext(), imagePath);
    galleryAdapter.add(1, photoItem);
  }

  private void setDataToPhotoDir(final List<PhotoItem> photoItems) {
    dataSourceDelegate.getPhotoDirs(photoItems).subscribe(new Action1<List<PhotoDir>>() {
      @Override public void call(List<PhotoDir> photoDirs) {
        PhotoDir rootDir = new PhotoDir();
        rootDir.name = "全部";
        rootDir.thumbnailPath = photoItems.get(0).imgthumbnail;
        photoDirs.add(0, rootDir);
        photoDirAdapter = new PhotoDirAdapter(getActivity(), photoDirs);
        recyclerViewPhotoDir.setAdapter(photoDirAdapter);
        photoDirAdapter.setPhotoDirOnItemClickListener(new OnItemClickListener<PhotoDir>() {
          @Override public void onClick(PhotoDir photoDir) {
            popMenu.close();
            tvSelectPhotoDir.setText(photoDir.name);
            if (null == photoDir.dir) {
              galleryPresenter.setPhotoDir(null);
              setDataToGallery(photoItems);
            } else {
              galleryPresenter.setPhotoDir(photoDir);
              photoDirAdapter.notifyDataSetChanged();
              dataSourceDelegate.getSysPhotosByDir(photoItems, photoDir.dir)
                  .subscribe(new Action1<List<PhotoItem>>() {
                    @Override public void call(List<PhotoItem> dirPhotoItems) {
                      setDataToGallery(dirPhotoItems);
                    }
                  });
            }
          }
        });
        setDataToGallery(photoItems);
      }
    });
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == Constant.REQUEST_STORAGE_PERMISSION) {
      for (int grant : grantResults) {
        if (grant != PackageManager.PERMISSION_GRANTED) {
          Toast.makeText(getActivity(), "权限被拒绝无法访问相册", Toast.LENGTH_SHORT).show();
          return;
        }
      }
      loadData();
    }
  }
}
