package me.richard.note.repository;

import android.support.annotation.MainThread;

import java.util.List;

import me.richard.note.PalmApp;
import me.richard.note.model.Note;
import me.richard.note.model.enums.Status;
import me.richard.note.provider.NotesStore;
import me.richard.note.provider.schema.BaseSchema;
import me.richard.note.provider.schema.NoteSchema;
import me.richard.note.util.LogUtils;
import me.richard.note.util.tools.SearchConditions;


/**
 * Created by WngShhng on 2017/12/11.*/
public class QueryRepository {

    private SearchConditions conditions;

    private NotesStore notesStore;

    public QueryRepository(SearchConditions conditions) {
        this.conditions = conditions;
        LogUtils.d(conditions);
        notesStore = NotesStore.getInstance(PalmApp.getContext());
    }

    @MainThread
    public List<Note> getNotes(String queryString) {
        return notesStore.get(getNoteQuerySQL(queryString), NoteSchema.ADDED_TIME + " DESC ");
    }

    private String getNoteQuerySQL(String queryString) {
        return (conditions.isIncludeTags() ?
                " ( " + NoteSchema.TITLE + " LIKE '%'||'" + queryString + "'||'%' "
                        + " OR " + NoteSchema.TAGS + " LIKE '%'||'" + queryString + "'||'%' ) "
                : NoteSchema.TITLE + " LIKE '%'||'" + queryString + "'||'%'"
        ) + getQueryConditions();
    }

    private String getQueryConditions() {
        // should not query the deleted item out
        return (conditions.isIncludeArchived() ? "" : " AND " + BaseSchema.STATUS + " != " + Status.ARCHIVED.id)
                + (conditions.isIncludeTrashed() ? "" : " AND " + BaseSchema.STATUS + " != " + Status.TRASHED.id)
                + " AND " + BaseSchema.STATUS + " != " + Status.DELETED.id;
    }

    public void setConditions(SearchConditions conditions) {
        this.conditions = conditions;
    }
}
