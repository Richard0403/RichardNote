package me.richard.note.util;

import android.content.Context;
import android.content.Intent;

import me.richard.note.R;
import me.richard.note.activity.ContentActivity;
import me.richard.note.activity.MainActivity;
import me.richard.note.config.Constants;
import me.richard.note.model.Model;
import me.richard.note.model.Note;

public class ShortcutHelper {

    public static <T extends Model> void addShortcut(Context context, T model) {
        Context mContext = context.getApplicationContext();
        Intent shortcutIntent = new Intent(mContext, MainActivity.class);
        shortcutIntent.putExtra(Constants.EXTRA_CODE, model.getCode());
        shortcutIntent.putExtra(Constants.EXTRA_FRAGMENT, getFragmentToDispatch(model));
        shortcutIntent.setAction(Constants.ACTION_SHORTCUT);
        shortcutIntent.putExtra(ContentActivity.EXTRA_HAS_TOOLBAR, true);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getShortcutName(model));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(mContext, R.drawable.note_shortcut));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        context.sendBroadcast(addIntent);
    }

    private static <T extends Model> String getShortcutName(T model) {
         if (model instanceof Note) {
            return ((Note) model).getTitle();
        }
        return "PalmCollege";
    }

    private static <T extends Model> String getFragmentToDispatch(T model) {
        if (model instanceof Note) {
            return Constants.VALUE_FRAGMENT_NOTE;
        }
        return "PalmCollege";
    }
}
