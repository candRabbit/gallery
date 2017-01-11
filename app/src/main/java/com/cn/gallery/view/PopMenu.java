package com.cn.gallery.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.cn.gallery.R;

/**
 * Created by linqinglv on 16/5/19.
 *
 * 内容摘要：公共的弹出窗
 * 系统版本：
 * 版权所有：
 * 修改内容：
 * 修改日期
 */
public class PopMenu extends FrameLayout {

  Animation outAnimation, inAnimation;
  PullStyle pullStyle = PullStyle.DOWN;

  View contentView;
  PopMenuListener popMenuListener;

  public boolean isOpen = false;

  public PopMenu(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PopMenu);
    pullStyle = PullStyle.getStyle(typedArray.getInteger(R.styleable.PopMenu_style, 0x0));
    typedArray.recycle();
    setBackgroundColor(getResources().getColor(R.color.color_half));
    initAnimation();
  }

  public void setPullStyle(PullStyle pullStyle) {
    this.pullStyle = pullStyle;
  }

  public PopMenu(Context context) {
    super(context);
    setBackgroundColor(getResources().getColor(R.color.color_half));
    initAnimation();
  }

  public void setPopMenuListener(PopMenuListener popMenuListener) {
    this.popMenuListener = popMenuListener;
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if (getChildCount() != 1) {
      throw new RuntimeException("must one child");
    }
    contentView = getChildAt(0);
  }

  private void initAnimation() {
    switch (pullStyle) {
      case DOWN:
        inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pop_in);
        outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pop_out);
        break;
      case UP:
        inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_in);
        outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_out);
        break;
    }

    inAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {

      }

      @Override public void onAnimationEnd(Animation animation) {
        isOpen = true;
        if (null != popMenuListener) {
          popMenuListener.onOpenEnd();
        }
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    });

    outAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {

      }

      @Override public void onAnimationEnd(Animation animation) {
        isOpen = false;
        setVisibility(GONE);
        if (null != popMenuListener) {
          popMenuListener.onCloseEnd();
        }
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    });
  }

  public void setContentView(View contentView) {
    this.contentView = contentView;
  }

  public void show() {
    initAnimation();
    setVisibility(VISIBLE);
    contentView.clearAnimation();
    contentView.setAnimation(inAnimation);
  }

  public void close() {
    initAnimation();
    contentView.clearAnimation();
    contentView.setAnimation(outAnimation);
  }

  public enum PullStyle {
    DOWN(0X0), UP(0x1);
    int type;

    PullStyle(int type) {
      this.type = type;
    }

    public static PullStyle getStyle(int type) {
      switch (type) {
        case 0x0:
          return DOWN;
        case 0x1:
          return UP;
      }
      return null;
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      close();
    }
    return true;
  }

  public interface PopMenuListener {
    void onOpenEnd();

    void onCloseEnd();
  }
}
