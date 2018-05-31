package com.ezhealthtrack.templates.physiotherapist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ezhealthtrack.EzFragment;
import com.ezhealthtrack.R;

public class WebPageFragment extends EzFragment {

	private WebView mWebView;
	protected FragmentActivity mActivity;

	private String mURL;

	void setWebPageURL(String url) {
		mURL = url;
		Log.e("WPF::setWebPageURL()", "URL: " + mURL);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("HF", "onCreate");

		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("HF: ", "onCreateView");

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_web_view, container,
				false);

		final ProgressDialog pd = ProgressDialog.show(mActivity, "",
				"Loading...", true);
		pd.setCancelable(true);

		mWebView = (WebView) view.findViewById(R.id.id_web_view);
		// mWebView.setWebViewClient(new WebViewClient());
		// webView.getSettings().setJavaScriptEnabled(true);
		// mWebView.getSettings().setSupportZoom(true);
		// mWebView.getSettings().setBuiltInZoomControls(true);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}
		});

		mWebView.loadUrl(mURL);

		// mWebView.loadData("<html><body>Hello, world!</body></html>",
		// "text/html", "UTF-8");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * Called when the fragment is visible to the user and actively running.
	 * Resumes the WebView.
	 */
	@Override
	public void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	/**
	 * Called when the fragment is no longer resumed. Pauses the WebView.
	 */
	@Override
	public void onResume() {
		mWebView.onResume();
		super.onResume();
	}

	/**
	 * Called when the WebView has been detached from the fragment. The WebView
	 * is no longer available after this time.
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

}
