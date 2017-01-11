package com.cn.gallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.cn.gallery.R;
import com.cn.gallery.activity.DataSourceDelegate;
import com.cn.gallery.callback.OnItemClickListener;
import com.cn.gallery.model.PhotoDir;
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
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryVH> {
  private List<PhotoItem> photoItems;
  private Context context;
  private LayoutInflater layoutInflater;

  private DataSourceDelegate dataSourceDelegate;

  public GalleryAdapter(List<PhotoItem> photoItems, Context context) {
    this.photoItems = photoItems;
    this.context = context;
    layoutInflater = LayoutInflater.from(context);
    if (context instanceof DataSourceDelegate) {
      dataSourceDelegate = (DataSourceDelegate) context;
    }
  }

  public void replace(List<PhotoItem> photoItems){
    this.photoItems = photoItems;
    notifyDataSetChanged();
  }

  @Override public GalleryVH onCreateViewHolder(ViewGroup parent, int viewType) {
    return new GalleryVH(layoutInflater.inflate(R.layout.item_gallery, null));
  }

  @Override public void onBindViewHolder(GalleryVH holder, int position) {
    holder.photoItem = photoItems.get(position);
    holder.setDataToView();
  }

  @Override public int getItemCount() {
    return null == photoItems ? 0 : photoItems.size();
  }

  class GalleryVH extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private View checkedStateView;
    private Button cb;

    private PhotoItem photoItem;

    GalleryVH(View itemView) {
      super(itemView);
      imageView = (ImageView) itemView.findViewById(R.id.iv);
      checkedStateView = itemView.findViewById(R.id.view_check_state);
      cb = (Button) itemView.findViewById(R.id.btn_cb);
    }

    void setDataToView() {
      ImageLoader.load(context, photoItem.imgthumbnail, imageView);
      if (null != dataSourceDelegate) {
        checkedStateView.setVisibility(
            dataSourceDelegate.getCheckedPhotos().contains(photoItem.imgPath) ? View.VISIBLE
                : View.GONE);
        cb.setSelected(dataSourceDelegate.getCheckedPhotos().contains(photoItem.imgPath));
      }
    }
  }
}
