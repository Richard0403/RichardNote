package me.richard.note.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import me.richard.note.PalmApp;
import me.richard.note.async.NormalAsyncTask;
import me.richard.note.model.Category;
import me.richard.note.model.Note;
import me.richard.note.model.data.Resource;
import me.richard.note.model.enums.Status;
import me.richard.note.provider.BaseStore;
import me.richard.note.provider.CategoryStore;
import me.richard.note.provider.schema.CategorySchema;

/**
 * Created by Richard on 2018/3/13.*/
public class CategoryRepository extends BaseRepository<Category> {

    @Override
    protected BaseStore<Category> getStore() {
        return CategoryStore.getInstance(PalmApp.getContext());
    }

    public LiveData<Resource<List<Category>>> getCategories(Note note) {
        MutableLiveData<Resource<List<Category>>> result = new MutableLiveData<>();
        new NormalAsyncTask<>(result, () -> ((CategoryStore) getStore()).getCategories(note)).execute();
        return result;
    }

    public LiveData<Resource<List<Category>>> getCategories(Status status) {
        MutableLiveData<Resource<List<Category>>> result = new MutableLiveData<>();
        new NormalAsyncTask<>(result, () -> {
            if (status == Status.ARCHIVED) {
                return getStore().getArchived(null, CategorySchema.CATEGORY_ORDER);
            } else if (status == Status.TRASHED) {
                return getStore().getTrashed(null, CategorySchema.CATEGORY_ORDER);
            } else {
                return getStore().get(null, CategorySchema.CATEGORY_ORDER);
            }
        }).execute();
        return result;
    }

    public LiveData<Resource<List<Category>>> updateOrders(List<Category> categories) {
        MutableLiveData<Resource<List<Category>>> result = new MutableLiveData<>();
        new NormalAsyncTask<>(result, () -> {
            ((CategoryStore) getStore()).updateOrders(categories);
            return categories;
        }).execute();
        return result;
    }
}
