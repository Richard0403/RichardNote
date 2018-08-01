package me.richard.note.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import me.richard.note.PalmApp;
import me.richard.note.async.ResourceAsyncTask;
import me.richard.note.model.Attachment;
import me.richard.note.model.ModelFactory;
import me.richard.note.model.Note;
import me.richard.note.model.data.Resource;
import me.richard.note.model.enums.ModelType;
import me.richard.note.provider.AttachmentsStore;
import me.richard.note.repository.AttachmentRepository;
import me.richard.note.repository.BaseRepository;
import me.richard.note.util.FileHelper;
import me.richard.note.util.LogUtils;
import me.richard.note.util.preferences.NotePreferences;

/**
 * Created by Richard on 2018/3/13.*/
public class AttachmentViewModel extends BaseViewModel<Attachment> {

    @Override
    protected BaseRepository<Attachment> getRepository() {
        return new AttachmentRepository();
    }

    public LiveData<Resource<Attachment>> saveIfNew(Attachment attachment) {
        return ((AttachmentRepository) getRepository()).saveIfNew(attachment);
    }

    public LiveData<Resource<String>> readNoteContent(Note note) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        new ResourceAsyncTask<>(result, () -> {
            Attachment atFile = AttachmentsStore.getInstance(PalmApp.getContext()).get(note.getContentCode());
            if (atFile == null) {
                // return the note content field
                return Resource.success(note.getContent());
            } else {
                try {
                    File noteFile = new File(atFile.getPath());
                    LogUtils.d(noteFile);
                    String content = FileUtils.readFileToString(noteFile, "utf-8");
                    return Resource.success(content);
                } catch (IOException e) {
                    return Resource.error(e.getMessage(), null);
                }
            }
        }).execute();
        return result;
    }

    public LiveData<Resource<Attachment>> writeNoteContent(Note note) {
        MutableLiveData<Resource<Attachment>> result = new MutableLiveData<>();
        new ResourceAsyncTask<>(result, () -> {
            Attachment atFile = AttachmentsStore.getInstance(PalmApp.getContext()).get(note.getContentCode());
            if (atFile == null) {
                // If the attachment is not exist, we will try to create a new one.
                String extension = NotePreferences.getInstance().getNoteFileExtension();
                File noteFile = FileHelper.createNewAttachmentFile(PalmApp.getContext(), extension);
                try {
                    FileUtils.writeStringToFile(noteFile, note.getContent(), "utf-8");
                    atFile = ModelFactory.getAttachment();
                    atFile.setUri(FileHelper.getUriFromFile(PalmApp.getContext(), noteFile));
                    atFile.setSize(FileUtils.sizeOf(noteFile));
                    atFile.setPath(noteFile.getPath());
                    atFile.setName(noteFile.getName());
                    atFile.setModelType(ModelType.NOTE);
                    atFile.setModelCode(note.getCode());
                    AttachmentsStore.getInstance(PalmApp.getContext()).saveModel(atFile);
                    return Resource.success(atFile);
                } catch (IOException e) {
                    return Resource.error(e.getMessage(), null);
                }
            } else {
                try {
                    File noteFile = new File(atFile.getPath());
                    FileUtils.writeStringToFile(noteFile, note.getContent(), "utf-8", false);
                    // Whenever the attachment file is updated, remember to update its attachment.
                    atFile.setLastModifiedTime(new Date());
                    AttachmentsStore.getInstance(PalmApp.getContext()).update(atFile);
                    return Resource.success(atFile);
                } catch (IOException e) {
                    return Resource.error(e.getMessage(), atFile);
                }
            }
        }).execute();
        return result;
    }
}
