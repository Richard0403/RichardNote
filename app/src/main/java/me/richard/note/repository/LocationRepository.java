package me.richard.note.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import me.richard.note.PalmApp;
import me.richard.note.async.NormalAsyncTask;
import me.richard.note.model.Location;
import me.richard.note.model.Note;
import me.richard.note.model.data.Resource;
import me.richard.note.provider.BaseStore;
import me.richard.note.provider.LocationsStore;

/**
 * Created by shouh on 2018/3/17.*/
public class LocationRepository extends BaseRepository<Location> {

    @Override
    protected BaseStore<Location> getStore() {
        return LocationsStore.getInstance(PalmApp.getContext());
    }

    public LiveData<Resource<Location>> getLocation(Note note) {
        MutableLiveData<Resource<Location>> result = new MutableLiveData<>();
        new NormalAsyncTask<>(result, () -> ((LocationsStore) getStore()).getLocation(note)).execute();
        return result;
    }
}
