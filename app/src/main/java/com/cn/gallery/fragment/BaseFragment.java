package com.cn.gallery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by linqinglv on 10/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public abstract class BaseFragment extends Fragment {
  private View rootView;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    if (null == rootView) {
      rootView = inflater.inflate(getContentLayoutId(), null);
      initView(rootView);
      setDataToView();
    } else {
      ViewGroup viewGroup = (ViewGroup) rootView.getParent();
      if (null != viewGroup) {
        viewGroup.removeView(rootView);
      }
    }
    return rootView;
  }

  protected abstract int getContentLayoutId();

  protected abstract void initView(View view);

  protected abstract void setDataToView();
}
