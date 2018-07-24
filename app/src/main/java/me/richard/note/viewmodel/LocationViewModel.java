package me.richard.note.viewmodel;

import android.arch.lifecycle.LiveData;

import me.richard.note.model.Location;
import me.richard.note.model.Note;
import me.richard.note.model.data.Resource;
import me.richard.note.repository.BaseRepository;
import me.richard.note.repository.LocationRepository;

/**
 * Created by shouh on 2018/3/17.*/
public class LocationViewModel extends BaseViewModel<Location> {

    @Override
    protected BaseRepository<Location> getRepository() {
        return new LocationRepository();
    }

    public LiveData<Resource<Location>> getLocation(Note note) {
        return ((LocationRepository) getRepository()).getLocation(note);
    }
}