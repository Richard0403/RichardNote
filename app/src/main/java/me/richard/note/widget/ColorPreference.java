package me.richard.note.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import me.richard.note.R;
import me.richard.note.util.ColorUtils;

/**
 * Created by richard on 2017/12/23. */
public class ColorPreference extends Preference {

    private int value;

    public ColorPreference(Context context) {
        super(context);
        initAttrs(null, 0);
    }

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, 0);
    }

    public ColorPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs, defStyle);
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPreference, defStyle, defStyle);
//        isPrimary = a.getBoolean(R.styleable.ColorPreference_primary, true);
        a.recycle();
        setWidgetLayoutResource(R.layout.widget_pref_color_layout);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        CircleImageView previewView = view.findViewById(R.id.color_view);
        previewView.setFillingCircleColor(getValue());
    }

    public void setValue(int value) {
        if (callChangeListener(value)) {
            this.value = value;
            if (isPersistent()) {
                persistInt(value);
            }
            notifyChanged();
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return ColorUtils.parseColor(a.getString(index), Color.parseColor("#FF049372"));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(0) : (Integer) defaultValue);
    }

    public int getValue() {
        return value;
    }
}
