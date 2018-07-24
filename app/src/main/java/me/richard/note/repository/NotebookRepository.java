package me.richard.note.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import me.richard.note.PalmApp;
import me.richard.note.async.NormalAsyncTask;
import me.richard.note.model.Notebook;
import me.richard.note.model.data.Resource;
import me.richard.note.model.enums.Status;
import me.richard.note.provider.BaseStore;
import me.richard.note.provider.NotebookStore;

/**
 * Created by Employee on 2018/3/13. */
public class NotebookRepository extends BaseRepository<Notebook> {

    @Override
    protected BaseStore<Notebook> getStore() {
        return NotebookStore.getInstance(PalmApp.getContext());
    }

    public LiveData<Resource<Notebook>> update(Notebook model, Status fromStatus, Status toStatus) {
        MutableLiveData<Resource<Notebook>> result = new MutableLiveData<>();
        new NormalAsyncTask<>(result, () -> {
            ((NotebookStore) getStore()).update(model, fromStatus, toStatus);
            return model;
        }).execute();
        return result;
    }

    public LiveData<Resource<Notebook>> move(Notebook notebook, Notebook toNotebook) {
        MutableLiveData<Resource<Notebook>> result = new MutableLiveData<>();
        new NormalAsyncTask<>(result, () -> {
            ((NotebookStore) getStore()).move(notebook, toNotebook);
            return notebook;
        }).execute();
        return result;
    }
}
