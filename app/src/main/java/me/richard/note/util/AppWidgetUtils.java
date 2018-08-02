package me.richard.note.util;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.Arrays;

import me.richard.note.R;
import me.richard.note.widget.desktop.ListWidgetProvider;

/**
 * Created by richard on 2018/1/25.*/
public class AppWidgetUtils {

    public static void notifyAppWidgets(Context context) {
        // Home widgets
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        int[] ids = mgr.getAppWidgetIds(new ComponentName(context, ListWidgetProvider.class));
        LogUtils.d("Notifies AppWidget data changed for widgets " + Arrays.toString(ids));
        mgr.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);
    }
}
