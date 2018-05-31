package com.ezhealthtrack.views;

import java.util.Calendar;
import java.util.HashMap;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.DentistSoap.Model.Task;

public class EditUtils {
	public static void autoSaveEditText(final EditText editText,
			final HashMap<String, String> hm) {
		if (hm.containsKey(editText.getTag().toString())) {
			editText.setText(hm.get(editText.getTag().toString()));
		}
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable s) {
				// TODO Auto-generated method stub
				hm.put(editText.getTag().toString(), s.toString());
			}

			@Override
			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(final CharSequence s, final int start,
					final int before, final int count) {
			}
		});

	}
	
	public static void autoSaveEditText(final EditText editText,
			final HashMap<String, String> hm,final HashMap<String,Task> ht,final String key) {
		if (hm.containsKey(editText.getTag().toString())) {
			editText.setText(hm.get(editText.getTag().toString()));
		}
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(final Editable s) {
				// TODO Auto-generated method stub
				hm.put(editText.getTag().toString(), s.toString());
				if(s.length()==0){
					ht.remove(key);
				}else{
					if(!ht.containsKey(key)){
						Task task = new Task();
						task.setStatus("pending");
						task.setDate(EzApp.sdfyyMmdd
								.format(Calendar.getInstance()
										.getTime()));
						ht.put(key, task);
					}
				}
			}

			@Override
			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(final CharSequence s, final int start,
					final int before, final int count) {
			}
		});

	}

	public static void autoSaveEditTextLayout(final ViewGroup v,
			final HashMap<String, String> hm) {
		for (int i = 0; i < v.getChildCount(); i++) {
			if (v.getChildAt(i) instanceof EditText) {
				final EditText editText = (EditText) v.getChildAt(i);
				EditUtils.autoSaveEditText(editText, hm);
			} else if (v.getChildAt(i) instanceof ViewGroup) {
				EditUtils.autoSaveEditTextLayout((ViewGroup) v.getChildAt(i),
						hm);
			}
		}

	}

}
