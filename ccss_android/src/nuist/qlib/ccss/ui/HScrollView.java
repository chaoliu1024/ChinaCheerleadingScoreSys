/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * 自定义滚动控件,重载 onScrollChanged,监听每次的变化通知给观察者
 * 可使用AddOnScrollChangedListener订阅本控件的滚动条变化
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class HScrollView extends HorizontalScrollView {
	ScrollViewObserver mScrollViewObserver = new ScrollViewObserver();

	public HScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HScrollView(Context context) {
		super(context);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		// 当滚动条移动后，引发 滚动事件。通知给观察者，观察者会传达给其他的。
		if (mScrollViewObserver != null) {
			mScrollViewObserver.NotifyOnScrollChanged(l, t, oldl, oldt);
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	/**
	 * 订阅 本控件 的 滚动条变化事件
	 */
	public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
		mScrollViewObserver.AddOnScrollChangedListener(listener);
	}

	/**
	 * 取消 订阅 本控件 的 滚动条变化事件
	 */
	public void RemoveOnScrollChangedListener(OnScrollChangedListener listener) {
		mScrollViewObserver.RemoveOnScrollChangedListener(listener);
	}

	/**
	 * 当发生了滚动事件时
	 */
	public static interface OnScrollChangedListener {
		public void onScrollChanged(int l, int t, int oldl, int oldt);
	}

	/**
	 * 观察者
	 */
	public static class ScrollViewObserver {
		List<OnScrollChangedListener> mList;

		public ScrollViewObserver() {
			super();
			mList = new ArrayList<OnScrollChangedListener>();
		}

		public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
			mList.add(listener);
		}

		public void RemoveOnScrollChangedListener(
				OnScrollChangedListener listener) {
			mList.remove(listener);
		}

		public void NotifyOnScrollChanged(int l, int t, int oldl, int oldt) {
			if (mList == null || mList.size() == 0) {
				return;
			}
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i) != null) {
					mList.get(i).onScrollChanged(l, t, oldl, oldt);
				}
			}
		}
	}
}
