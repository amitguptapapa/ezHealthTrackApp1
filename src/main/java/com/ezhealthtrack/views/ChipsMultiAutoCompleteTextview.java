package com.ezhealthtrack.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezhealthtrack.R;
import com.ezhealthtrack.model.PatientAutoSuggest;
import com.tokenautocomplete.TokenCompleteTextView;

public class ChipsMultiAutoCompleteTextview extends TokenCompleteTextView {

    public ChipsMultiAutoCompleteTextview(Context context) {
        super(context);
    }

    public ChipsMultiAutoCompleteTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChipsMultiAutoCompleteTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(Object object) {
        PatientAutoSuggest p = (PatientAutoSuggest)object;

        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = l.inflate(R.layout.chips_edittext, (ViewGroup)ChipsMultiAutoCompleteTextview.this.getParent(), false);
        ((TextView)view.findViewById(R.id.edtTxt1)).setText(p.getName());

        return view;
    }

    @Override
    protected Object defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        return new PatientAutoSuggest("0", completionText);
    }
}
//extends MultiAutoCompleteTextView implements OnItemClickListener {
//
//	private final String TAG = "ChipsMultiAutoCompleteTextview";
//	public ArrayList<PatientAutoSuggest> arrpat = new ArrayList<PatientAutoSuggest>();
//	
//	/* Constructor */
//	public ChipsMultiAutoCompleteTextview(Context context) {
//		super(context);
//		init(context);
//	}
//	/* Constructor */
//	public ChipsMultiAutoCompleteTextview(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init(context);
//	}
//	/* Constructor */
//	public ChipsMultiAutoCompleteTextview(Context context, AttributeSet attrs,
//			int defStyle) {
//		super(context, attrs, defStyle);
//		init(context);
//	}
//	/* set listeners for item click and text change */
//	public void init(Context context){
//		setOnItemClickListener(this);
//		addTextChangedListener(textWather);
//		setMovementMethod(LinkMovementMethod.getInstance());
//	}
//	/*TextWatcher, If user type any country name and press comma then following code will regenerate chips */
//	private TextWatcher textWather = new TextWatcher() {
//		String str;
//		
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//			Log.i(TAG, s+" "+start+" "+before+" "+count);
//			if(count == 0&&str.length()>0&&str.charAt(str.length()-1)==','){
//				String chips[] = str.trim().split(",");
//				str = "";
//				for(int i =0;i<chips.length-1;i++){
//					str = str+chips[i]+",";
//				}
//				setText(str);
//				setChips();
//			}
//			if(count >=1){
//				if(s.charAt(start) == ',')
//					setChips(); // generate chips
//			}
//		}
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
//			str = s.toString();
//		}
//		@Override
//		public void afterTextChanged(Editable s) {}
//	};
//	/*This function has whole logic for chips generate*/
//	public void setChips(){
//		if(getText().toString().contains(",")) // check comman in string
//		{
//			
//			SpannableStringBuilder ssb = new SpannableStringBuilder(getText().toString());
//			// split string wich comma
//			String chips[] = getText().toString().trim().split(",");
//			int x =0;
//			int pos = 0;
//			// loop will generate ImageSpan for every country name separated by comma
//			for(final String c : chips){
//				// inflate chips_edittext layout 
//				LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//				View v = lf.inflate(R.layout.chips_edittext, null);
//				TextView textView = (TextView) v.findViewById(R.id.edtTxt1);
//				textView.setText(c); // set text
//				//textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.india, 0); // set flag image
//				// capture bitmapt of genreated textview
//				int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//				v.measure(spec, spec);
//				v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
//				Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),Bitmap.Config.ARGB_8888);
//				Canvas canvas = new Canvas(b);
//				canvas.translate(-v.getScrollX(), -v.getScrollY());
//				v.draw(canvas);
//				v.setDrawingCacheEnabled(true);
//				Bitmap cacheBmp = v.getDrawingCache();
//				Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
//				v.destroyDrawingCache();  // destory drawable
//				// create bitmap drawable for imagespan
//				BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
//				bmpDrawable.setBounds(0, 0,bmpDrawable.getIntrinsicWidth(),bmpDrawable.getIntrinsicHeight());
//				// create and set imagespan 
//				final int x1 = x;
//				final int pos1 = pos;
//				//ssb.setSpan(new ImageSpan(bmpDrawable),x ,x + c.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				ssb.setSpan(new ClickableSpan() {
//					
//					@Override
//					public void onClick(View widget) {
//						Log.i("", "text clicked");
//						CharSequence cs = getText().toString().replace(c+",","");
//						try{
//						if(cs.length()>0&&cs.charAt(0) == ' ')
//							cs = cs.subSequence(1, cs.length()-1);
//						}catch(Exception e){
//							
//						}
//						setText(cs);
//						setChips();
//						arrpat.remove(pos1);
//						Log.i("", new Gson().toJson(arrpat));
//						
//					}
//				},x ,x + c.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				
//				x = x+ c.length() +1;
//				pos = pos+1;
//			}
//			// set chips span 
//			setText(ssb);
//			// move cursor to last 
//			setSelection(getText().length());
//		}
//		
//		
//	}
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		arrpat.add((PatientAutoSuggest) getAdapter().getItem(position));
//		setChips(); // call generate chips when user select any item from auto complete
//	}
//	
//	public static Tokenizer getTokenizer() {
//		return new MultiAutoCompleteTextView.Tokenizer() {
//
//			@Override
//			public int findTokenEnd(final CharSequence text, final int cursor) {
//				int i = cursor;
//				final int len = text.length();
//
//				while (i < len) {
//					if (text.charAt(i) == ',') {
//						return i;
//					} else {
//						i++;
//					}
//				}
//
//				return len;
//			}
//
//			@Override
//			public int findTokenStart(final CharSequence text, final int cursor) {
//				int i = cursor;
//
//				while ((i > 0) && (text.charAt(i - 1) != ',')) {
//					i--;
//				}
//				while ((i < cursor) && (text.charAt(i) == ' ')) {
//					i++;
//				}
//
//				return i;
//			}
//
//			@Override
//			public CharSequence terminateToken(final CharSequence text) {
//				int i = text.length();
//
//				while ((i > 0) && (text.charAt(i - 1) == ' ')) {
//					i--;
//				}
//
//				if ((i > 0) && (text.charAt(i - 1) == ',')) {
//					return text + ",";
//				} else {
//					if (text instanceof Spanned) {
//						final SpannableString sp = new SpannableString(text);
//						android.text.TextUtils.copySpansFrom((Spanned) text, 0,
//								text.length(), Object.class, sp, 0);
//						return sp + ",";
//					} else {
//						return text + ",";
//					}
//				}
//			}
//		};
//
//	}
//}
