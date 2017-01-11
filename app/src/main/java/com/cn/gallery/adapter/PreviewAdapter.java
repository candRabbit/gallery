package com.cn.gallery.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.cn.gallery.lib.photoview.PhotoView;
import com.cn.gallery.model.PhotoItem;
import com.cn.gallery.utils.ImageLoader;
import java.util.List;

/**
 * Created by linqinglv on 10/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class PreviewAdapter extends PagerAdapter {
  private Context context;
  private List<PhotoItem> photoItems;

  public PreviewAdapter(Context context, List<PhotoItem> photoItems) {
    this.context = context;
    this.photoItems = photoItems;
  }

  @Override public int getCount() {
    return null == photoItems ? 0 : photoItems.size();
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    PhotoView photoView = new PhotoView(context);
    photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
    ImageLoader.load(context, photoItems.get(position).imgPath, photoView);
    container.addView(photoView);
    return photoView;
  }

  public List<PhotoItem> getPhotoItems(){
    return photoItems;
  }
}
