package me.richard.note.model;

import me.richard.note.provider.annotation.Column;
import me.richard.note.provider.annotation.Table;
import me.richard.note.provider.schema.NotebookSchema;

/**
 * Created by Richard on 2017/7/23.*/
@Table(name = NotebookSchema.TABLE_NAME)
public class Notebook extends Model implements Selectable {

    @Column(name = NotebookSchema.TITLE)
    private String title;

    @Column(name = NotebookSchema.PARENT_CODE)
    private long parentCode;

    @Column(name = NotebookSchema.TREE_PATH)
    private String treePath;

    @Column(name = NotebookSchema.COLOR)
    private int color;

    // region Android端字段，不计入数据库

    /**
     * 目录中内容的数量 */
    private int count;

    private int notebookCount;

    private boolean isSelected;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNotebookCount() {
        return notebookCount;
    }

    public void setNotebookCount(int notebookCount) {
        this.notebookCount = notebookCount;
    }

    // endregion

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "title='" + title + '\'' +
                ", parentCode=" + parentCode +
                ", treePath='" + treePath + '\'' +
                ", color=" + color +
                ", count=" + count +
                ", notebookCount=" + notebookCount +
                "} " + super.toString();
    }
}
