/*
 * ttrss-reader-fork for Android
 * 
 * Copyright (C) 2010 N. Braden.
 * Copyright (C) 2009-2010 J. Devauchelle.
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
import org.ttrssreader.controllers.Data;
import org.ttrssreader.gui.dialogs.FeedUnsubscribeDialog;
import org.ttrssreader.gui.fragments.ArticleFragment;
import org.ttrssreader.gui.fragments.FeedHeadlineListFragment;
import org.ttrssreader.gui.fragments.MainListFragment;
import org.ttrssreader.model.FeedAdapter;
import org.ttrssreader.model.updaters.ReadStateUpdater;
import org.ttrssreader.model.updaters.Updater;
import org.ttrssreader.utils.AsyncTask;
import org.ttrssreader.utils.Utils;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.actionbarsherlock.view.MenuItem;

public class FeedHeadlineActivity extends MenuActivity {
    
    public static final int FEED_NO_ID = 37846914;
    private static final String FRAGMENT = "HEADLINE_FRAGMENT";
    
    private int categoryId = -1000;
    private int feedId = -1000;
    private int articleId = -1000;
    private boolean selectArticlesForCategory = false;
    
    private FeedHeadlineUpdater headlineUpdater = null;
    private int[] parentIDs = new int[2];
    private String title = "";
    private int unreadCount = 0;
    
    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.feedheadlinelist);
        super.initTabletLayout();
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoryId = extras.getInt(FeedHeadlineListFragment.FEED_CAT_ID);
            feedId = extras.getInt(FeedHeadlineListFragment.FEED_ID);
            articleId = extras.getInt(FeedHeadlineListFragment.ARTICLE_ID);
            selectArticlesForCategory = extras.getBoolean(FeedHeadlineListFragment.FEED_SELECT_ARTICLES);
        } else if (instance != null) {
            categoryId = instance.getInt(FeedHeadlineListFragment.FEED_CAT_ID);
            feedId = instance.getInt(FeedHeadlineListFragment.FEED_ID);
            articleId = instance.getInt(FeedHeadlineListFragment.ARTICLE_ID);
            selectArticlesForCategory = instance.getBoolean(FeedHeadlineListFragment.FEED_SELECT_ARTICLES);
        }
        
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT) == null) {
            int targetLayout = isTablet ? R.id.frame_left : R.id.list;
            
            Fragment fragment = FeedHeadlineListFragment.newInstance(feedId, categoryId, selectArticlesForCategory,
                    articleId);
            
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(targetLayout, fragment, FRAGMENT);
            
            // Display article in right frame:
            if (isTablet && articleId != -1000) {
                transaction.add(R.id.frame_right, ArticleFragment.newInstance(articleId, feedId, categoryId,
                        selectArticlesForCategory, ArticleFragment.ARTICLE_MOVE_DEFAULT));
            }
            
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        }
        
        initialize();
    }
    
    private void initialize() {
        Controller.getInstance().lastOpenedFeeds.add(feedId);
        Controller.getInstance().lastOpenedArticles.clear();
        fillParentInformation();
    }
    
    private void fillParentInformation() {
        FeedAdapter parentAdapter = null;
        try {
            parentAdapter = new FeedAdapter(getApplicationContext(), categoryId);
            int index = parentAdapter.getIds().indexOf(feedId);
            if (index >= 0) {
                parentIDs[0] = parentAdapter.getId(index - 1); // Previous
                parentIDs[1] = parentAdapter.getId(index + 1); // Next
                
                if (parentIDs[0] == 0)
                    parentIDs[0] = -1;
                if (parentIDs[1] == 0)
                    parentIDs[1] = -1;
            }
        } finally {
            if (parentAdapter != null)
                parentAdapter.close();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        refreshAndUpdate();
    }
    
    @Override
    protected void doRefresh() {
        super.doRefresh();
        setTitle(title);
        setUnread(unreadCount);
        Utils.doRefreshFragment(getSupportFragmentManager().findFragmentById(R.id.list));
    }
    
    @Override
    protected void doUpdate(boolean forceUpdate) {
        // Only update if no headlineUpdater already running
        if (headlineUpdater != null) {
            if (headlineUpdater.getStatus().equals(AsyncTask.Status.FINISHED)) {
                headlineUpdater = null;
            } else {
                return;
            }
        }
        
        if (!isCacherRunning()) {
            headlineUpdater = new FeedHeadlineUpdater(forceUpdate);
            headlineUpdater.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
    
    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Menu_Refresh:
                doUpdate(true);
                return true;
            case R.id.Menu_MarkAllRead:
                if (selectArticlesForCategory) {
                    new Updater(this, new ReadStateUpdater(categoryId)).exec();
                } else {
                    new Updater(this, new ReadStateUpdater(feedId, 42)).exec();
                }
                
                if (Controller.getInstance().goBackAfterMakeAllRead())
                    onBackPressed();
                
                return true;
            case R.id.Menu_FeedUnsubscribe:
                FeedUnsubscribeDialog.getInstance(this, feedId).show(getSupportFragmentManager(),
                        FeedUnsubscribeDialog.DIALOG_UNSUBSCRIBE);
            default:
                return false;
        }
    }
    
    public void openNextFeed(int direction) {
        if (feedId < 0)
            return;
        
        int id = direction < 0 ? parentIDs[0] : parentIDs[1];
        if (id <= 0) {
            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(Utils.SHORT_VIBRATE);
            return;
        }
        
        this.feedId = id;
        
        FeedHeadlineListFragment feedHeadlineView = FeedHeadlineListFragment.newInstance(feedId, categoryId,
                selectArticlesForCategory, -1000);
        
        // Replace the old fragment with the new one
        int target = isTablet ? R.id.frame_left : R.id.list;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(target, feedHeadlineView);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        
        initialize();
        doRefresh();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Controller.getInstance().useVolumeKeys()) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_N) {
                openNextFeed(-1);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_B) {
                openNextFeed(1);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (Controller.getInstance().useVolumeKeys()) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                    || keyCode == KeyEvent.KEYCODE_N || keyCode == KeyEvent.KEYCODE_B) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
    
    /**
     * Updates all articles from the selected feed.
     */
    public class FeedHeadlineUpdater extends ActivityUpdater {
        private static final int DEFAULT_TASK_COUNT = 2;
        
        public FeedHeadlineUpdater(boolean forceUpdate) {
            super(forceUpdate);
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            taskCount = DEFAULT_TASK_COUNT;
            
            int progress = 0;
            boolean displayUnread = Controller.getInstance().onlyUnread();
            
            publishProgress(++progress); // Move progress forward
            if (selectArticlesForCategory) {
                Data.getInstance().updateArticles(categoryId, displayUnread, true, false, forceUpdate);
            } else {
                Data.getInstance().updateArticles(feedId, displayUnread, false, false, forceUpdate);
            }
            publishProgress(taskCount); // Move progress forward to 100%
            return null;
        }
    }
    
    @Override
    public void itemSelected(MainListFragment source, int selectedIndex, int oldIndex, int selectedId) {
        Log.d(Utils.TAG, "itemSelected in FeedHeadlineActivity");
        
        if (isTablet) {
            
            switch (source.getType()) {
                case FEEDHEADLINE:
                    //
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_right, ArticleFragment.newInstance(selectedId, feedId, categoryId,
                            selectArticlesForCategory, ArticleFragment.ARTICLE_MOVE_DEFAULT));
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                    
                    break;
                default:
                    Toast.makeText(this, "Invalid request!", Toast.LENGTH_SHORT).show();
                    break;
            }
            return;
        }
        
        // Non-Tablet behaviour:
        Intent i = new Intent(context, ArticleActivity.class);
        i.putExtra(ArticleFragment.ARTICLE_ID, selectedId);
        i.putExtra(ArticleFragment.ARTICLE_FEED_ID, feedId);
        i.putExtra(FeedHeadlineListFragment.FEED_CAT_ID, categoryId);
        i.putExtra(FeedHeadlineListFragment.FEED_SELECT_ARTICLES, selectArticlesForCategory);
        i.putExtra(ArticleFragment.ARTICLE_MOVE, ArticleFragment.ARTICLE_MOVE_DEFAULT);
        startActivity(i);
    }
    
}
