package com.cn.gallery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.cn.gallery.Constant;
import com.cn.gallery.R;
import com.cn.gallery.activity.DataSourceDelegate;
import com.cn.gallery.activity.GalleryActivity;
import com.cn.gallery.activity.GalleryPresenter;
import com.cn.gallery.adapter.PreviewAdapter;
import com.cn.gallery.model.PhotoItem;
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
public class PreviewFragment extends BaseFragment {
  private ViewPager viewPager;
  private Button cb;
  private DataSourceDelegate dataSourceDelegate;
  private GalleryPresenter galleryPresenter;
  private PreviewAdapter previewAdapter;
  private int position;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    dataSourceDelegate = (DataSourceDelegate) context;
    galleryPresenter = (GalleryPresenter) context;
  }

  @Override protected int getContentLayoutId() {
    return R.layout.fragment_preview;
  }

  @Override protected void initView(View view) {
    cb = (Button) view.findViewById(R.id.btn_cb);
    viewPager = (ViewPager) view.findViewById(R.id.view_pager);
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override public void onPageSelected(int position) {
        updateCheck(position);
      }

      @Override public void onPageScrollStateChanged(int state) {
      }
    });

    cb.setOnClickListener(v -> {
      v.setSelected(!v.isSelected());
      String imgPath = previewAdapter.getPhotoItems().get(viewPager.getCurrentItem()).imgPath;
      if (v.isSelected()) {
        if (!dataSourceDelegate.getCheckedPhotos().contains(imgPath)) {
          if (galleryPresenter.getMode() == GalleryActivity.Mode.SINGLE) {
            dataSourceDelegate.getCheckedPhotos().clear();
          }
          if (galleryPresenter.getMode() == GalleryActivity.Mode.MULTI) {
            if (dataSourceDelegate.getCheckedPhotos().size() >= Constant.MAX_COUNT) {
              Toast.makeText(getActivity(), String.format("选择数量不能超过%s张", Constant.MAX_COUNT),
                  Toast.LENGTH_SHORT).show();
              v.setSelected(false);
              return;
            }
          }
          dataSourceDelegate.getCheckedPhotos().add(imgPath);
        }
      } else {
        if (dataSourceDelegate.getCheckedPhotos().contains(imgPath)) {
          dataSourceDelegate.getCheckedPhotos().remove(imgPath);
        }
      }
    });
  }

  @Override protected void setDataToView() {
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    final String dir = getArguments().getString(Constant.DIR);
    position = getArguments().getInt(Constant.POSITION);
    dataSourceDelegate.getSysPhotos().subscribe(photoItems -> {
      if (null != dir) {
        dataSourceDelegate.getSysPhotosByDir(photoItems, dir).subscribe(dirPhotoItems -> {
          setDataToViewPager(dirPhotoItems);
        });
      } else {
        setDataToViewPager(photoItems);
      }
    });
  }

  private void setDataToViewPager(List<PhotoItem> photoItems) {
    previewAdapter = new PreviewAdapter(getActivity(), photoItems);
    viewPager.setAdapter(previewAdapter);
    viewPager.setCurrentItem(position, false);
    updateCheck(position);
  }

  private void updateCheck(int position){
    cb.setSelected(dataSourceDelegate.getCheckedPhotos()
        .contains(previewAdapter.getPhotoItems().get(position).imgPath));
  }
}
