/*
 * Copyright (c) 2014, NUIST - 120Lib. All rights reserved.
 */

package nuist.qlib.ccss.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 视图容器控件,阻止拦截ontouch事件传递给其子控件
 * 
 * @author Fang Wang
 * @since ccss 1.0
 */
public class InterceptScrollContainer extends LinearLayout {

	public InterceptScrollContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InterceptScrollContainer(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}
}
