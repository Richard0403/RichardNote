package me.richard.note.widget.themed;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 细瘦字体
 */
public class HelveticaThinTextView extends AppCompatTextView {

    public HelveticaThinTextView(Context context) {
        super(context);
        init();
    }

    public HelveticaThinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelveticaThinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Helvetica_Thin.ttf");
        setTypeface(typeface);
    }
}
