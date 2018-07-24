package me.richard.note.async;

import android.os.AsyncTask;

import java.io.File;

import me.richard.note.listener.OnAttachingFileListener;

/**
 * Created by shouh on 2018/4/6.*/
public class ImageCompressTask extends AsyncTask<Void, Void, Void>{

    private File file;

    private OnAttachingFileListener mOnAttachingFileListener;

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
