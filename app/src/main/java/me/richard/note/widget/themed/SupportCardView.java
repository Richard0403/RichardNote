package me.richard.note.widget.themed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import me.richard.note.util.ColorUtils;

/**
 * Created by Richard on 2018/3/1. */
public class SupportCardView extends CardView {

    public SupportCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public SupportCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SupportCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Set the foreground only for above lollipop */
    private void init() {
        ColorUtils.addRipple(this);
    }
}
