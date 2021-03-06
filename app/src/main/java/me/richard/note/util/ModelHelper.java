package me.richard.note.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.richard.note.PalmApp;
import me.richard.note.R;
import me.richard.note.config.Constants;
import me.richard.note.config.TextLength;
import me.richard.note.model.Attachment;
import me.richard.note.model.Location;
import me.richard.note.model.MindSnagging;
import me.richard.note.model.Model;
import me.richard.note.model.Note;
import me.richard.note.viewmodel.CategoryViewModel;
import me.richard.note.widget.FlowLayout;

/**
 * Created by Richard on 2017/11/4.*/
public class ModelHelper {

    @Nullable
    private static Pattern titlePattern;

    @Nullable
    private static Pattern imagePattern;

    public static <T extends Model> String getTimeInfo(T model) {
        return PalmApp.getContext().getString(R.string.text_created_time) + " : "
                + TimeUtils.getPrettyTime(model.getAddedTime()) + "\n"
                + PalmApp.getContext().getString(R.string.text_last_modified_time) + " : "
                + TimeUtils.getPrettyTime(model.getLastModifiedTime()) + "\n"
                + PalmApp.getContext().getString(R.string.text_last_sync_time) + " : "
                + (model.getLastSyncTime().getTime() == 0 ? "--" : TimeUtils.getPrettyTime(model.getLastModifiedTime()));
    }

    public static void copyToClipboard(Activity ctx, String content) {
        ClipboardManager clipboardManager = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(content);
    }

    public static void share(Context context, String title, String content, List<Attachment> attachments) {
        Intent shareIntent = new Intent();
        if (attachments.size() == 0) {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
        } else if (attachments.size() == 1) {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType(attachments.get(0).getMineType());
            shareIntent.putExtra(Intent.EXTRA_STREAM, attachments.get(0).getUri());
        } else {
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            ArrayList<Uri> uris = new ArrayList<>();
            Map<String, Boolean> mimeTypes = new HashMap<>();
            for (Attachment attachment : attachments) {
                uris.add(attachment.getUri());
                mimeTypes.put(attachment.getMineType(), true);
            }
            shareIntent.setType(mimeTypes.size() > 1 ? "*/*" : (String) mimeTypes.keySet().toArray()[0]);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);

        context.startActivity(Intent.createChooser(shareIntent,
                context.getResources().getString(R.string.share_message_chooser)));
    }

    public static void share(Context context, MindSnagging mindSnagging) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mindSnagging.getContent());

        if (mindSnagging.getPicture() != null) {
            shareIntent.setType(FileHelper.getMimeTypeInternal(context, mindSnagging.getPicture()));
            shareIntent.putExtra(Intent.EXTRA_STREAM, mindSnagging.getPicture());
        }

        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.share_message_chooser)));
    }

    public static void shareFile(Context context, File file, String mimeType) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType(mimeType);
        shareIntent.putExtra(Intent.EXTRA_STREAM, FileHelper.getUriFromFile(context, file));
        context.startActivity(Intent.createChooser(shareIntent,
                context.getResources().getString(R.string.share_message_chooser)));
    }

    public static <T extends Model> void copyLink(Activity ctx, T model) {
        if (model.getLastSyncTime().getTime() == 0) {
            ToastUtils.makeToast(R.string.cannot_get_link_of_not_synced_item);
            return;
        }

        ClipboardManager clipboardManager = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(null);
    }

    public static String getFormatLocation(Location location) {
        return location.getCountry() + "|"
                + location.getProvince() + "|"
                + location.getCity() + "|"
                + location.getDistrict();
    }

    // region statistic helper
    public static void showStatistic(Context context, Note note) {
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_stats, null, false);
        LinearLayout llStats = root.findViewById(R.id.ll_stats);
        addStat(context, llStats, context.getString(R.string.text_created_time), TimeUtils.getPrettyTime(note.getAddedTime()));
        addStat(context, llStats, context.getString(R.string.text_last_modified_time), TimeUtils.getPrettyTime(note.getLastModifiedTime()));
        addStat(context, llStats, context.getString(R.string.text_last_sync_time), (note.getLastSyncTime().getTime() == 0 ? "--" : TimeUtils.getPrettyTime(note.getLastModifiedTime())));
        addStat(context, llStats, context.getString(R.string.text_chars_number), String.valueOf(note.getContent().length()));
        new AlertDialog.Builder(context)
                .setTitle(R.string.text_statistic)
                .setView(root)
                .setPositiveButton(R.string.text_confirm, null)
                .create()
                .show();
    }

    private static void addStat(Context context, LinearLayout llStats, String name, String result) {
        LinearLayout llStat = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_stat, null, false);
        ((TextView) llStat.findViewById(R.id.tv_name)).setText(name);
        ((TextView) llStat.findViewById(R.id.tv_result)).setText(result);
        llStats.addView(llStat);
    }
    // endregion

    public static void showLabels(Context context, String tags) {
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_tags, null, false);
        FlowLayout flowLayout = root.findViewById(R.id.fl_labels);
        addTagsToLayout(context, flowLayout, tags);
        new AlertDialog.Builder(context)
                .setTitle(R.string.text_tags)
                .setView(root)
                .setPositiveButton(R.string.text_confirm, null)
                .create()
                .show();
    }

    private static void addTagsToLayout(Context context, FlowLayout flowLayout, String stringTags){
        if (TextUtils.isEmpty(stringTags)) return;
        String[] tags = stringTags.split(CategoryViewModel.CATEGORY_SPLIT);
        for (String tag : tags) addTagToLayout(context, flowLayout, tag);
    }

    private static  void addTagToLayout(Context context, FlowLayout flowLayout, String tag){
        int margin = ViewUtils.dp2Px(context, 2f);
        int padding = ViewUtils.dp2Px(context, 5f);
        TextView tvLabel = new TextView(context);
        tvLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(tvLabel.getLayoutParams());
        params.setMargins(margin, margin, margin, margin);
        tvLabel.setLayoutParams(params);
        tvLabel.setPadding(padding, 0, padding, 0);
        tvLabel.setBackgroundResource(ColorUtils.isDarkTheme(context) ? R.drawable.label_background_dark : R.drawable.label_background);
        tvLabel.setText(tag);
        flowLayout.addView(tvLabel);
    }

    public static String getNoteTitle(String inputTitle, String noteContent) {
        if (!TextUtils.isEmpty(inputTitle)) {
            if (inputTitle.length() >= TextLength.TITLE_TEXT_LENGTH.length) {
                return inputTitle.substring(0, TextLength.TITLE_TEXT_LENGTH.length);
            }
            return inputTitle;
        }

        // Use default note title
        if (TextUtils.isEmpty(noteContent)) {
            return PalmApp.getStringCompact(R.string.note_default_name);
        }

        // Get title from note content
        if (titlePattern == null) {
            titlePattern = Pattern.compile(Constants.TITLE_REGEX);
        }
        Matcher matcher = titlePattern.matcher(noteContent);
        if (matcher.find()) {
            String mdTitle = matcher.group();
            char[] chars = mdTitle.toCharArray();
            int i = 0;
            for (char c : chars) {
                if (c != '#' && c != ' ') {
                    break;
                }
                i++;
            }
            if (i < chars.length) {
                String title = mdTitle.substring(i);
                // The length of the matched title must be
                if (title.length() >= TextLength.TITLE_TEXT_LENGTH.length) {
                    title = title.substring(0, TextLength.TITLE_TEXT_LENGTH.length);
                }
                return title;
            }
        }

        // Use default note title
        return PalmApp.getStringCompact(R.string.note_default_name);
    }

    public static String getNotePreview(String noteContent) {
        if (TextUtils.isEmpty(noteContent)) {
            return "";
        }

        if (noteContent.length() > TextLength.NOTE_CONTENT_PREVIEW_LENGTH.length) {
//            return noteContent.substring(0, TextLength.NOTE_CONTENT_PREVIEW_LENGTH.length).trim();
            return noteContent.substring(0, TextLength.NOTE_CONTENT_PREVIEW_LENGTH.length).trim().replace('\n', ' ');
        }

//        return noteContent.trim().replace('\n', ' ');
        return noteContent.trim().replace('\n', ' ');
    }

    public static Uri getNotePreviewImage(String noteContent) {
        if (TextUtils.isEmpty(noteContent)) {
            return null;
        }

        if (imagePattern == null) {
            imagePattern = Pattern.compile(Constants.IMAGE_REGEX);
        }
        Matcher matcher = imagePattern.matcher(noteContent);
        if (matcher.find()) {
            String str = matcher.group();
            if (!TextUtils.isEmpty(str)) {
                int len = str.length();
                str = str.substring(str.lastIndexOf('(') + 1, len - 1);
                return Uri.parse(str);
            }
        }
        return null;
    }

}