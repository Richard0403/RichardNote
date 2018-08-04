package me.richard.note.net.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * By Richard on 2018/1/6.
 */

public class VersionEntity extends BaseEntity {


    /**
     * 更新点：{n}1.增加计步的准确性{n}2.优化后台电量消耗"
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private long id;
        private int isForce;
        private double size;
        private String updateContent;
        private String url;
        private int versionCode;
        private String versionName;
        private long updateTime;
        private long createTime;


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int isForce() {
            return isForce;
        }

        public void setForce(int force) {
            isForce = force;
        }

        public double getSize() {
            return size;
        }

        public void setSize(double size) {
            this.size = size;
        }

        public String getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
