package com.cn.gallery.fragment;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.cn.gallery.R;
import com.cn.gallery.activity.DataSourceDelegate;
import com.cn.gallery.adapter.GalleryAdapter;
import com.cn.gallery.adapter.PhotoDirAdapter;
import com.cn.gallery.callback.OnItemClickListener;
import com.cn.gallery.model.PhotoDir;
import com.cn.gallery.model.PhotoItem;
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

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    dataSourceDelegate = (DataSourceDelegate) context;
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
    dataSourceDelegate.getSysPhotos().subscribe(new Action1<List<PhotoItem>>() {
      @Override public void call(final List<PhotoItem> photoItems) {
        setDataToPhotoDir(photoItems);
      }
    });
  }

  private void setDataToGallery(List<PhotoItem> photoItems) {
    PhotoItem photoItem = new PhotoItem();
    photoItem.imgthumbnail = "file:///android_asset/ic_camera.png";
    photoItems.add(0, photoItem);
    if (null == galleryAdapter) {
      galleryAdapter = new GalleryAdapter(photoItems, getActivity());
      recyclerView.setAdapter(galleryAdapter);
    } else {
      galleryAdapter.replace(photoItems);
    }
  }

  private void setDataToPhotoDir(final List<PhotoItem> photoItems) {
    dataSourceDelegate.getPhotoDirs(photoItems).subscribe(new Action1<List<PhotoDir>>() {
      @Override public void call(List<PhotoDir> photoDirs) {
        photoDirAdapter = new PhotoDirAdapter(getActivity(), photoDirs);
        recyclerViewPhotoDir.setAdapter(photoDirAdapter);
        photoDirAdapter.setPhotoDirOnItemClickListener(new OnItemClickListener<PhotoDir>() {
          @Override public void onClick(PhotoDir photoDir) {
            popMenu.close();
            tvSelectPhotoDir.setText(photoDir.name);
            dataSourceDelegate.getSysPhotosByDir(photoItems, photoDir.dir)
                .subscribe(new Action1<List<PhotoItem>>() {
                  @Override public void call(List<PhotoItem> photoItems) {
                    setDataToGallery(photoItems);
                  }
                });
          }
        });
        setDataToGallery(photoItems);
      }
    });
  }
}
