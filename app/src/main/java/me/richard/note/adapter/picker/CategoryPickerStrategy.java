package me.richard.note.adapter.picker;

import android.graphics.drawable.Drawable;

import me.richard.note.PalmApp;
import me.richard.note.R;
import me.richard.note.model.Category;
import me.richard.note.util.ColorUtils;

/**
 * Created by shouh on 2018/3/20. */
public class CategoryPickerStrategy implements ModelsPickerStrategy<Category> {

    @Override
    public String getTitle(Category model) {
        return model.getName();
    }

    @Override
    public String getSubTitle(Category model) {
        return PalmApp.getContext().getResources().getQuantityString(
                R.plurals.notes_number, model.getCount(), model.getCount());
    }

    @Override
    public Drawable getIconDrawable(Category model) {
        Drawable drawable =  PalmApp.getDrawableCompact(model.getPortrait().iconRes);
        return ColorUtils.tintDrawable(drawable, model.getColor());
    }

    @Override
    public boolean shouldShowMore() {
        return false;
    }

    @Override
    public boolean isMultiple() {
        return true;
    }
}
