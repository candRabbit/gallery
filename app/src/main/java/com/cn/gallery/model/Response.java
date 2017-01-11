package com.cn.gallery.model;

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
public class Response<T> {
  public long responseTime;
  public int code;
  public String message;
  public T data;
  public int page;
  public int totalCount;
  public List<T> datas;
}
