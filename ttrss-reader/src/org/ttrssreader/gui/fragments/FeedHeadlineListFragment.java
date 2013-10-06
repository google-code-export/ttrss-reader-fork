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

import java.util.ArrayList;
import java.util.List;
import org.ttrssreader.R;
import org.ttrssreader.controllers.Controller;
import org.ttrssreader.gui.FeedHeadlineActivity;
import org.ttrssreader.gui.MenuActivity;
import org.ttrssreader.gui.TextInputAlert;
import org.ttrssreader.gui.interfaces.IItemSelectedListener.TYPE;
import org.ttrssreader.gui.interfaces.TextInputAlertCallback;
import org.ttrssreader.gui.view.MyGestureDetector;
import org.ttrssreader.model.FeedHeadlineAdapter;
import org.ttrssreader.model.pojos.Article;
import org.ttrssreader.model.updaters.PublishedStateUpdater;
import org.ttrssreader.model.updaters.ReadStateUpdater;
import org.ttrssreader.model.updaters.StarredStateUpdater;
import org.ttrssreader.model.updaters.Updater;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FeedHeadlineListFragment extends MainListFragment implements TextInputAlertCallback {
    
    protected static final TYPE THIS_TYPE = TYPE.FEEDHEADLINE;
    
    public static final String FEED_CAT_ID = "FEED_CAT_ID";
    public static final String FEED_ID = "ARTICLE_FEED_ID";
    public static final String ARTICLE_ID = "ARTICLE_ID";
    public static final String FEED_TITLE = "FEED_TITLE";
    public static final String FEED_SELECT_ARTICLES = "FEED_SELECT_ARTICLES";
    public static final String FEED_INDEX = "INDEX";
    
    private int categoryId = -1000;
    private int feedId = -1000;
    private int articleId = -1000;
    private boolean selectArticlesForCategory = false;
    
    public static FeedHeadlineListFragment newInstance(int id, int categoryId, boolean selectArticles, int articleId) {
        FeedHeadlineListFragment detail = new FeedHeadlineListFragment();
        detail.categoryId = categoryId;
        detail.feedId = id;
        detail.selectArticlesForCategory = selectArticles;
        detail.articleId = articleId;
        detail.setHasOptionsMenu(true);
        detail.setRetainInstance(true);
        return detail;
    }
    
    public static FeedHeadlineListFragment newInstance(int id, int categoryId, boolean selectArticles) {
        return newInstance(id, categoryId, selectArticles, -1000);
    }
    
    public static FeedHeadlineListFragment newInstance(FeedHeadlineListFragment old) {
        return newInstance(old.getFeedId(), old.getCategoryId(), old.getSelectArticlesForCategory(), old.getArticleId());
    }
    
    @Override
    public void onActivityCreated(Bundle instance) {
        super.onActivityCreated(instance);
        
        if (instance != null) {
            categoryId = instance.getInt(FEED_CAT_ID);
            feedId = instance.getInt(FEED_ID);
            selectArticlesForCategory = instance.getBoolean(FEED_SELECT_ARTICLES);
            articleId = instance.getInt(ARTICLE_ID);
        }
        
        // Detect touch gestures like swipe and scroll down:
        ActionBar actionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        gestureDetector = new GestureDetector(getActivity(), new HeadlineGestureDetector(actionBar, Controller
                .getInstance().hideActionbar()));
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        getView().setOnTouchListener(gestureListener);
        
        adapter = new FeedHeadlineAdapter(getActivity(), feedId, categoryId, selectArticlesForCategory);
        setListAdapter(adapter);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(FEED_CAT_ID, categoryId);
        outState.putInt(FEED_ID, feedId);
        outState.putBoolean(FEED_SELECT_ARTICLES, selectArticlesForCategory);
        outState.putInt(ARTICLE_ID, articleId);
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        
        // Get selected Article
        AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        Article a = (Article) adapter.getItem(info.position);
        menu.removeItem(MenuActivity.MARK_READ); // Remove "Mark read" from super-class
        
        menu.add(MenuActivity.MARK_GROUP, MenuActivity.MARK_ABOVE_READ, Menu.NONE, R.string.Commons_MarkAboveRead);
        
        if (a.isUnread) {
            menu.add(MenuActivity.MARK_GROUP, MenuActivity.MARK_READ, Menu.NONE, R.string.Commons_MarkRead);
        } else {
            menu.add(MenuActivity.MARK_GROUP, MenuActivity.MARK_READ, Menu.NONE, R.string.Commons_MarkUnread);
        }
        
        if (a.isStarred) {
            menu.add(MenuActivity.MARK_GROUP, MenuActivity.MARK_STAR, Menu.NONE, R.string.Commons_MarkUnstar);
        } else {
            menu.add(MenuActivity.MARK_GROUP, MenuActivity.MARK_STAR, Menu.NONE, R.string.Commons_MarkStar);
        }
        
        if (a.isPublished) {
            menu.add(MenuActivity.MARK_GROUP, MenuActivity.MARK_PUBLISH, Menu.NONE, R.string.Commons_MarkUnpublish);
        } else {
            menu.add(MenuActivity.MARK_GROUP, MenuActivity.MARK_PUBLISH, Menu.NONE, R.string.Commons_MarkPublish);
            menu.add(MenuActivity.MARK_GROUP, MenuActivity.MARK_PUBLISH_NOTE, Menu.NONE,
                    R.string.Commons_MarkPublishNote);
        }
        
        menu.add(MenuActivity.MARK_GROUP, MenuActivity.SHARE, Menu.NONE, R.string.ArticleActivity_ShareLink);
    }
    
    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterContextMenuInfo cmi = (AdapterContextMenuInfo) item.getMenuInfo();
        if (cmi == null)
            return false;
        
        Article a = (Article) adapter.getItem(cmi.position);
        if (a == null)
            return false;
        
        switch (item.getItemId()) {
            case MenuActivity.MARK_READ:
                new Updater(this, new ReadStateUpdater(a, feedId, a.isUnread ? 0 : 1)).exec();
                break;
            case MenuActivity.MARK_STAR:
                new Updater(this, new StarredStateUpdater(a, a.isStarred ? 0 : 1)).exec();
                break;
            case MenuActivity.MARK_PUBLISH:
                new Updater(this, new PublishedStateUpdater(a, a.isPublished ? 0 : 1)).exec();
                break;
            case MenuActivity.MARK_PUBLISH_NOTE:
                new TextInputAlert(this, a).show(getActivity());
                break;
            case MenuActivity.MARK_ABOVE_READ:
                new Updater(this, new ReadStateUpdater(getUnreadArticlesAbove(cmi.position), feedId, 0)).exec();
                break;
            case MenuActivity.SHARE:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, a.url);
                i.putExtra(Intent.EXTRA_SUBJECT, a.title);
                startActivity(Intent.createChooser(i, (String) getText(R.string.ArticleActivity_ShareTitle)));
                break;
            default:
                return false;
        }
        return true;
    }
    
    /**
     * Creates a list of articles which are above the given index in the currently displayed list of items.
     * 
     * @param index
     *            the selected index, will be excluded in returned list
     * @return a list of items above the selected item
     */
    private List<Article> getUnreadArticlesAbove(int index) {
        List<Article> ret = new ArrayList<Article>();
        for (int i = 0; i < index; i++) {
            Article a = (Article) adapter.getItem(i);
            if (a != null && a.isUnread)
                ret.add(a);
        }
        return ret;
    }
    
    public void onPublishNoteResult(Article a, String note) {
        new Updater(this, new PublishedStateUpdater(a, a.isPublished ? 0 : 1, note)).exec();
    }
    
    @Override
    public TYPE getType() {
        return THIS_TYPE;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public int getFeedId() {
        return feedId;
    }
    
    public int getArticleId() {
        return articleId;
    }
    
    public boolean getSelectArticlesForCategory() {
        return selectArticlesForCategory;
    }
    
    class HeadlineGestureDetector extends MyGestureDetector {
        public HeadlineGestureDetector(ActionBar actionBar, boolean hideActionbar) {
            super(actionBar, hideActionbar);
        }
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Refresh metrics-data in Controller
            Controller.refreshDisplayMetrics(((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay());
            
            try {
                if (Math.abs(e1.getY() - e2.getY()) > Controller.relSwipeMaxOffPath)
                    return false;
                if (e1.getX() - e2.getX() > Controller.relSwipeMinDistance
                        && Math.abs(velocityX) > Controller.relSwipteThresholdVelocity) {
                    
                    // right to left swipe
                    ((FeedHeadlineActivity) getActivity()).openNextFeed(1);
                    
                } else if (e2.getX() - e1.getX() > Controller.relSwipeMinDistance
                        && Math.abs(velocityX) > Controller.relSwipteThresholdVelocity) {
                    
                    // left to right swipe
                    ((FeedHeadlineActivity) getActivity()).openNextFeed(-1);
                    
                }
            } catch (Exception e) {
            }
            return false;
        }
    };
    
}
