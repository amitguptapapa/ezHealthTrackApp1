package com.ezhealthtrack.views;

import java.util.HashMap;

import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ezhealthtrack.util.Util;

public class TextUtils {
	public static void autoSaveTextView(final TextView editText,
			final HashMap<String, String> hm) {
		if (hm.containsKey(editText.getTag().toString())) {
			editText.setText(hm.get(editText.getTag().toString()));
		}else{
			if(!(editText instanceof RadioButton))
			editText.setText("");
		}
	}

	public static void autoSaveTextViewLayout(final ViewGroup v,
			final HashMap<String, String> hm) {
		for (int i = 0; i < v.getChildCount(); i++) {
			if (v.getChildAt(i) instanceof TextView) {
				final TextView editText = (TextView) v.getChildAt(i);
				if (!Util.isEmptyString((String) editText.getTag())) {
					TextUtils.autoSaveTextView(editText, hm);
				}
			} else if (v.getChildAt(i) instanceof ViewGroup) {
				TextUtils.autoSaveTextViewLayout((ViewGroup) v.getChildAt(i),
						hm);
			}
		}

	}

}
