package me.richard.note.adapter.picker;

import android.graphics.drawable.Drawable;

import me.richard.note.model.Model;


/**
 * Created by Richard on 2017/10/5.*/
public interface ModelsPickerStrategy<T extends Model> {

    String getTitle(T model);

    String getSubTitle(T model);

    Drawable getIconDrawable(T model);

    boolean shouldShowMore();

    boolean isMultiple();
}
