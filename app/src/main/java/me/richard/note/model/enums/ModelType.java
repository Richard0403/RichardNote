package me.richard.note.model.enums;

import android.support.annotation.StringRes;

import me.richard.note.R;
import me.richard.note.model.Alarm;
import me.richard.note.model.Attachment;
import me.richard.note.model.Location;
import me.richard.note.model.MindSnagging;
import me.richard.note.model.Model;
import me.richard.note.model.Note;
import me.richard.note.model.Notebook;
import me.richard.note.model.TimeLine;
import me.richard.note.model.Weather;


/**
 * Created by wangshouheng on 2017/8/12. */
public enum ModelType {
    NONE(0, Model.class, R.string.model_name_none),
    NOTE(3, Note.class, R.string.model_name_note),
    NOTEBOOK(9, Notebook.class, R.string.model_name_notebook),
    ALARM(10, Alarm.class, R.string.model_name_alarm),
    ATTACHMENT(11, Attachment.class, R.string.model_name_attachment),
    LOCATION(13, Location.class, R.string.model_name_location),
    MIND_SNAGGING(14, MindSnagging.class, R.string.model_name_mind_snagging),
    TIME_LINE(15, TimeLine.class, R.string.model_name_timeline),
    WEATHER(16, Weather.class, R.string.model_name_weather);

    public final int id;

    public final Class<?> cls;

    @StringRes
    public final int typeName;

    ModelType(int id, Class<?> cls, int typeName) {
        this.id = id;
        this.cls = cls;
        this.typeName = typeName;
    }

    public static ModelType getTypeById(int id){
        for (ModelType type : values()){
            if (type.id == id){
                return type;
            }
        }
        return NONE;
    }

    public static ModelType getTypeByName(Class<?> cls) {
        for (ModelType type : values()){
            if (type.cls.getName().equals(cls.getName())){
                return type;
            }
        }
        return NONE;
    }
}
