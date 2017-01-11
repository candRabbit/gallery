package com.cn.gallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.cn.gallery.R;
import com.cn.gallery.callback.OnItemClickListener;
import com.cn.gallery.model.PhotoDir;
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
public class PhotoDirAdapter extends RecyclerView.Adapter<PhotoDirAdapter.VH> {
  private Context context;
  private List<PhotoDir> photoDirs;

  private LayoutInflater layoutInflater;
  private OnItemClickListener<PhotoDir> photoDirOnItemClickListener;

  public PhotoDirAdapter(Context context, List<PhotoDir> photoDirs) {
    this.context = context;
    this.photoDirs = photoDirs;
    layoutInflater = LayoutInflater.from(context);
  }

  public void setPhotoDirOnItemClickListener(
      OnItemClickListener<PhotoDir> photoDirOnItemClickListener) {
    this.photoDirOnItemClickListener = photoDirOnItemClickListener;
  }

  @Override public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return new VH(layoutInflater.inflate(R.layout.item_photo_dir, null));
  }

  @Override public void onBindViewHolder(VH holder, int position) {
    holder.photoDir = photoDirs.get(position);
    holder.setDataToView();
  }

  @Override public int getItemCount() {
    return null == photoDirs ? 0 : photoDirs.size();
  }

  class VH extends RecyclerView.ViewHolder {
    PhotoDir photoDir;
    ImageView imageView;
    TextView tvDirName;

    VH(View itemView) {
      super(itemView);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (null != photoDirOnItemClickListener) photoDirOnItemClickListener.onClick(photoDir);
        }
      });
      imageView = (ImageView) itemView.findViewById(R.id.iv);
      tvDirName = (TextView) itemView.findViewById(R.id.tv_dir_name);
    }

    void setDataToView() {
      tvDirName.setText(photoDir.name);
      ImageLoader.load(context, photoDir.thumbnailPath, imageView);
    }
  }
}
