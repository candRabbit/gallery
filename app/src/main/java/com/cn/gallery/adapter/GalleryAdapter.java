package com.cn.gallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.cn.gallery.Constant;
import com.cn.gallery.R;
import com.cn.gallery.activity.DataSourceDelegate;
import com.cn.gallery.activity.GalleryActivity;
import com.cn.gallery.activity.GalleryPresenter;
import com.cn.gallery.model.PhotoItem;
import com.cn.gallery.utils.CameraUtils;
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
  private GalleryPresenter galleryPresenter;

  public GalleryAdapter(List<PhotoItem> photoItems, Context context) {
    this.photoItems = photoItems;
    this.context = context;
    layoutInflater = LayoutInflater.from(context);
    if (context instanceof DataSourceDelegate) {
      dataSourceDelegate = (DataSourceDelegate) context;
    }
    if (context instanceof GalleryPresenter) {
      galleryPresenter = (GalleryPresenter) context;
    }
  }

  public void replace(List<PhotoItem> photoItems) {
    this.photoItems = photoItems;
    notifyDataSetChanged();
  }

  public void add(int position, PhotoItem photoItem) {
    photoItems.add(position, photoItem);
    notifyItemInserted(position);
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
      cb.setOnClickListener(v -> {
        v.setSelected(!v.isSelected());
        if (v.isSelected()) {
          if (!dataSourceDelegate.getCheckedPhotos().contains(photoItem.imgPath)) {
            checkedStateView.setVisibility(View.VISIBLE);
            if (galleryPresenter.getMode() == GalleryActivity.Mode.SINGLE) {
              dataSourceDelegate.getCheckedPhotos().clear();
            } else {
              if (dataSourceDelegate.getCheckedPhotos().size() >= Constant.MAX_COUNT) {
                Toast.makeText(context, String.format("选择数量不能超过%s张", Constant.MAX_COUNT),
                    Toast.LENGTH_SHORT).show();
                v.setSelected(false);
                return;
              }
            }
            dataSourceDelegate.getCheckedPhotos().add(photoItem.imgPath);
            if (galleryPresenter.getMode() == GalleryActivity.Mode.SINGLE) {
              notifyDataSetChanged();
            }
          }
        } else {
          if (dataSourceDelegate.getCheckedPhotos().contains(photoItem.imgPath)) {
            checkedStateView.setVisibility(View.GONE);
            dataSourceDelegate.getCheckedPhotos().remove(photoItem.imgPath);
          }
        }
      });
      itemView.setOnClickListener(v -> {
        if (0 == getAdapterPosition()) {
          CameraUtils.takePhotoByCamera(context);
        } else {
          switch (galleryPresenter.getMode()) {
            case CROP:
              galleryPresenter.toCrop(photoItem.imgPath);
              break;
            case SINGLE:
            case MULTI:
              galleryPresenter.toPreview(getAdapterPosition()-1);
              break;
          }
        }
      });
    }

    void setDataToView() {
      if (0 == getAdapterPosition()) {
        cb.setVisibility(View.GONE);
      } else {
        cb.setVisibility(View.VISIBLE);
      }
      if (galleryPresenter.getMode() == GalleryActivity.Mode.CROP) {
        cb.setVisibility(View.GONE);
      }
      cb.setSelected(dataSourceDelegate.getCheckedPhotos().contains(photoItem.imgPath));
      checkedStateView.setVisibility(
          dataSourceDelegate.getCheckedPhotos().contains(photoItem.imgPath) ? View.VISIBLE
              : View.GONE);
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
