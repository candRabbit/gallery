package com.cn.gallery.model;

/**
 * Created by linqinglv on 10/01/2017.
 *
 * 内容摘要：
 * 系统版本：
 * 版权所有：宝润兴业
 * 修改内容：
 * 修改日期
 */
public class Request<T> {
  public String token;
  public int page;
  public int count;
  public T requestData;
}
