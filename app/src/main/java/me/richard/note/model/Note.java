package me.richard.note.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import me.richard.note.PalmApp;
import me.richard.note.model.enums.NoteType;
import me.richard.note.model.enums.Status;
import me.richard.note.provider.annotation.Column;
import me.richard.note.provider.annotation.Table;
import me.richard.note.provider.schema.NoteSchema;

/**
 * Created by Richard on 2017/5/12.*/
@Table(name = NoteSchema.TABLE_NAME)
public class Note extends Model implements Parcelable {

    @Column(name = NoteSchema.PARENT_CODE)
    private long parentCode;

    @Column(name = NoteSchema.TREE_PATH)
    private String treePath;

    @Column(name = NoteSchema.TITLE)
    private String title;

    @Column(name = NoteSchema.CONTENT_CODE)
    private long contentCode;

    @Column(name = NoteSchema.TAGS)
    private String tags;

    @Column(name = NoteSchema.PREVIEW_IMAGE)
    private Uri previewImage;

    @Column(name = NoteSchema.NOTE_TYPE)
    private NoteType noteType;

    @Column(name = NoteSchema.PREVIEW_CONTENT)
    private String previewContent;

    @Column(name = NoteSchema.LAT)
    private double latitude;

    @Column(name = NoteSchema.LNT)
    private double longitude;

    @Column(name = NoteSchema.LOC_POI)
    private String locPoi;

    @Column(name = NoteSchema.LOC_CITY)
    private String locCity;

    @Column(name = NoteSchema.WEATHER)
    private String weather;

    @Column(name = NoteSchema.TEMPERATURE)
    private int temperature;

    // region Android端字段，不计入数据库

    private String content;

    private String tagsName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTagsName() {
        return tagsName;
    }

    public void setTagsName(String tagsName) {
        this.tagsName = tagsName;
    }

    // endregion

    public Note(){}

    private Note(Parcel in) {
        setId(in.readLong());
        setCode(in.readLong());
        setUserId(in.readLong());
        setAddedTime(new Date(in.readLong()));
        setLastModifiedTime(new Date(in.readLong()));
        setLastSyncTime(new Date(in.readLong()));
        setStatus(Status.getStatusById(in.readInt()));

        setParentCode(in.readLong());
        setTreePath(in.readString());
        setTitle(in.readString());
        setContent(in.readString());
        setContentCode(in.readLong());
        setTags(in.readString());
        setNoteType(NoteType.getTypeById(in.readInt()));
        setPreviewContent(in.readString());
        setTagsName(in.readString());

        setLatitude(in.readDouble());
        setLongitude(in.readDouble());
        setLocPoi(in.readString());
        setLocCity(in.readString());
        setWeather(in.readString());
        setTemperature(in.readInt());

    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public long getParentCode() {
        return parentCode;
    }

    public void setParentCode(long parentCode) {
        this.parentCode = parentCode;
    }

    public String getTreePath() {
        return treePath;
    }

    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getContentCode() {
        return contentCode;
    }

    public void setContentCode(long contentCode) {
        this.contentCode = contentCode;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Uri getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(Uri previewImage) {
        this.previewImage = previewImage;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public String getPreviewContent() {
        return previewContent;
    }

    public void setPreviewContent(String previewContent) {
        this.previewContent = previewContent;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocPoi() {
        return locPoi;
    }

    public void setLocPoi(String locPoi) {
        this.locPoi = locPoi;
    }

    public String getLocCity() {
        return locCity;
    }

    public void setLocCity(String locCity) {
        this.locCity = locCity;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Note{" +
                "parentCode=" + parentCode +
                ", treePath='" + treePath + '\'' +
                ", title='" + title + '\'' +
                ", contentCode=" + contentCode +
                ", tags='" + tags + '\'' +
                ", previewImage=" + previewImage +
                ", noteType=" + noteType +
                ", previewContent='" + previewContent + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", locPoi='" + locPoi + '\'' +
                ", locCity='" + locCity + '\'' +
                ", weather='" + weather + '\'' +
                ", temperature='" + temperature + '\'' +
                ", content='" + content + '\'' +
                ", tagsName='" + tagsName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(getCode());
        dest.writeLong(getUserId());
        dest.writeLong(getAddedTime().getTime());
        dest.writeLong(getLastModifiedTime().getTime());
        dest.writeLong(getLastSyncTime().getTime());
        dest.writeInt(getStatus().id);

        dest.writeLong(getParentCode());
        dest.writeString(getTreePath());
        dest.writeString(getTitle());
        dest.writeString(getContent());
        dest.writeLong(getContentCode());
        dest.writeString(getTags());
        dest.writeInt(getNoteType().getId());
        dest.writeString(getPreviewContent());
        dest.writeString(getTagsName());

        dest.writeDouble(getLatitude());
        dest.writeDouble(getLongitude());
        dest.writeString(getLocPoi());
        dest.writeString(getLocCity());
        dest.writeString(getWeather());
        dest.writeInt(getTemperature());
    }
}
