package me.richard.note.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.richard.note.PalmApp;
import me.richard.note.R;
import me.richard.note.adapter.NotesAdapter.MultiItem;
import me.richard.note.async.ResourceAsyncTask;
import me.richard.note.config.Constants;
import me.richard.note.model.Attachment;
import me.richard.note.model.Category;
import me.richard.note.model.MindSnagging;
import me.richard.note.model.ModelFactory;
import me.richard.note.model.Note;
import me.richard.note.model.Notebook;
import me.richard.note.model.data.Resource;
import me.richard.note.model.enums.ModelType;
import me.richard.note.model.enums.Status;
import me.richard.note.provider.AttachmentsStore;
import me.richard.note.provider.NotesStore;
import me.richard.note.repository.BaseRepository;
import me.richard.note.repository.NoteRepository;
import me.richard.note.util.FileHelper;
import me.richard.note.util.LogUtils;
import me.richard.note.util.ModelHelper;
import me.richard.note.util.preferences.NotePreferences;

/**
 * Created by richard on 2018/3/13.*/
public class NoteViewModel extends BaseViewModel<Note> {

    @Override
    protected BaseRepository<Note> getRepository() {
        return new NoteRepository();
    }

    public LiveData<Resource<List<MultiItem>>> getMultiItems(Category category, Status status, Notebook notebook) {
        return ((NoteRepository) getRepository()).getMultiItems(category, status, notebook);
    }

    public String getEmptySubTitle(Status status) {
        return PalmApp.getContext().getString(
                status == Status.NORMAL ? R.string.notes_list_empty_sub_normal :
                        status == Status.TRASHED ? R.string.notes_list_empty_sub_trashed :
                                status == Status.ARCHIVED ? R.string.notes_list_empty_sub_archived :
                                        R.string.notes_list_empty_sub_normal);
    }

    public LiveData<Resource<Note>> saveSnagging(@NonNull Note note, MindSnagging snagging, @Nullable Attachment attachment) {
        MutableLiveData<Resource<Note>> result = new MutableLiveData<>();
        new ResourceAsyncTask<>(result, () -> {
            String content = snagging.getContent();
            if (attachment != null) {
                // Save attachment
                attachment.setModelCode(note.getCode());
                attachment.setModelType(ModelType.NOTE);
                AttachmentsStore.getInstance(PalmApp.getContext()).saveModel(attachment);

                // prepare note content
                if (Constants.MIME_TYPE_IMAGE.equalsIgnoreCase(attachment.getMineType())
                        || Constants.MIME_TYPE_SKETCH.equalsIgnoreCase(attachment.getMineType())) {
                    content = content + "![](" + snagging.getPicture() + ")";
                } else {
                    content = content + "[](" + snagging.getPicture() + ")";
                }
            }

            // Prepare note info
            note.setContent(content);
            note.setTitle(ModelHelper.getNoteTitle(snagging.getContent(), snagging.getContent()));
            note.setPreviewImage(snagging.getPicture());
            note.setPreviewContent(ModelHelper.getNotePreview(snagging.getContent()));

            // Create note file and attach to note
            String extension = NotePreferences.getInstance().getNoteFileExtension();
            File noteFile = FileHelper.createNewAttachmentFile(PalmApp.getContext(), extension);
            try {
                // Create note content attachment
                Attachment atFile = ModelFactory.getAttachment();
                FileUtils.writeStringToFile(noteFile, note.getContent(), "utf-8");
                atFile.setUri(FileHelper.getUriFromFile(PalmApp.getContext(), noteFile));
                atFile.setSize(FileUtils.sizeOf(noteFile));
                atFile.setPath(noteFile.getPath());
                atFile.setName(noteFile.getName());
                atFile.setModelType(ModelType.NOTE);
                atFile.setModelCode(note.getCode());
                AttachmentsStore.getInstance(PalmApp.getContext()).saveModel(atFile);

                note.setContentCode(atFile.getCode());
            } catch (IOException e) {
                LogUtils.e(e);
                return Resource.error(e.getMessage(), null);
            }

            NotesStore.getInstance(PalmApp.getContext()).saveModel(note);

            // Return value
            return Resource.success(note);
        }).execute();
        return result;
    }
}
