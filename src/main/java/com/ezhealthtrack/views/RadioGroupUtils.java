package com.ezhealthtrack.views;

import java.util.HashMap;

import com.ezhealthtrack.util.Util;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RadioGroupUtils {
	public static void autoSaveRg(final RadioGroup rg,
			final HashMap<String, String> hm, final String key) {
		try {
			if (hm.containsKey(key)&&!Util.isEmptyString(hm.get(key))) {
				((RadioButton) rg.findViewWithTag(hm.get(key)))
						.setChecked(true);
			}else{
				rg.check(-1);
			}
			rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(final RadioGroup group,
						final int checkedId) {
					hm.put(key, rg.findViewById(checkedId).getTag().toString());

				}
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

}
