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

import java.util.Set;
import org.ttrssreader.R;
import org.ttrssreader.controllers.Controller;
import org.ttrssreader.controllers.DBHelper;
import org.ttrssreader.controllers.Data;
import org.ttrssreader.controllers.NotInitializedException;
import org.ttrssreader.gui.interfaces.IUpdateEndListener;
import org.ttrssreader.gui.view.ArticleHeaderView;
import org.ttrssreader.gui.view.ArticleView;
import org.ttrssreader.gui.view.ArticleWebViewClient;
import org.ttrssreader.imageCache.ImageCacher;
import org.ttrssreader.model.FeedHeadlineAdapter;
import org.ttrssreader.model.pojos.Article;
import org.ttrssreader.model.updaters.PublishedStateUpdater;
import org.ttrssreader.model.updaters.ReadStateUpdater;
import org.ttrssreader.model.updaters.StarredStateUpdater;
import org.ttrssreader.model.updaters.StateSynchronisationUpdater;
import org.ttrssreader.model.updaters.Updater;
import org.ttrssreader.utils.StringSupport;
import org.ttrssreader.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class ArticleActivity extends Activity implements IUpdateEndListener {
    
    public static final String ARTICLE_ID = "ARTICLE_ID";
    public static final String ARTICLE_FEED_ID = "ARTICLE_FEED_ID";
    
    public static final String ARTICLE_MOVE = "ARTICLE_MOVE";
    public static final int ARTICLE_MOVE_NONE = 0;
    public static final int ARTICLE_MOVE_DEFAULT = ARTICLE_MOVE_NONE;
    
    // Extras
    private int articleId = -1;
    private int feedId = -1;
    private int categoryId = -1000;
    private boolean selectArticlesForCategory = false;
    private int lastMove = ARTICLE_MOVE_DEFAULT;
    
    private ArticleHeaderView headerContainer;
    private ArticleView mainContainer;
    
    private Article article = null;
    private String content;
    private boolean linkAutoOpened;
    
    private WebView webView;
    private TextView swipeView;
    private Button buttonNext;
    private Button buttonPrev;
    private GestureDetector mGestureDetector;
    
    private String baseUrl = null;
    
    private FeedHeadlineAdapter parentAdapter = null;
    
    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        Log.d(Utils.TAG, "onCreate - ArticleActivity");
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
        Controller.getInstance().checkAndInitializeController(this);
        Controller.refreshDisplayMetrics(getWindowManager().getDefaultDisplay());
        DBHelper.getInstance().checkAndInitializeDB(this);
        Data.getInstance().checkAndInitializeData(this);
        
        if (Controller.getInstance().displayArticleHeader())
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.articleitem);
        headerContainer = (ArticleHeaderView) findViewById(R.id.article_header_container);
        mainContainer = (ArticleView) findViewById(R.id.article_main_layout);
        
        webView = (WebView) findViewById(R.id.webView);
        buttonPrev = (Button) findViewById(R.id.buttonPrev);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        swipeView = (TextView) findViewById(R.id.swipeView);
        
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new ArticleWebViewClient(this));
        
        // TODO: Use this to reposition the zoom-buttons?
        // final View zoom = webView.getZoomControls();
        // zoom.setLayoutParams(params)
        
        // Detect gestures
        mGestureDetector = new GestureDetector(onGestureListener);
        webView.setOnKeyListener(keyListener);
        
        buttonNext.setOnClickListener(onButtonPressedListener);
        buttonPrev.setOnClickListener(onButtonPressedListener);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            articleId = extras.getInt(ARTICLE_ID);
            feedId = extras.getInt(ARTICLE_FEED_ID);
            categoryId = extras.getInt(FeedHeadlineActivity.FEED_CAT_ID);
            selectArticlesForCategory = extras.getBoolean(FeedHeadlineActivity.FEED_SELECT_ARTICLES);
            lastMove = extras.getInt(ARTICLE_MOVE);
        } else if (instance != null) {
            articleId = instance.getInt(ARTICLE_ID);
            feedId = instance.getInt(ARTICLE_FEED_ID);
            categoryId = instance.getInt(FeedHeadlineActivity.FEED_CAT_ID);
            selectArticlesForCategory = instance.getBoolean(FeedHeadlineActivity.FEED_SELECT_ARTICLES);
            lastMove = instance.getInt(ARTICLE_MOVE);
        }
        
        Controller.getInstance().lastOpenedFeed = feedId;
        Controller.getInstance().lastOpenedArticle = articleId;
        parentAdapter = new FeedHeadlineAdapter(getApplicationContext(), feedId, categoryId, selectArticlesForCategory);
        doVibrate(0);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        DBHelper.getInstance().checkAndInitializeDB(this);
        doRefresh();
    }
    
    private void closeCursor() {
        if (parentAdapter != null) {
            parentAdapter.closeCursor();
        }
    }
    
    @Override
    protected void onPause() {
        // First call super.onXXX, then do own clean-up. It actually makes a difference but I got no idea why.
        super.onPause();
        closeCursor();
    }
    
    @Override
    protected void onStop() {
        // Check again to make sure it didnt get updated and marked as unread again in the background
        if (article != null && article.isUnread && Controller.getInstance().automaticMarkRead())
            new Updater(null, new ReadStateUpdater(article, feedId, 0)).execute();
        super.onStop();
        closeCursor();
    }
    
    @Override
    protected void onDestroy() {
        // Check again to make sure it didnt get updated and marked as unread again in the background
        if (article != null && article.isUnread && Controller.getInstance().automaticMarkRead())
            new Updater(null, new ReadStateUpdater(article, feedId, 0)).execute();
        super.onDestroy();
        closeCursor();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARTICLE_ID, articleId);
        outState.putInt(ARTICLE_FEED_ID, feedId);
        outState.putInt(FeedHeadlineActivity.FEED_CAT_ID, categoryId);
        outState.putBoolean(FeedHeadlineActivity.FEED_SELECT_ARTICLES, selectArticlesForCategory);
        outState.putInt(ARTICLE_MOVE, lastMove);
    }
    
    private void doRefresh() {
        setProgressBarIndeterminateVisibility(true);
        
        if (Controller.getInstance().workOffline()) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        
        try {
            if (!Controller.getInstance().getConnector().hasLastError()) {
                article = DBHelper.getInstance().getArticle(articleId);
                
                if (article != null && article.content != null) {
                    
                    // Populate information-bar on top of the webView if enabled
                    if (Controller.getInstance().displayArticleHeader()) {
                        headerContainer.populate(article);
                    } else {
                        headerContainer.setVisibility(View.GONE);
                    }
                    
                    // Initialize mainContainer with buttons or swipe-view
                    mainContainer.populate();
                    
                    // Inject the specific code for attachments, <img> for images, http-link for Videos
                    StringBuilder contentTmp = injectAttachments(getApplicationContext(), new StringBuilder(
                            article.content), article.attachments);
                    
                    // if (article.cachedImages)
                    // Do this anyway, article.cachedImages can be true also if some images were fetched and others
                    // produced errors
                    contentTmp = injectCachedImages(contentTmp, articleId);
                    final int contentLength = contentTmp.length();
                    contentTmp = injectArticleLink(contentTmp);
                    content = contentTmp.toString();
                    
                    // Load html from Controller and insert content
                    String text = Controller.htmlHeader.replace("MARKER", content);
                    
                    // TODO
                    if (Controller.getInstance().darkBackground()) {
                        webView.setBackgroundColor(Color.BLACK);
                        text = "<font color='white'>" + text + "</font>";
                        
                        setDarkBackground(headerContainer);
                    }
                    
                    // Use if loadDataWithBaseURL, 'cause loadData is buggy (encoding error & don't support "%" in
                    // html).
                    baseUrl = StringSupport.getBaseURL(article.url);
                    webView.loadDataWithBaseURL(baseUrl, text, "text/html", "utf-8", "about:blank");
                    
                    setTitle(article.title);
                    
                    if (article.isUnread && Controller.getInstance().automaticMarkRead())
                        new Updater(null, new ReadStateUpdater(article, feedId, 0)).execute();
                    
                    if (!linkAutoOpened && contentLength < 3) {
                        if (Controller.getInstance().openUrlEmptyArticle()) {
                            Log.i(Utils.TAG, "Article-Content is empty, opening URL in browser");
                            linkAutoOpened = true;
                            openLink();
                        }
                    }
                    
                }
            } else {
                openConnectionErrorDialog(Controller.getInstance().getConnector().pullLastError());
            }
        } catch (NotInitializedException e) {
        }
        
        setProgressBarIndeterminateVisibility(false);
    }
    
    /**
     * Recursively walks all viewGroups and their Views inside the given ViewGroup and sets the background to black and,
     * in case a TextView is found, the Text-Color to white.
     * 
     * @param v
     *            the ViewGroup to walk through
     */
    private void setDarkBackground(ViewGroup v) {
        v.setBackgroundColor(Color.BLACK);
        
        for (int i = 0; i < v.getChildCount(); i++) { // View at index 0 seems to be this view itself.
            View vChild = v.getChildAt(i);
            
            if (vChild == null || vChild.getId() == v.getId())
                continue;
            
            if (vChild instanceof TextView)
                ((TextView) vChild).setTextColor(Color.WHITE);
            
            if (vChild instanceof ViewGroup)
                setDarkBackground(((ViewGroup) vChild));
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.article, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        
        if (article == null)
            return true;
        
        MenuItem read = menu.findItem(R.id.Article_Menu_MarkRead);
        if (article.isUnread) {
            read.setTitle(getString(R.string.Commons_MarkRead));
        } else {
            read.setTitle(getString(R.string.Commons_MarkUnread));
        }
        
        MenuItem publish = menu.findItem(R.id.Article_Menu_MarkStar);
        if (article.isStarred) {
            publish.setTitle(getString(R.string.Commons_MarkUnstar));
        } else {
            publish.setTitle(getString(R.string.Commons_MarkStar));
        }
        
        MenuItem star = menu.findItem(R.id.Article_Menu_MarkPublish);
        if (article.isPublished) {
            star.setTitle(getString(R.string.Commons_MarkUnpublish));
        } else {
            star.setTitle(getString(R.string.Commons_MarkPublish));
        }
        
        MenuItem offline = menu.findItem(R.id.Article_Menu_WorkOffline);
        if (Controller.getInstance().workOffline()) {
            offline.setTitle(getString(R.string.UsageOnlineTitle));
            offline.setIcon(R.drawable.ic_menu_play_clip);
        } else {
            offline.setTitle(getString(R.string.UsageOfflineTitle));
            offline.setIcon(R.drawable.ic_menu_stop);
        }
        
        return true;
    }
    
    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Article_Menu_MarkRead:
                new Updater(null, new ReadStateUpdater(article, feedId, article.isUnread ? 0 : 1)).execute();
                return true;
            case R.id.Article_Menu_MarkStar:
                new Updater(null, new StarredStateUpdater(article, article.isStarred ? 0 : 1)).execute();
                return true;
            case R.id.Article_Menu_MarkPublish:
                new Updater(null, new PublishedStateUpdater(article, article.isPublished ? 0 : 1)).execute();
                return true;
            case R.id.Article_Menu_WorkOffline:
                Controller.getInstance().setWorkOffline(!Controller.getInstance().workOffline());
                if (!Controller.getInstance().workOffline()) {
                    // Synchronize status of articles with server
                    new Updater(this, new StateSynchronisationUpdater()).execute((Void[]) null);
                }
                return true;
            case R.id.Article_Menu_OpenLink:
                openLink();
                return true;
            case R.id.Article_Menu_ShareLink:
                String content = (String) getText(R.string.ArticleActivity_ShareSubject);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                
                if (article != null) {
                    i.putExtra(Intent.EXTRA_TEXT, content + " " + article.url);
                    i.putExtra(Intent.EXTRA_SUBJECT, article.title);
                }
                
                startActivity(Intent.createChooser(i, (String) getText(R.string.ArticleActivity_ShareTitle)));
                return true;
            default:
                return false;
        }
    }
    
    private void openLink() {
        if (article != null) {
            if ((article.url != null) && (article.url.length() > 0)) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(article.url));
                startActivity(i);
            }
        }
    }
    
    public void onZoomChanged() {
        // Load html from Raw-Ressources and insert content
        String temp = getResources().getString(R.string.INJECT_HTML_HEAD_ZOOM);
        if (content == null)
            content = "";
        String text = temp.replace("MARKER", content);
        webView.loadDataWithBaseURL(baseUrl, text, "text/html", "utf-8", "about:blank");
    }
    
    private void openNextArticle(int direction) {
        int newIndex = getCurrentIndex() + direction;
        
        // Check: No more articles in this direction?
        if (doVibrate(newIndex))
            return;
        
        Intent i = new Intent(this, ArticleActivity.class);
        i.putExtra(ARTICLE_ID, parentAdapter.getId(newIndex));
        i.putExtra(ARTICLE_FEED_ID, feedId);
        i.putExtra(FeedHeadlineActivity.FEED_CAT_ID, categoryId);
        i.putExtra(FeedHeadlineActivity.FEED_SELECT_ARTICLES, selectArticlesForCategory);
        i.putExtra(ARTICLE_MOVE, direction); // Store direction so next article can evaluate if we are running into
                                             // a "wall"
        
        startActivityForResult(i, 0);
        finish();
    }
    
    private int getCurrentIndex() {
        int currentIndex = -2; // -2 so index is still -1 if direction is +1, avoids moving when no move possible
        int tempIndex = parentAdapter.getIds().indexOf(articleId);
        if (tempIndex >= 0)
            currentIndex = tempIndex;
        return currentIndex;
    }
    
    private boolean doVibrate(int newIndex) {
        int tempIndex = 0;
        
        if (newIndex == 0) {
            if (lastMove != 0) {
                tempIndex = getCurrentIndex() + lastMove;
            }
        } else {
            tempIndex = newIndex;
        }
        
        if (tempIndex < 0 || tempIndex >= parentAdapter.getCount()) {
            if (Controller.getInstance().vibrateOnLastArticle())
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(Utils.SHORT_VIBRATE);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        boolean temp = super.dispatchTouchEvent(e);
        
        if (Controller.getInstance().useSwipe())
            return mGestureDetector.onTouchEvent(e);
        else
            return temp;
    }
    
    private OnGestureListener onGestureListener = new OnGestureListener() {
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!Controller.getInstance().useSwipe())
                return false;
            
            float movement = 0;
            boolean isSwipe = false;
            
            int dx = (int) (e2.getX() - e1.getX());
            int dy = (int) (e2.getY() - e1.getY());
            
            if (Controller.landscape) {
                
                // LANDSCAPE
                // Don't accept the fling if it's too short as it may conflict with a button push
                if (Math.abs(dy) > Controller.swipeHeight && Math.abs(velocityY) > Math.abs(velocityX))
                    isSwipe = true;
                
                if (Math.abs(dx) > (int) (Controller.absWidth * 0.30))
                    return false; // Too much X-Movement (30% of screen-width)
                    
                // Check if Swipe-Motion is inside the Swipe-Area
                if (Controller.getInstance().leftHanded()) {
                    
                    // Swipe-Area on LEFT side of the screen
                    int swipeAreaPosition = Controller.swipeAreaWidth;
                    if (e1.getX() > swipeAreaPosition || e2.getX() > swipeAreaPosition) {
                        if (isSwipe) {
                            // Display text for swipe-area
                            swipeView.setVisibility(TextView.VISIBLE);
                            new Handler().postDelayed(timerTask, 1000);
                        }
                        return false;
                    }
                    
                } else {
                    
                    // Swipe-Area on RIGHT side of the screen
                    int swipeAreaPosition = webView.getWidth() - Controller.swipeAreaWidth;
                    if (e1.getX() < swipeAreaPosition || e2.getX() < swipeAreaPosition) {
                        if (isSwipe) {
                            // Display text for swipe-area
                            swipeView.setVisibility(TextView.VISIBLE);
                            new Handler().postDelayed(timerTask, 1000);
                        }
                        return false;
                    }
                    
                }
                
                if (isSwipe)
                    movement = velocityY;
                
            } else {
                
                // PORTRAIT
                int SWIPE_BOTTOM = webView.getHeight() - Controller.swipeAreaHeight;
                
                // Don't accept the fling if it's too short as it may conflict with a button push
                if (Math.abs(dx) > Controller.swipeWidth && Math.abs(velocityX) > Math.abs(velocityY))
                    isSwipe = true;
                
                if (Math.abs(dy) > (int) (Controller.absHeight * 0.2))
                    return false; // Too much Y-Movement (20% of screen-height)
                    
                // Check if Swipe-Motion is inside the Swipe-Area
                if (e1.getY() < SWIPE_BOTTOM || e2.getY() < SWIPE_BOTTOM) {
                    if (isSwipe) {
                        // Display text for swipe-area
                        swipeView.setVisibility(TextView.VISIBLE);
                        new Handler().postDelayed(timerTask, 1000);
                    }
                    return false;
                }
                
                if (isSwipe)
                    movement = velocityX;
            }
            
            if (isSwipe && movement != 0) {
                if (movement > 0)
                    openNextArticle(-1);
                else
                    openNextArticle(1);
                
                return true;
            }
            return false;
            
        }
        
        // @formatter:off
        private Runnable timerTask = new Runnable() {
            public void run() { // Need this to set the text invisible after some time
                swipeView.setVisibility(TextView.INVISIBLE);
            }
        };
        @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
        @Override public boolean onSingleTapUp(MotionEvent e) { return false; }
        @Override public boolean onDown(MotionEvent e) { return false; }
        @Override public void onLongPress(MotionEvent e) { }
        @Override public void onShowPress(MotionEvent e) { }
        // @formatter:on
    };
    
    private OnClickListener onButtonPressedListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.equals(buttonNext))
                openNextArticle(-1);
            else if (v.equals(buttonPrev))
                openNextArticle(1);
        }
    };
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Controller.getInstance().useVolumeKeys()) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                openNextArticle(-1);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                openNextArticle(1);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (Controller.getInstance().useVolumeKeys()) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    
    private OnKeyListener keyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (Controller.getInstance().useVolumeKeys()) {
                if (keyCode == KeyEvent.KEYCODE_N) {
                    openNextArticle(-1);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_B) {
                    openNextArticle(1);
                    return true;
                }
            }
            return false;
        }
    };
    
    private void openConnectionErrorDialog(String errorMessage) {
        Intent i = new Intent(this, ErrorActivity.class);
        i.putExtra(ErrorActivity.ERROR_MESSAGE, errorMessage);
        startActivityForResult(i, ErrorActivity.ACTIVITY_SHOW_ERROR);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ErrorActivity.ACTIVITY_SHOW_ERROR)
            doRefresh();
    }
    
    private static StringBuilder injectAttachments(Context context, StringBuilder content, Set<String> attachments) {
        if (content == null)
            content = new StringBuilder();
        
        for (String url : attachments) {
            if (url.length() == 0)
                continue;
            
            boolean image = false;
            for (String s : Utils.IMAGE_EXTENSIONS) {
                if (url.toLowerCase().contains("." + s))
                    image = true;
            }
            
            boolean audioOrVideo = false;
            for (String s : Utils.MEDIA_EXTENSIONS) {
                if (url.toLowerCase().contains("." + s))
                    audioOrVideo = true;
            }
            
            content.append("<br>\n");
            if (image) {
                content.append("<img src=\"").append(url).append("\" /><br>\n");
            } else if (audioOrVideo) {
                content.append("<a href=\"").append(url).append("\">");
                content.append((String) context.getText(R.string.ArticleActivity_MediaPlay)).append("</a>");
            } else {
                content.append("<a href=\"").append(url).append("\">");
                content.append((String) context.getText(R.string.ArticleActivity_MediaDisplayLink)).append("</a>");
            }
        }
        return content;
    }
    
    private StringBuilder injectArticleLink(StringBuilder html) {
        if (html == null)
            html = new StringBuilder();
        if (article != null) {
            if ((article.url != null) && (article.url.length() > 0)) {
                html.append("<br>\n");
                html.append("<a href=\"").append(article.url).append("\" rel=\"alternate\">");
                html.append("*** article ***").append("</a>"); // TODO: internationalization
            }
        }
        return html;
    }
    
    /**
     * Injects the local path to every image which could be found in the local cache, replacing the original URLs in the
     * html.
     * 
     * @param html
     *            the original html
     * @return the altered html with the URLs replaced so they point on local files if available
     */
    private static StringBuilder injectCachedImages(StringBuilder html, int articleId) {
        if (html == null || html.length() < 40)
            return html;
        
        for (String url : ImageCacher.findAllImageUrls(html.toString(), articleId)) {
            String localUrl = ImageCacher.getCachedImageUrl(url);
            if (localUrl != null) {
                replace(html, url, localUrl);
            }
        }
        return html;
    }
    
    private static void replace(StringBuilder s, String target, String replacement) {
        final boolean oldway = true;
        if (oldway) {
            String stmp = s.toString();
            stmp.replace(target, replacement);
            s = new StringBuilder(stmp);
        } else {
            // TODO: find out why this is broken.
            int searchStart = 0;
            final int targetLength = target.length();
            final int replacementLength = replacement.length();
            while (true) {
                int begin = s.indexOf(target, searchStart);
                if (begin < 0) {
                    break;
                } else {
                    s.replace(begin, begin + targetLength, replacement);
                    searchStart = begin + replacementLength;
                }
            }
        }
    }
    
    @Override
    public void onUpdateEnd() {
        // Not necessary here
    }
    
    @Override
    public void onUpdateProgress() {
        // Not necessary here
    }
    
}
