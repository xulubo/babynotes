package cn.year11.babynote.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Toast;

public class ClickableStringBuilder extends SpannableStringBuilder {

	Spanned sp;
	Context mContext;
	public ClickableStringBuilder(Context context, String htmlText) 
	{
		super(Html.fromHtml(htmlText));
		mContext = context;
        Spanned sp = Html.fromHtml(htmlText);  
        URLSpan[] urls = sp.getSpans(0, htmlText.length(), URLSpan.class);  

        for (URLSpan url : urls) {  
            MyURLSpan myURLSpan = new MyURLSpan(url.getURL());  
            setSpan(myURLSpan, sp.getSpanStart(url),  
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);  
        }  
	}
	
    private class MyURLSpan extends ClickableSpan {  
    	  
        private String mUrl;  
  
        MyURLSpan(String url) {  
            mUrl = url;  
        }  
  
        @Override  
        public void onClick(View widget) {  
        	Toast.makeText(mContext, mUrl, Toast.LENGTH_SHORT);
        }  
    }  
}
