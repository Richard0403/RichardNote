package me.richard.note.provider.schema;

/**
 * Created by richard on 2018/2/14.*/
public interface CategorySchema extends BaseSchema {
    String TABLE_NAME = "gt_category";
    String NAME = "name";
    String COLOR = "color";
    String PORTRAIT = "portrait";
    String CATEGORY_ORDER = "category_order";
    String COUNT = "count";
}
