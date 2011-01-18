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

package org.ttrssreader.gui;

import org.ttrssreader.R;
import org.ttrssreader.controllers.Controller;
import org.ttrssreader.model.updaters.Updater;
import org.ttrssreader.preferences.Constants;
import org.ttrssreader.utils.Utils;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * This class pulls common functionality from the three subclasses (CategoryActivity, FeedListActivity and
 * FeedHeadlineListActivity).
 */
public class MenuActivity extends ListActivity {
    
    protected boolean configChecked = false;
    protected ListView mListView;
    protected Updater updater;
    
    protected static final int MARK_GROUP = 42;
    protected static final int MARK_READ = MARK_GROUP + 1;
    protected static final int MARK_STAR = MARK_GROUP + 2;
    protected static final int MARK_PUBLISH = MARK_GROUP + 3;
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(MARK_GROUP, MARK_READ, Menu.NONE, R.string.Commons_MarkRead);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.generic, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        
        MenuItem offline = menu.findItem(R.id.Menu_WorkOffline);
        if (Controller.getInstance().isWorkOffline()) {
            offline.setTitle(getString(R.string.UsageOnlineTitle));
            offline.setIcon(R.drawable.ic_menu_play_clip);
        } else {
            offline.setTitle(getString(R.string.UsageOfflineTitle));
            offline.setIcon(R.drawable.ic_menu_stop);
        }
        
        MenuItem displayUnread = menu.findItem(R.id.Menu_DisplayOnlyUnread);
        if (Controller.getInstance().isDisplayOnlyUnread()) {
            displayUnread.setTitle(getString(R.string.Commons_DisplayAll));
            // displayUnread.setIcon(R.drawable.ic_menu_play_clip);
        } else {
            displayUnread.setTitle(getString(R.string.Commons_DisplayOnlyUnread));
            // displayUnread.setIcon(R.drawable.ic_menu_stop);
        }
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);
        
        switch (item.getItemId()) {
            case R.id.Menu_DisplayOnlyUnread:
                Controller.getInstance().setDisplayOnlyUnread(!Controller.getInstance().isDisplayOnlyUnread());
                return true;
            case R.id.Menu_WorkOffline:
                Controller.getInstance().setWorkOffline(!Controller.getInstance().isWorkOffline());
                return true;
            case R.id.Menu_ShowPreferences:
                startActivityForResult(new Intent(this, PreferencesActivity.class),
                        PreferencesActivity.ACTIVITY_SHOW_PREFERENCES);
                return true;
            case R.id.Menu_About:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return false;
        }
    }
    
    protected boolean checkConfig() {
        String url = Controller.getInstance().getUrl();
        if (url.equals(Constants.URL_DEFAULT + Controller.JSON_END_URL)) {
            return false;
        }
        configChecked = true;
        return true;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(Utils.TAG, "onActivityResult. requestCode: " + requestCode + " resultCode: " + resultCode);
        if (resultCode == ErrorActivity.ACTIVITY_SHOW_ERROR) {
            if (configChecked || checkConfig()) {
                doRefresh();
                doUpdate();
            }
        } else if (resultCode == PreferencesActivity.ACTIVITY_SHOW_PREFERENCES) {
            if (configChecked || checkConfig()) {
                doRefresh();
                doUpdate();
            }
        }
    }
    
    protected synchronized void doRefresh() {
    }
    
    protected synchronized void doUpdate() {
    }
    
    protected void openConnectionErrorDialog(String errorMessage) {
        if (updater != null) {
            updater.cancel(true);
            updater = null;
        }
        setProgressBarIndeterminateVisibility(false);
        Intent i = new Intent(this, ErrorActivity.class);
        i.putExtra(ErrorActivity.ERROR_MESSAGE, errorMessage);
        startActivityForResult(i, ErrorActivity.ACTIVITY_SHOW_ERROR);
    }
}