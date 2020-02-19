package com.softpal.softpalbadgedlayouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 The type Custom view pager.
 */
public class CustomViewPager extends ViewPager
{
	
	private boolean pagingEnabled;
	
	/**
	 Instantiates a new Custom view pager.
	 
	 @param context the context
	 */
	public CustomViewPager(Context context)
	{
		super(context);
	}
	
	/**
	 Instantiates a new Custom view pager.
	 
	 @param context the context
	 @param attrs   the attrs
	 */
	public CustomViewPager(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		this.pagingEnabled = true;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		try
		{
			return pagingEnabled && super.onInterceptTouchEvent(ev);
		}
		catch(IllegalArgumentException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		try
		{
			return pagingEnabled && super.onTouchEvent(ev);
		}
		catch(IllegalArgumentException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 Is paging enabled boolean.
	 
	 @return the boolean
	 */
	public boolean isPagingEnabled()
	{
		return pagingEnabled;
	}
	
	/**
	 Sets paging enabled.
	 
	 @param enabled the enabled
	 */
	public void setPagingEnabled(boolean enabled)
	{
		this.pagingEnabled = enabled;
	}
}