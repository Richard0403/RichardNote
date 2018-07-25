package me.richard.note.provider.schema;

/**
 * Created by WngShhng on 2017/12/10.*/
public interface NoteSchema extends BaseSchema {
    String TABLE_NAME = "gt_note";
    String PARENT_CODE = "parent_code";
    String TITLE = "title";
    String CONTENT_CODE = "content_code";
    String TAGS = "tags";
    String TREE_PATH = "tree_path";
    String PREVIEW_IMAGE = "preview_image";
    String NOTE_TYPE = "note_type";
    String PREVIEW_CONTENT = "preview_content";

    String LAT = "latitude";
    String LNT = "longitude";
    String LOC_POI = "location_poi";
    String LOC_CITY = "location_city";
    String WEATHER = "weather";
    String TEMPERATURE = "temperature";
}
