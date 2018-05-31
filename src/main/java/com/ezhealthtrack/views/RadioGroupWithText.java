package com.ezhealthtrack.views;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ezhealthtrack.util.Util;

public class RadioGroupWithText {
	private TextView txt = null;
	private RadioGroup rg = null;
	private final ArrayList<String> colors;
	HashMap<String, String> hashState;

	public RadioGroupWithText(final Context context, final View txt,
			final View rg, final int selectedRb,
			final ArrayList<String> colors,
			final HashMap<String, String> hashState) {
		this.txt = (TextView) txt;
		this.rg = (RadioGroup) rg;
		this.colors = colors;
		this.hashState = hashState;
		load();
	}

	private void load() {
		if (Util.isEmptyString(hashState.get(rg.getTag()))
				|| hashState.get(rg.getTag()).equals("A")) {
			rg.check(rg.getChildAt(1).getId());
			txt.setBackgroundColor(Color.parseColor(colors.get(1)));
		} else {
			rg.check(rg.getChildAt(0).getId());
			txt.setBackgroundColor(Color.parseColor(colors.get(0)));
		}
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(final RadioGroup group,
					final int checkedId) {
				txt.setBackgroundColor(Color.parseColor(colors.get(group
						.indexOfChild(group.findViewById(checkedId)))));
				if (group.indexOfChild(group.findViewById(checkedId)) == 0) {
					hashState.put(rg.getTag().toString(), "P");
				} else {
					hashState.put(rg.getTag().toString(), "A");
				}

			}
		});

	}
}
