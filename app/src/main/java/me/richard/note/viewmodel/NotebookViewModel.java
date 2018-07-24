package me.richard.note.viewmodel;

import android.arch.lifecycle.LiveData;

import me.richard.note.model.Notebook;
import me.richard.note.model.data.Resource;
import me.richard.note.model.enums.Status;
import me.richard.note.repository.BaseRepository;
import me.richard.note.repository.NotebookRepository;

/**
 * Created by shouh on 2018/3/17.*/
public class NotebookViewModel extends BaseViewModel<Notebook> {

    @Override
    protected BaseRepository<Notebook> getRepository() {
        return new NotebookRepository();
    }

    public LiveData<Resource<Notebook>> update(Notebook notebook, Status fromStatus, Status toStatus) {
        return ((NotebookRepository) getRepository()).update(notebook, fromStatus, toStatus);
    }

    public LiveData<Resource<Notebook>> move(Notebook notebook, Notebook toNotebook) {
        return ((NotebookRepository) getRepository()).move(notebook, toNotebook);
    }
}
