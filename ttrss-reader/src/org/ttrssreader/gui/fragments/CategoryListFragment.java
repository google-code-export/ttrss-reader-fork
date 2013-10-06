/*
 * ttrss-reader-fork for Android
 * 
 * Copyright (C) 2010 N. Braden.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 */

package org.ttrssreader.gui.fragments;

import org.ttrssreader.R;
import org.ttrssreader.controllers.Controller;
import org.ttrssreader.gui.FeedActivity;
import org.ttrssreader.gui.FeedHeadlineActivity;
import org.ttrssreader.gui.MenuActivity;
import org.ttrssreader.gui.interfaces.IItemSelectedListener.TYPE;
import org.ttrssreader.model.CategoryAdapter;
import org.ttrssreader.model.updaters.ReadStateUpdater;
import org.ttrssreader.model.updaters.Updater;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class CategoryListFragment extends MainListFragment {
    
    protected static final TYPE THIS_TYPE = TYPE.CATEGORY;
    
    private static final int SELECT_ARTICLES = MenuActivity.MARK_GROUP + 54;
    private static final int SELECT_FEEDS = MenuActivity.MARK_GROUP + 55;
    
    public static CategoryListFragment newInstance() {
        // Create a new fragment instance
        CategoryListFragment detail = new CategoryListFragment();
        detail.setHasOptionsMenu(true);
        detail.setRetainInstance(true);
        return detail;
    }
    
    public static CategoryListFragment newInstance(CategoryListFragment old) {
        return newInstance();
    }
    
    @Override
    public void onActivityCreated(Bundle instance) {
        super.onActivityCreated(instance);
        
        adapter = new CategoryAdapter(getActivity().getApplicationContext());
        setListAdapter(adapter);
        
        // Read the selected list item after orientation changes and similar
        if (instance != null) {
            selectedIndex = instance.getInt(SELECTED_INDEX, SELECTED_INDEX_DEFAULT);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Controller.getInstance().lastOpenedFeeds.clear();
        Controller.getInstance().lastOpenedArticles.clear();
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (Controller.getInstance().invertBrowsing())
            menu.add(MenuActivity.MARK_GROUP, SELECT_FEEDS, Menu.NONE, R.string.Commons_SelectFeeds);
        else
            menu.add(MenuActivity.MARK_GROUP, SELECT_ARTICLES, Menu.NONE, R.string.Commons_SelectArticles);
        
    }
    
    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterContextMenuInfo cmi = (AdapterContextMenuInfo) item.getMenuInfo();
        if (adapter == null || cmi == null)
            return false;
        
        int id = adapter.getId(cmi.position);
        Intent intent;
        
        switch (item.getItemId()) {
            case MenuActivity.MARK_READ:
                if (id < -10)
                    new Updater(this, new ReadStateUpdater(id, 42)).exec();
                new Updater(this, new ReadStateUpdater(id)).exec();
                return true;
            case SELECT_ARTICLES:
                if (id < 0)
                    return false;
                intent = new Intent(getActivity(), FeedHeadlineActivity.class);
                intent.putExtra(FeedHeadlineListFragment.FEED_ID, FeedHeadlineActivity.FEED_NO_ID);
                intent.putExtra(FeedHeadlineListFragment.FEED_CAT_ID, id);
                intent.putExtra(FeedHeadlineListFragment.FEED_SELECT_ARTICLES, true);
                startActivity(intent);
                return true;
            case SELECT_FEEDS:
                if (id < 0)
                    return false;
                intent = new Intent(getActivity(), FeedActivity.class);
                intent.putExtra(FeedListFragment.FEED_CAT_ID, id);
                startActivity(intent);
                return true;
        }
        return false;
    }
    
    @Override
    public TYPE getType() {
        return THIS_TYPE;
    }
    
}
