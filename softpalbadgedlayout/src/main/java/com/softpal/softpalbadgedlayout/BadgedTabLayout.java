package com.softpal.softpalbadgedlayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;


/**
 BadgedTabLayout extends {@link TabLayout} class with additional functionality of adding small badges
 near titles. This is useful when you want to show specific tab information (like new messages, or
 searched result count etc)
 <p>
 Tab text color can be updated using TabLayout methods Badges are also fully customizable
 */
public class BadgedTabLayout extends TabLayout
{
	
	private static final String TAG = "BadgedTabLayout";
	/**
	 The Badge background colors.
	 */
	protected ColorStateList badgeBackgroundColors;
	/**
	 The Badge text colors.
	 */
	protected ColorStateList badgeTextColors;
	/**
	 The Badge text size.
	 */
	protected float badgeTextSize = 0;
	/**
	 The Tab text size.
	 */
	protected float tabTextSize = 0;
	/**
	 The Tab font.
	 */
	protected Typeface tabFont = null;
	/**
	 The Badge font.
	 */
	protected Typeface badgeFont = null;
	/**
	 The Tab truncate at.
	 */
	protected TextUtils.TruncateAt tabTruncateAt = null;
	/**
	 The Badge truncate at.
	 */
	protected TextUtils.TruncateAt badgeTruncateAt = null;
	/**
	 The Is span text.
	 */
	protected boolean isSpanText = false;
	/**
	 The Max width text.
	 */
	protected int maxWidthText = - 1;
	private Context mContext;
	private AppCompatActivity mActivity;
	
	/**
	 Instantiates a new Badged tab layout.
	 
	 @param context the context
	 @param attrs   the attrs
	 */
	public BadgedTabLayout(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		this.mContext = context;
		//	this.mActivity = (AppCompatActivity)context;
		//set default colors from resources
		badgeBackgroundColors = ContextCompat.getColorStateList(context,R.color.primaryLightColor);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.BadgedTabLayout,0,0);
		
		badgeTextColors = getContextColors();
		
		try
		{
			
			// If we have an explicit text color set, use it instead
			if(a.hasValue(R.styleable.BadgedTabLayout_badgeBackgroundColor))
			{
				badgeBackgroundColors = a.getColorStateList(R.styleable.BadgedTabLayout_badgeBackgroundColor);
			}
			
			if(a.hasValue(R.styleable.BadgedTabLayout_badgeTextColor))
			{
				badgeTextColors = a.getColorStateList(R.styleable.BadgedTabLayout_badgeTextColor);
			}
			
			if(a.hasValue(R.styleable.BadgedTabLayout_badgeTextSize))
			{
				badgeTextSize = a.getDimension(R.styleable.BadgedTabLayout_badgeTextSize,0);
			}
			
			if(a.hasValue(R.styleable.BadgedTabLayout_tabTextSize))
			{
				tabTextSize = a.getDimension(R.styleable.BadgedTabLayout_tabTextSize,getResources().getDimension(R.dimen.tab_text_size));
			}
			
			// We have an explicit selected text color set, so we need to make merge it with the
			// current colors. This is exposed so that developers can use theme attributes to set
			// this (theme attrs in ColorStateLists are Lollipop+)
			
			if(a.hasValue(R.styleable.BadgedTabLayout_badgeSelectedBackgroundColor))
			{
				final int selected = a.getColor(R.styleable.BadgedTabLayout_badgeSelectedBackgroundColor,0);
				badgeBackgroundColors = createColorStateList(badgeBackgroundColors.getDefaultColor(),selected);
			}
			
			if(a.hasValue(R.styleable.BadgedTabLayout_badgeSelectedTextColor))
			{
				final int selected = a.getColor(R.styleable.BadgedTabLayout_badgeSelectedTextColor,0);
				badgeTextColors = createColorStateList(badgeTextColors.getDefaultColor(),selected);
			}
		}
		finally
		{
			a.recycle();
		}
	}
	
	/**
	 takes primary and primaryDark colors from context
	 
	 @return {@link ColorStateList} object, with primary color at selected state and primaryDark on
	 unselected state
	 */
	private ColorStateList getContextColors()
	{
		TypedValue typedValue = new TypedValue();
		TypedArray a = getContext().obtainStyledAttributes(typedValue.data,new int[]{R.attr.colorPrimary,R.attr.colorPrimaryDark});
		int primaryColor = a.getColor(0,0);
		int primaryDarkColor = a.getColor(1,0);
		a.recycle();
		return createColorStateList(primaryDarkColor,primaryColor);
	}
	
	/**
	 Creates color states list out of two given params
	 
	 @param defaultColor  color for state_selected = false
	 @param selectedColor color for state_selected = true
	 
	 @return {@link ColorStateList} object
	 */
	private static ColorStateList createColorStateList(int defaultColor,int selectedColor)
	{
		final int[][] states = new int[2][];
		final int[] colors = new int[2];
		int i = 0;
		states[i] = SELECTED_STATE_SET;
		colors[i] = selectedColor;
		i++;
		// Default enabled state
		states[i] = EMPTY_STATE_SET;
		colors[i] = defaultColor;
		i++;
		return new ColorStateList(states,colors);
	}
	
	/**
	 Gets badge background colors.
	 
	 @return the badge background colors
	 */
	public ColorStateList getBadgeBackgroundColors()
	{
		return badgeBackgroundColors;
	}
	
	/**
	 sets badge background color
	 
	 @param badgeBackgroundColors state color list for badge background (selected/unselected)
	 */
	public void setBadgeBackgroundColors(ColorStateList badgeBackgroundColors)
	{
		this.badgeBackgroundColors = badgeBackgroundColors;
		updateTabViews();
	}
	
	/**
	 Invalidates the tab views
	 */
	public void updateTabViews()
	{
		for(int i = 0;i < getTabCount();i++)
		{
			TabLayout.Tab tab = getTabAt(i);
			
			if(tab != null)
			{
				tab.setCustomView(makeCustomView(tab,R.layout.badged_tab));
			}
		}
	}
	
	/**
	 Creates new view for the tab from custom layout, look at {@link R.layout#badged_tab}
	 
	 @param tab   the tab for which new custom view will be created; required to extract title
	 @param resId layout id which is used to inflate new appearance of the tab
	 
	 @return new customized view of the tab
	 */
	private View makeCustomView(TabLayout.Tab tab,int resId)
	{
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = tab.getCustomView() == null ? inflater.inflate(resId,null,false) : tab.getCustomView();
		
		makeCustomTitle(tab,view);
		
		makeCustomIcon(tab,view);
		
		makeBadge(view);
		
		return view;
	}
	
	/**
	 @param tab  for which custom title is created
	 @param view custom view, manually inflated from badged_tab.xml
	 */
	private void makeCustomTitle(Tab tab,View view)
	{
		TextView title = view.findViewById(R.id.textview_tab_title);
		
		title.setTextColor(getTabTextColors());
		
		if(tabTextSize != 0)
		{
			title.setTextSize(TypedValue.COMPLEX_UNIT_PX,tabTextSize);
		}
		
		if(tabTruncateAt != null)
		{
			title.setEllipsize(tabTruncateAt);
		}
		
		if(tabFont != null)
		{
			title.setTypeface(tabFont);
		}
		
		if(isSpanText)
		{
			title.setSingleLine(false);
			title.setMarqueeRepeatLimit(- 1);
			title.setEllipsize(TextUtils.TruncateAt.MIDDLE);
		}
		
		if(maxWidthText != - 1)
		{
			title.setMaxWidth(maxWidthText);
		}
		
		if(! TextUtils.isEmpty(tab.getText()))
		{
			CharSequence tabText = tab.getText();
			if(tabText != null)
			{
				title.setText(tabText.toString());
			}
			
		}
		else
		{
			title.setVisibility(GONE);
		}
	}
	
	/**
	 @param tab  for which custom icon is created
	 @param view custom view created from badged_tab.xml
	 */
	private void makeCustomIcon(Tab tab,View view)
	{
		if(tab.getIcon() == null)
		{
			return;
		}
		
		ImageView icon = view.findViewById(R.id.imageview_tab_icon);
		
		DrawableCompat.setTintList(tab.getIcon(),getTabTextColors());
		
		icon.setImageDrawable(tab.getIcon());
		
		icon.setVisibility(VISIBLE);
	}
	
	/**
	 @param view custom view, manually inflated from badged_tab.xml
	 */
	private void makeBadge(View view)
	{
		TextView badge = view.findViewById(R.id.textview_tab_badge);
		badge.setTextColor(badgeTextColors);
		
		if(badgeTruncateAt != null)
		{
			badge.setEllipsize(badgeTruncateAt);
		}
		
		if(badgeFont != null)
		{
			badge.setTypeface(badgeFont);
		}
		
		if(badgeTextSize != 0)
		{
			badge.setTextSize(TypedValue.COMPLEX_UNIT_PX,badgeTextSize);
		}
		
		DrawableCompat.setTintList(badge.getBackground(),badgeBackgroundColors);
	}
	
	/**
	 Gets tab text size.
	 
	 @return the tab text size
	 */
	public float getTabTextSize()
	{
		return tabTextSize;
	}
	
	/**
	 Sets tab text size.
	 
	 @param tabTextSize in pixels
	 */
	public void setTabTextSize(float tabTextSize)
	{
		this.tabTextSize = tabTextSize;
		updateTabViews();
	}
	
	/**
	 Sets tab text size.
	 
	 @param dimensionRes resource value of dimension ex: R.dimen.example
	 */
	public void setTabTextSize(@DimenRes int dimensionRes)
	{
		this.tabTextSize = getResources().getDimension(dimensionRes);
		updateTabViews();
	}
	
	/**
	 Sets max width text.
	 
	 @param maxWidthText in pixels
	 */
	public void setMaxWidthText(int maxWidthText)
	{
		this.maxWidthText = maxWidthText;
		updateTabViews();
	}
	
	/**
	 Is span text.
	 
	 @param isSpanText in boolean
	 */
	public void isSpanText(boolean isSpanText)
	{
		this.isSpanText = isSpanText;
		updateTabViews();
	}
	
	/**
	 Gets badge text size.
	 
	 @return the badge text size
	 */
	public float getBadgeTextSize()
	{
		return badgeTextSize;
	}
	
	/**
	 Sets badge text size.
	 
	 @param badgeTextSize in pixels
	 */
	public void setBadgeTextSize(float badgeTextSize)
	{
		this.badgeTextSize = badgeTextSize;
		updateTabViews();
	}
	
	/**
	 Gets tab font.
	 
	 @return the tab font
	 */
	public Typeface getTabFont()
	{
		return tabFont;
	}
	
	/**
	 Sets tab font.
	 
	 @param tabFont the tab font
	 */
	public void setTabFont(Typeface tabFont)
	{
		this.tabFont = tabFont;
		updateTabViews();
	}
	
	/**
	 Gets badge font.
	 
	 @return the badge font
	 */
	public Typeface getBadgeFont()
	{
		return badgeFont;
	}
	
	/**
	 Sets badge font.
	 
	 @param badgeFont the badge font
	 */
	public void setBadgeFont(Typeface badgeFont)
	{
		this.badgeFont = badgeFont;
		updateTabViews();
	}
	
	/**
	 Gets badge text colors.
	 
	 @return the badge text colors
	 */
	public ColorStateList getBadgeTextColors()
	{
		return badgeTextColors;
	}
	
	/**
	 sets badge text color
	 
	 @param badgeTextColors state color list for badge text (selected/unselected)
	 */
	public void setBadgeTextColors(ColorStateList badgeTextColors)
	{
		this.badgeTextColors = badgeTextColors;
		updateTabViews();
	}
	
	/**
	 Gets tab truncate at.
	 
	 @return the tab truncate at
	 */
	public TextUtils.TruncateAt getTabTruncateAt()
	{
		return tabTruncateAt;
	}
	
	/**
	 Sets tab truncate at.
	 
	 @param tabTruncateAt the tab truncate at
	 */
	public void setTabTruncateAt(TextUtils.TruncateAt tabTruncateAt)
	{
		this.tabTruncateAt = tabTruncateAt;
		updateTabViews();
	}
	
	/**
	 Gets badge truncate at.
	 
	 @return the badge truncate at
	 */
	public TextUtils.TruncateAt getBadgeTruncateAt()
	{
		return badgeTruncateAt;
	}
	
	/**
	 Sets badge truncate at.
	 
	 @param badgeTruncateAt the badge truncate at
	 */
	public void setBadgeTruncateAt(TextUtils.TruncateAt badgeTruncateAt)
	{
		this.badgeTruncateAt = badgeTruncateAt;
		updateTabViews();
	}
	
	@Override
	public void addTab(@NonNull Tab tab,int position,boolean setSelected)
	{
		super.addTab(tab,position,setSelected);
		onTabAdded(tab);
	}
	
	/**
	 On tab added.
	 
	 @param tab the tab
	 */
	public void onTabAdded(Tab tab)
	{
		if(tab == null)
		{
			return;
		}
		
		tab.setCustomView(makeCustomView(tab,R.layout.badged_tab));
		
		if(tab.getCustomView() != null)
		{
			((TextView)(tab.getCustomView().findViewById(R.id.textview_tab_title))).setMaxWidth(1080 / 2);
		}
	}
	
	/**
	 Sets icon.
	 
	 @param position of tab where icon need to be set
	 @param resourse drawable resourse of vector icon
	 */
	public void setIcon(int position,@DrawableRes int resourse)
	{
		Tab tab = getTabAt(position);
		
		if(tab == null)
		{
			return;
		}
		tab.setIcon(resourse);
		
		makeCustomIcon(tab,tab.getCustomView());
	}
	
	/**
	 Sets badge text.
	 
	 @param index of tab where badge should be added
	 @param text  the text of the badge (null to hide the badge)
	 */
	public void setBadgeText(int index,@Nullable String text)
	{
		Tab tab = getTabAt(index);
		if(tab == null || tab.getCustomView() == null)
		{
			return;
		}
		
		TextView badge = tab.getCustomView().findViewById(R.id.textview_tab_badge);
		TextView tabText = tab.getCustomView().findViewById(R.id.textview_tab_title);
		
		if(text == null)
		{
			badge.setVisibility(View.GONE);
			tabText.setMaxWidth(Integer.MAX_VALUE);
		}
		else
		{
			badge.setText(text);
			badge.setVisibility(View.VISIBLE);
			tabText.setMaxWidth(1080 / 2);
		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			TransitionManager.beginDelayedTransition((ViewGroup)tab.getCustomView());
		}
	}
}
