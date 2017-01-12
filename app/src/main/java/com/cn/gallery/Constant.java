package com.cn.gallery;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import com.cn.gallery.model.PhotoItem;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class Constant {

  public final static String DIR = "dir";
  public final static String POSITION = "position";
  public final static int REQUEST_STORAGE_PERMISSION = 997;
  public final static int REQUEST_CAMERA_PERMISSION = 996;

  public final static String COLUMN = "column";
  public final static String CROP_WIDTH = "cropWidth";
  public final static String CROP_HEIGHT = "cropHeight";
  public final static String MAX_COUNT = "maxCount";
  public final static String MODE = "mode";
  public final static String QUALITY = "quality";
  public final static String SELECT_PHOTOS = "selectPhotos";
}
