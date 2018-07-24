package me.richard.note.activity;

import android.support.v4.app.Fragment;

import me.richard.note.R;
import me.richard.note.activity.base.BaseListActivity;
import me.richard.note.fragment.CategoriesFragment;
import me.richard.note.fragment.NotesFragment;
import me.richard.note.model.Category;
import me.richard.note.model.Notebook;
import me.richard.note.model.enums.Status;
import me.richard.note.util.FragmentHelper;

public class ArchiveActivity extends BaseListActivity {

    @Override
    protected CharSequence getActionbarTitle() {
        return getString(R.string.drawer_menu_archive);
    }

    @Override
    protected Fragment getNotesFragment() {
        return NotesFragment.newInstance(Status.ARCHIVED);
    }

    @Override
    protected Fragment getCategoryFragment() {
        return CategoriesFragment.newInstance(Status.ARCHIVED);
    }

    @Override
    public void onCategorySelected(Category category) {
        NotesFragment notesFragment = NotesFragment.newInstance(category, Status.ARCHIVED);
        FragmentHelper.replaceWithCallback(this, notesFragment, R.id.fragment_container);
    }

    @Override
    public void onNotebookSelected(Notebook notebook) {
        NotesFragment notesFragment = NotesFragment.newInstance(notebook, Status.ARCHIVED);
        FragmentHelper.replaceWithCallback(this, notesFragment, R.id.fragment_container);
    }
}
