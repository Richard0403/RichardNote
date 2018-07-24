package me.richard.note.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.List;

import me.richard.note.PalmApp;
import me.richard.note.R;
import me.richard.note.model.Category;
import me.richard.note.model.Note;
import me.richard.note.model.data.Resource;
import me.richard.note.model.enums.Status;
import me.richard.note.repository.BaseRepository;
import me.richard.note.repository.CategoryRepository;
import me.richard.note.util.LogUtils;

/**
 * Created by WangShouheng on 2018/3/13.*/
public class CategoryViewModel extends BaseViewModel<Category> {

    public final static String CATEGORY_SPLIT = ",";

    @Override
    protected BaseRepository<Category> getRepository() {
        return new CategoryRepository();
    }

    public String getEmptySubTitle(Status status) {
        if (status == null) return null;
        return PalmApp.getContext().getString(
                status == Status.NORMAL ? R.string.tags_list_empty_sub_normal :
                        status == Status.TRASHED ? R.string.tags_list_empty_sub_trashed :
                                status == Status.ARCHIVED ? R.string.tags_list_empty_sub_archived :
                                        R.string.tags_list_empty_sub_normal);
    }

    public LiveData<Resource<List<Category>>> getCategories(Note note) {
        return ((CategoryRepository) getRepository()).getCategories(note);
    }

    public LiveData<Resource<List<Category>>> getCategories(Status status) {
        return ((CategoryRepository) getRepository()).getCategories(status);
    }

    public LiveData<Resource<List<Category>>> updateOrders(List<Category> categories) {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.updateOrders(categories);
    }

    /**
     * Get tags of given category list.
     *
     * @param categories category list
     * @return the tags get from list
     */
    public static String getTags(List<Category> categories) {
        if (categories == null || categories.isEmpty()) return null;
        int len = categories.size();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<len; i++) {
            sb.append(categories.get(i).getCode());
            if (i != len - 1) sb.append(CATEGORY_SPLIT);
        }
        LogUtils.d(sb.toString());
        return sb.toString();
    }

    /**
     * Get the tags name to show.
     *
     * @param categories category list
     * @return tags name
     */
    public static String getTagsName(List<Category> categories) {
        if (categories == null || categories.isEmpty()) return "";
        int len = categories.size();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<len; i++) {
            sb.append(categories.get(i).getName());
            if (i != len - 1) sb.append(CATEGORY_SPLIT);
        }
        LogUtils.d(sb.toString());
        return sb.toString();
    }
}
