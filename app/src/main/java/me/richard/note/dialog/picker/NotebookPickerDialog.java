package me.richard.note.dialog.picker;

import android.support.v7.app.AlertDialog;

import java.util.List;

import me.richard.note.R;
import me.richard.note.adapter.ModelsPickerAdapter;
import me.richard.note.adapter.picker.NotebookPickerStrategy;
import me.richard.note.model.Notebook;
import me.richard.note.provider.NotebookStore;
import me.richard.note.provider.schema.NotebookSchema;
import me.richard.note.util.ColorUtils;
import me.richard.note.widget.EmptyView;

/**
 * Created by wangshouheng on 2017/10/5.*/
public class NotebookPickerDialog extends BasePickerDialog<Notebook> {

    public static NotebookPickerDialog newInstance() {
        return new NotebookPickerDialog();
    }

    @Override
    protected ModelsPickerAdapter<Notebook> prepareAdapter() {
        return new ModelsPickerAdapter<>(getNotebooks(), new NotebookPickerStrategy(getContext()));
    }

    private List<Notebook> getNotebooks() {
        return NotebookStore.getInstance(getContext()).getNotebooks(null, NotebookSchema.ADDED_TIME + " DESC ");
    }

    @Override
    protected void onCreateDialog(AlertDialog.Builder builder, EmptyView emptyView) {
        builder.setTitle(getString(R.string.pick_notebook));
        builder.setPositiveButton(R.string.text_cancel, null);
        emptyView.setTitle(getString(R.string.no_notebook_available));
        emptyView.setIcon(ColorUtils.tintDrawable(
                getContext().getResources().getDrawable(R.drawable.ic_folder_black_24dp), getImageTintColor()));
    }

    private int getImageTintColor() {
        return getContext().getResources().getColor(ColorUtils.isDarkTheme(getContext())
                ? R.color.dark_theme_empty_icon_tint_color : R.color.light_theme_empty_icon_tint_color);
    }
}
