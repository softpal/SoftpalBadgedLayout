# SoftpalBadgedTabLayout


### Developed by
[Softpal](https://www.github.com/softpal)

**Features**

     1.This Library is used for in a full screenTabLayout provides a horizontal layout to display tabs. 
     2.The layout handles interactions for a group of tabs.
     
 ## Installation

Add repository url and dependency in application module gradle file:
  
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

### Gradle
[![](https://jitpack.io/v/softpal/SoftpalBadgedTabLayout.svg)](https://jitpack.io/#softpal/SoftpalBadgedTabLayout)
```javascript
dependencies {
    implementation 'com.github.softpal:SoftpalBadgedTabLayout:1.0'
}
```
## Usage

Add BadgedTabLayout as if you added TabLayout itself
 
    <com.softpal.softpalbadgedlayout.BadgedTabLayout
            android:id="@+id/tabs_offline"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:badgeBackgroundColor="@color/primaryLightColor"
            app:badgeTextColor="@color/primaryTextColor"
            app:badgeTextSize="@dimen/badge_text_size"
            app:tabIndicatorColor="@color/secondaryColor"
            app:tabSelectedTextColor="@color/secondaryColor"
            app:tabTextColor="@color/secondaryColor">


            <com.google.android.material.tabs.TabItem
                android:id="@+id/tabItem_offline_delivery"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/tab_offline_delivery" />

            <com.google.android.material.tabs.TabItem
                android:id="@+id/tabItem_offline_pod"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/tab_offline_pod" />

        </com.softpal.softpalbadgedlayout.BadgedTabLayout>
        
        
   ### 1. Calling Badged Layout in Your Activity
   
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setBadgeTruncateAt(TextUtils.TruncateAt.MIDDLE);
	   tabLayout.setTabTruncateAt(TextUtils.TruncateAt.MIDDLE);
   


