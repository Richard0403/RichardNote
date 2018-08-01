package me.richard.note.provider.helper;


import me.richard.note.PalmApp;
import me.richard.note.model.Attachment;
import me.richard.note.model.Location;
import me.richard.note.model.MindSnagging;
import me.richard.note.model.Model;
import me.richard.note.model.ModelFactory;
import me.richard.note.model.Note;
import me.richard.note.model.Notebook;
import me.richard.note.model.TimeLine;
import me.richard.note.model.Weather;
import me.richard.note.model.enums.Operation;
import me.richard.note.provider.TimelineStore;

/**
 * Created by Richard on 2017/11/3.*/
public class TimelineHelper {

    public static <T extends Model> void addTimeLine(T model, Operation operation) {
        if (!hasTimeLine(model, operation)) return;
        TimelineStore.getInstance(PalmApp.getContext()).saveModel(ModelFactory.getTimeLine(model, operation));
    }

    public static <T extends Model> TimeLine getTimeLine(T model, Operation operation) {
        if (!hasTimeLine(model, operation)) return null;
        return ModelFactory.getTimeLine(model, operation);
    }

    private static<T extends Model> boolean hasTimeLine(T model, Operation operation) {
        return model != null && (model instanceof Note
                || model instanceof Notebook
                || model instanceof MindSnagging
                || (model instanceof Weather && Operation.ADD == operation)
                || (model instanceof Location && Operation.ADD == operation)
                || (model instanceof Attachment && Operation.ADD == operation));
    }
}
