package me.richard.note.listener;

import me.richard.note.model.Attachment;

public interface OnAttachingFileListener {

    void onAttachingFileErrorOccurred(Attachment attachment);

    void onAttachingFileFinished(Attachment attachment);
}
