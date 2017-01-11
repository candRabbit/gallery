package com.cn.gallery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.cn.gallery.Constant;
import com.cn.gallery.R;
import com.cn.gallery.activity.DataSourceDelegate;
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
  private DataSourceDelegate dataSourceDelegate;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    dataSourceDelegate = (DataSourceDelegate) context;
  }

  @Override protected int getContentLayoutId() {
    return R.layout.fragment_preview;
  }

  @Override protected void initView(View view) {
    viewPager = (ViewPager) view.findViewById(R.id.view_pager);
  }

  @Override protected void setDataToView() {}

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    final String dir = getArguments().getString(Constant.DIR);
    dataSourceDelegate.getSysPhotos().subscribe(new Action1<List<PhotoItem>>() {
      @Override public void call(List<PhotoItem> photoItems) {
        if (null != dir) {
          dataSourceDelegate.getSysPhotosByDir(photoItems, dir)
              .subscribe(new Action1<List<PhotoItem>>() {
                @Override public void call(List<PhotoItem> photoItems) {
                  viewPager.setAdapter(new PreviewAdapter(getActivity(), photoItems));
                }
              });
        } else {
          viewPager.setAdapter(new PreviewAdapter(getActivity(), photoItems));
        }
      }
    });
  }
}
