package com.cn.gallery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.cn.gallery.Constant;
import com.cn.gallery.R;
import com.cn.gallery.activity.DataSourceDelegate;
import com.cn.gallery.activity.GalleryPresenter;
import com.cn.gallery.adapter.GalleryAdapter;
import com.cn.gallery.adapter.PhotoDirAdapter;
import com.cn.gallery.callback.OnItemClickListener;
import com.cn.gallery.model.PhotoDir;
import com.cn.gallery.model.PhotoItem;
import com.cn.gallery.view.PopMenu;
import java.io.File;
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
    tvSelectPhotoDir.setOnClickListener(v -> {
      if (popMenu.isOpen) {
        popMenu.close();
      } else {
        popMenu.show();
      }
    });
  }

  @Override protected void setDataToView() {
    dataSourceDelegate.getSysPhotos().subscribe(photoItems -> {
      setDataToPhotoDir(photoItems);
    });
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (null != galleryAdapter) {
      galleryAdapter.notifyDataSetChanged();
    }
  }

  private void setDataToGallery(List<PhotoItem> photoItems) {
    if (null != photoItems && !photoItems.isEmpty()){
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
    PhotoItem photoItem = Constant.getPhoto(getContext(), imagePath);
    galleryAdapter.add(1, photoItem);
  }

  private void setDataToPhotoDir(final List<PhotoItem> photoItems) {
    dataSourceDelegate.getPhotoDirs(photoItems).subscribe(photoDirs -> {
      PhotoDir rootDir = new PhotoDir();
      rootDir.name = "全部";
      rootDir.thumbnailPath = photoItems.get(0).imgthumbnail;
      photoDirs.add(0, rootDir);
      photoDirAdapter = new PhotoDirAdapter(getActivity(), photoDirs);
      recyclerViewPhotoDir.setAdapter(photoDirAdapter);
      photoDirAdapter.setPhotoDirOnItemClickListener(photoDir -> {
        popMenu.close();
        tvSelectPhotoDir.setText(photoDir.name);
        if (null == photoDir.dir) {
          setDataToGallery(photoItems);
        } else {
          galleryPresenter.setPhotoDir(photoDir);
          dataSourceDelegate.getSysPhotosByDir(photoItems, photoDir.dir)
              .subscribe(dirPhotoItems -> {
                setDataToGallery(dirPhotoItems);
              });
        }
      });
      setDataToGallery(photoItems);
    });
  }
}
