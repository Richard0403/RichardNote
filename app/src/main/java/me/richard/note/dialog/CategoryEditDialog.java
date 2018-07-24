package me.richard.note.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.color.ColorChooserDialog;

import me.richard.note.R;
import me.richard.note.activity.base.CommonActivity;
import me.richard.note.model.Category;
import me.richard.note.model.enums.Portrait;
import me.richard.note.util.ColorUtils;
import me.richard.note.util.ToastUtils;
import me.richard.note.widget.CircleImageView;
import me.richard.note.widget.WatcherTextView;

/**
 * Created by wangshouheng on 2017/4/2.*/
public class CategoryEditDialog extends DialogFragment {

    private OnConfirmCategoryEditListener onConfirmCategoryEditListener;

    private EditText etCategoryName;
    private LinearLayout llNameEditBG;
    private CircleImageView civCategoryColor, civPortraitBG;
    private ImageView ivPortrait;

    private int primaryColor;
    private int iCategoryColor;

    private Category category;

    public static CategoryEditDialog newInstance(Category category, OnConfirmCategoryEditListener listener) {
        CategoryEditDialog dialog = new CategoryEditDialog();
        dialog.setOnConfirmCategoryEditListener(listener);
        dialog.setCategory(category);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        primaryColor = ColorUtils.primaryColor(getContext());

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_category_editor_layout, null);

        etCategoryName = rootView.findViewById(R.id.et_category_name);
        llNameEditBG = rootView.findViewById(R.id.ll_title_background);
        civCategoryColor = rootView.findViewById(R.id.civ_category_color);
        civPortraitBG = rootView.findViewById(R.id.civ_portrait_background);
        ivPortrait = rootView.findViewById(R.id.iv_portrait);

        rootView.findViewById(R.id.ll_category_color).setOnClickListener(v -> showColorPickerDialog());
        rootView.findViewById(R.id.fl_category_portrait).setOnClickListener(v -> showPortraitPickerDialog());

        WatcherTextView wtv = rootView.findViewById(R.id.wtv);
        wtv.bindEditText(etCategoryName);

        etCategoryName.setText(category.getName());
        updateUIBySelectedColor(category.getColor());
        ivPortrait.setImageResource(category.getPortrait().iconRes);

        return new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    if (TextUtils.isEmpty(etCategoryName.getText())){
                        ToastUtils.makeToast(R.string.title_required);
                        return;
                    }
                    category.setName(etCategoryName.getText().toString());
                    if (onConfirmCategoryEditListener != null){
                        onConfirmCategoryEditListener.onConfirmCategory(category);
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private void setCategory(Category category) {
        this.category = category;
    }

    public void updateUIBySelectedColor(int color) {
        iCategoryColor = color;
        category.setColor(color);
        llNameEditBG.setBackgroundColor(color);
        civPortraitBG.setFillingCircleColor(color);
        civCategoryColor.setFillingCircleColor(color);
    }

    private void showPortraitPickerDialog() {
        String SHOW_PORTRAIT_DIALOG = "SHOW_PORTRAIT_DIALOG";
        PortraitPickerDialog.newInstance(iCategoryColor, (portraitId, portraitRes) -> {
            category.setPortrait(Portrait.getPortraitById(portraitId));
            ivPortrait.setImageResource(portraitRes);
        }).show(getFragmentManager(), SHOW_PORTRAIT_DIALOG);
    }

    private void showColorPickerDialog() {
        assert getActivity() != null;
        new ColorChooserDialog.Builder((CommonActivity) getActivity(), R.string.pick_tag_color)
                .preselect(primaryColor)
                .accentMode(false)
                .titleSub(R.string.pick_tag_color)
                .backButton(R.string.back)
                .doneButton(R.string.done_label)
                .cancelButton(R.string.cancel)
                .show();
    }

    public void setOnConfirmCategoryEditListener(OnConfirmCategoryEditListener listener) {
        this.onConfirmCategoryEditListener = listener;
    }

    public interface OnConfirmCategoryEditListener {
        void onConfirmCategory(Category category);
    }
}
