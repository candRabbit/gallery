package com.cn.gallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.cn.gallery.activity.GalleryActivity;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void toCrop(View view) {
    new Gallery.Builder().builder().start(this, GalleryActivity.Mode.CROP, 1111);
  }

  public void toSingle(View view) {
    new Gallery.Builder().builder().start(this, GalleryActivity.Mode.SINGLE, 2222);
  }

  public void toMulti(View view) {
    new Gallery.Builder().builder().start(this, GalleryActivity.Mode.MULTI, 2222);
  }
}
