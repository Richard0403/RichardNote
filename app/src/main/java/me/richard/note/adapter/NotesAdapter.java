package me.richard.note.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import me.richard.note.PalmApp;
import me.richard.note.R;
import me.richard.note.config.Constants;
import me.richard.note.fragment.base.BaseFragment;
import me.richard.note.model.Note;
import me.richard.note.model.Notebook;
import me.richard.note.util.ColorUtils;
import me.richard.note.util.FileHelper;
import me.richard.note.util.FormatUtils;
import me.richard.note.util.StringUtils;
import me.richard.note.util.TimeUtils;
import me.richard.note.util.preferences.NotePreferences;
import me.richard.note.widget.tools.BubbleTextGetter;

/**
 * Created by richard on 2017/12/23.*/
public class NotesAdapter extends BaseMultiItemQuickAdapter<NotesAdapter.MultiItem, BaseViewHolder> implements
        BubbleTextGetter {

    private Context context;
    private BaseFragment baseFragment;

    private int accentColor;
    private boolean isDarkTheme;
    private boolean isExpanded;

    public NotesAdapter(BaseFragment baseFragment, List<NotesAdapter.MultiItem> data) {
        super(data);
        this.baseFragment = baseFragment;
        this.isExpanded = NotePreferences.getInstance().isNoteExpanded();
        this.context = baseFragment.getContext();
        addItemType(MultiItem.ITEM_TYPE_NOTE, isExpanded ? R.layout.item_note_expanded : R.layout.item_note);
        addItemType(MultiItem.ITEM_TYPE_NOTEBOOK, R.layout.item_note);

        accentColor = ColorUtils.accentColor(context);
        isDarkTheme = ColorUtils.isDarkTheme(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItem item) {
        if (isDarkTheme) helper.itemView.setBackgroundResource(R.color.dark_theme_background);
        switch (helper.getItemViewType()) {
            case MultiItem.ITEM_TYPE_NOTE:
                if (isExpanded) {
                    convertNoteExpanded(helper, item.note);
                } else {
                    convertNote(helper, item.note);
                }
                break;
            case MultiItem.ITEM_TYPE_NOTEBOOK:
                convertNotebook(helper, item.notebook);
                break;
        }
        helper.addOnClickListener(R.id.iv_more);
    }

    private void convertNote(BaseViewHolder helper, Note note) {
        helper.setText(R.id.tv_note_title, note.getTitle());
        helper.setText(R.id.tv_added_time, TimeUtils.getLongDateTime(context, note.getAddedTime()));
        helper.setImageDrawable(R.id.iv_icon, ColorUtils.tintDrawable(
                context.getResources().getDrawable(R.drawable.ic_doc_text_alpha), accentColor));

        helper.setBackgroundRes(R.id.ll_card, isDarkTheme ?
                R.drawable.bg_round_dark: R.drawable.bg_round_light);
    }

    private void convertNoteExpanded(BaseViewHolder holder, Note note) {
        holder.itemView.setBackgroundColor(PalmApp.getColorCompact(isDarkTheme ?
                R.color.dark_theme_background : R.color.light_theme_background));
        if(!PalmApp.getStringCompact(R.string.note_default_name).equals(note.getTitle())){
            holder.setText(R.id.tv_note_title, note.getTitle());
        }else{
            holder.setGone(R.id.tv_note_title, false);
        }
        holder.setText(R.id.tv_content, getShortPreviewContent(note.getPreviewContent()));

//        holder.setText(R.id.tv_time, TimeUtils.getPrettyTime(note.getAddedTime()));
//        holder.setTextColor(R.id.tv_time, accentColor);
        holder.setBackgroundRes(R.id.rl_card, isDarkTheme ?
                R.drawable.bg_round_dark: R.drawable.bg_round_light);
        String weather = note.getWeather() == null? "":note.getWeather();
        holder.setText(R.id.tv_time, FormatUtils.getFormatDateTime("HH:mm", note.getAddedTime().getTime())+" · "+weather);
        holder.setText(R.id.tv_location, note.getLocPoi());
        String day = FormatUtils.getFormatDateTime("dd", note.getAddedTime().getTime());
        String month = FormatUtils.getFormatDateTime("M月/E", note.getAddedTime().getTime());
        holder.setText(R.id.tv_date, day);
        holder.setText(R.id.tv_month, month);

        if (note.getPreviewImage() != null) {
            holder.getView(R.id.iv_image).setVisibility(View.VISIBLE);
            Uri thumbnailUri = FileHelper.getThumbnailUri(context, note.getPreviewImage());
            Glide.with(PalmApp.getContext())
                    .load(thumbnailUri)
                    .centerCrop()
                    .crossFade()
                    .into((ImageView) holder.getView(R.id.iv_image));
        } else {
            holder.getView(R.id.iv_image).setVisibility(View.GONE);
        }
    }

    private void convertNotebook(BaseViewHolder helper, Notebook notebook) {
        int nbColor = notebook.getColor();
        helper.setText(R.id.tv_note_title, notebook.getTitle());
        String str = context.getResources().getQuantityString(R.plurals.notes_number, notebook.getCount(), notebook.getCount());
        helper.setText(R.id.tv_added_time, str);
        helper.setImageDrawable(R.id.iv_icon, ColorUtils.tintDrawable(
                context.getResources().getDrawable(R.drawable.ic_folder_black_24dp), nbColor));
        helper.setBackgroundRes(R.id.ll_card, isDarkTheme ?
                R.drawable.bg_round_dark: R.drawable.bg_round_light);
    }


    private String getShortPreviewContent(String previewContent){
        List<String> pics = StringUtils.getMatchers(Constants.IMAGE_REGEX, previewContent);
        for (String pic : pics){
            previewContent = previewContent.replace(pic,mContext.getString(R.string.disp_picture));
        }
        List<String> files = StringUtils.getMatchers(Constants.FILE_REGEX, previewContent);
        for (String file : files){
            previewContent = previewContent.replace(file,mContext.getString(R.string.disp_file));
        }
        return previewContent;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        try {
            MultiItem multiItem = getItem(pos);
            if (multiItem.itemType == MultiItem.ITEM_TYPE_NOTE) {
                return String.valueOf(multiItem.note.getTitle().charAt(0));
            } else if (multiItem.itemType == MultiItem.ITEM_TYPE_NOTEBOOK) {
                return String.valueOf(multiItem.notebook.getTitle().charAt(0));
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public static class MultiItem implements MultiItemEntity {

        public static final int ITEM_TYPE_NOTE = 0;

        public static final int ITEM_TYPE_NOTEBOOK = 1;

        public int itemType;

        public Note note;

        public Notebook notebook;

        public MultiItem(Note note) {
            this.note = note;
            this.itemType = ITEM_TYPE_NOTE;
        }

        public MultiItem(Notebook notebook) {
            this.notebook = notebook;
            this.itemType = ITEM_TYPE_NOTEBOOK;
        }

        @Override
        public int getItemType() {
            return itemType;
        }
    }
}
