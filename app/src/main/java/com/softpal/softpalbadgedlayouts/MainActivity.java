package com.softpal.softpalbadgedlayouts;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
	
	TabLayout tabLayout = null;
	CustomViewPager customViewPager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabLayout = findViewById(R.id.tabs);
		customViewPager = findViewById(R.id.container);
		tabLayout.setupWithViewPager(customViewPager);
	}
}