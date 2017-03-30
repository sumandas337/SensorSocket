package graph.sockets.com.sensorsocket.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import graph.sockets.com.sensorsocket.R;


/**
 * Created by sumandas on 29/01/2017.
 */

public class FontTextView extends AppCompatTextView {
    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        String customFont = typedArray.getString(R.styleable.FontTextView_custom_font);
        if(!TextUtils.isEmpty(customFont)) {
            try {
                setTypeface(TypeFaceUtils.get(context, customFont));
            } catch (Exception e) {

            }
        }
        typedArray.recycle();
    }
}
