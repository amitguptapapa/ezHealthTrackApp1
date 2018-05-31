package com.ezhealthtrack.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ezhealthtrack.R;

public class TeethView extends LinearLayout {

	public TeethView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.tooth_image, this);
	}

	public TeethView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.tooth_image, this);
	}

	public TeethView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.tooth_image, this);
	}

}
