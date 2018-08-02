package me.richard.note.provider.helper;

import android.content.Context;
import android.support.annotation.MainThread;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import me.richard.note.PalmApp;
import me.richard.note.config.Constants;
import me.richard.note.model.Attachment;
import me.richard.note.model.Location;
import me.richard.note.model.Stats;
import me.richard.note.model.enums.ModelType;
import me.richard.note.model.enums.Operation;
import me.richard.note.model.enums.Status;
import me.richard.note.provider.AttachmentsStore;
import me.richard.note.provider.CategoryStore;
import me.richard.note.provider.LocationsStore;
import me.richard.note.provider.NotebookStore;
import me.richard.note.provider.NotesStore;
import me.richard.note.provider.TimelineStore;
import me.richard.note.provider.schema.TimelineSchema;
import me.richard.note.util.LogUtils;
import me.richard.note.util.TimeUtils;
import me.richard.note.viewmodel.StatisticViewModel;

/**
 * Created by richard on 2018/1/19.*/
public class StatisticsHelper {

    /**
     * Get all the stats prepared to show.
     *
     * @param context the context
     * @return the Stats object contains the actions result. */
    @MainThread
    public static Stats getStats(Context context) {
        Stats stats = new Stats();

        NotesStore notesStore = NotesStore.getInstance(context);
        stats.setTotalNotes(notesStore.getCount(null, Status.DELETED, true));
        stats.setArchivedNotes(notesStore.getCount(null, Status.ARCHIVED, false));
        stats.setTrashedNotes(notesStore.getCount(null, Status.TRASHED, false));

        CategoryStore categoryStore = CategoryStore.getInstance(context);
        stats.setTotalMinds(categoryStore.getCount(null, Status.DELETED, true));
        stats.setArchivedMinds(categoryStore.getCount(null, Status.ARCHIVED, false));
        stats.setTrashedMinds(categoryStore.getCount(null, Status.TRASHED, false));

        LocationsStore locationsStore = LocationsStore.getInstance(context);
        List<Location> locations = locationsStore.getDistinct(null, null);
        stats.setLocCnt(locations.size());
        stats.setLocations(locations);
        stats.setTotalLocations(locationsStore.getCount(null, Status.DELETED, true));

        NotebookStore notebookStore = NotebookStore.getInstance(context);
        stats.setTotalNotebooks(notebookStore.getCount(null, Status.TRASHED, false));

        AttachmentsStore attachmentsStore = AttachmentsStore.getInstance(context);
        List<Attachment> attachments = attachmentsStore.get(null, null);
        int images = 0, videos = 0, audioRecordings = 0, sketches = 0, files = 0;
        for (Attachment attachment : attachments) {
            if (Constants.MIME_TYPE_IMAGE.equals(attachment.getMineType())) {
                images++;
            } else if (Constants.MIME_TYPE_VIDEO.equals(attachment.getMineType())) {
                videos++;
            } else if (Constants.MIME_TYPE_AUDIO.equals(attachment.getMineType())) {
                audioRecordings++;
            } else if (Constants.MIME_TYPE_SKETCH.equals(attachment.getMineType())) {
                sketches++;
            } else if (Constants.MIME_TYPE_FILES.equals(attachment.getMineType())) {
                files++;
            }
        }
        stats.setTotalAttachments(attachments.size());
        stats.setImages(images);
        stats.setVideos(videos);
        stats.setAudioRecordings(audioRecordings);
        stats.setSketches(sketches);
        stats.setFiles(files);

        stats.setNotesStats(getAddedStatistics(ModelType.NOTE, StatisticViewModel.DAYS_OF_ADDED_MODEL));

        LogUtils.d(stats);
        return stats;
    }

    /**
     * Get the added statistic of model, this method can be refined by query one time and filter in program.
     *
     * @param modelType model type
     * @param days days of model statistic
     * @return the added count of every day */
    public static List<Integer> getAddedStatistics(ModelType modelType, int days) {
        Calendar sevenDaysAgo = TimeUtils.sevenDaysAgo();
        List<Integer> states = new LinkedList<>();
        for (int i=0; i<days; i++) {
            long startMillis = sevenDaysAgo.getTimeInMillis();
            sevenDaysAgo.add(Calendar.DAY_OF_YEAR, 1);
            long endMillis = sevenDaysAgo.getTimeInMillis();
            String whereSQL = TimelineSchema.ADDED_TIME + " >= " + startMillis
                    + " AND " + TimelineSchema.ADDED_TIME + " < " + endMillis
                    + " AND " + TimelineSchema.MODEL_TYPE + " = " + modelType.id
                    + " AND " + TimelineSchema.OPERATION + " = " + Operation.ADD.id;
            int count = TimelineStore.getInstance(PalmApp.getContext()).getCount(whereSQL, Status.DELETED, true);
            states.add(count);
        }
        return states;
    }
}
