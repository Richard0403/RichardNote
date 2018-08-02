package me.richard.note.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 2018/1/13. */
public class ActivityUtils {

    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
    }
}
