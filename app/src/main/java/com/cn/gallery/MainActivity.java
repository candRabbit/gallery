package com.cn.gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.cn.gallery.activity.GalleryActivity;
import com.cn.gallery.utils.PictureUtils;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

  public final static int CROP = 1;
  public final static int SINGLE = 2;
  public final static int MULTI = 3;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  /**
   * 剪裁模式
   */
  public void toCrop(View view) {
    new Gallery.Builder().builder().start(this, GalleryActivity.Mode.CROP, CROP);
  }

  /**
   * 单选模式
   */
  public void toSingle(View view) {
    new Gallery.Builder().builder().start(this, GalleryActivity.Mode.SINGLE, SINGLE);
  }

  /**
   * 单选模式
   */
  public void toMulti(View view) {
    new Gallery.Builder().builder().start(this, GalleryActivity.Mode.MULTI, MULTI);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && null != data){
      ArrayList<String> photoPaths = data.getStringArrayListExtra(Constant.SELECT_PHOTOS);
      //压缩图片
      PictureUtils.compress(photoPaths).subscribe(new Action1<List<byte[]>>() {
        @Override public void call(List<byte[]> bytes) {
          // TODO: 12/01/2017 上传操作等.
        }
      });
    }

  }
}
