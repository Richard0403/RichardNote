package me.richard.note.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import me.richard.note.PalmApp;
import me.richard.note.async.NormalAsyncTask;
import me.richard.note.model.Attachment;
import me.richard.note.model.data.Resource;
import me.richard.note.provider.AttachmentsStore;
import me.richard.note.provider.BaseStore;

/**
 * Created by Richard on 2018/3/13.*/
public class AttachmentRepository extends BaseRepository<Attachment> {

    @Override
    protected BaseStore<Attachment> getStore() {
        return AttachmentsStore.getInstance(PalmApp.getContext());
    }

    public LiveData<Resource<Attachment>> saveIfNew(Attachment attachment) {
        MutableLiveData<Resource<Attachment>> result = new MutableLiveData<>();
        new NormalAsyncTask<>(result, () -> {
            if (getStore().isNewModel(attachment.getCode())) {
                getStore().saveModel(attachment);
            }
            return attachment;
        }).execute();
        return result;
    }
}
