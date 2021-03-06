package me.richard.note.provider.helper;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import me.richard.note.model.Category;
import me.richard.note.model.Note;
import me.richard.note.model.Notebook;
import me.richard.note.provider.NotebookStore;
import me.richard.note.provider.NotesStore;
import me.richard.note.provider.schema.NoteSchema;
import me.richard.note.provider.schema.NotebookSchema;

/**
 * Created by WngShhng on 2017/12/12.*/
public class ArchiveHelper {

    public static List<Notebook> getNotebooks(Context context, Notebook notebook) {
        return NotebookStore.getInstance(context).getArchived(notebook == null ?
                        " ( " + NotebookSchema.PARENT_CODE + " IS NULL OR " + NotebookSchema.PARENT_CODE + " = 0 ) " :
                        " ( " + NotebookSchema.PARENT_CODE  + " = " + notebook.getCode() +" ) ",
                NotebookSchema.LAST_MODIFIED_TIME + " DESC ");
    }

    public static List<Note> getNotes(Context context, Notebook notebook) {
        return NotesStore.getInstance(context).getArchived(notebook == null ?
                        " ( " + NoteSchema.PARENT_CODE + " IS NULL OR " + NoteSchema.PARENT_CODE + " = 0 ) " :
                        " ( " + NoteSchema.PARENT_CODE  + " = " + notebook.getCode() +" ) ",
                NoteSchema.LAST_MODIFIED_TIME + " DESC ");
    }

    public static List<Note> getNotes(Context context, @Nonnull Category category) {
        return NotesStore.getInstance(context).getArchived(
                NoteSchema.TAGS + " LIKE '%'||'" + category.getCode() + "'||'%' ",
                NotebookSchema.ADDED_TIME + " DESC ");
    }

    public static List getNotebooksAndNotes(Context context, Notebook notebook) {
        List list = new LinkedList();
        list.addAll(getNotebooks(context, notebook));
        list.addAll(getNotes(context, notebook));
        return list;
    }

    public static List getNotebooksAndNotes(Context context, @Nonnull Category category) {
        return getNotes(context, category);
    }
}
