# Changes #
## Version 1.81 (1) ##
  * Attempt to build with PRNGFixes again (fixes [Issue #264](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#264))
  * Fixed [Issue #253](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#253): "Adding a new feed will put it in a random category"
  * Fixed some FCs ([Issue #266](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#266))
  * Fixed [Issue #267](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#267): Problem with digest auth via http auth

## Version 1.80 ##
  * Revert to default implementation of SecureRandom because the suggested bugfix from Google (http://android-developers.blogspot.de/2013/08/some-securerandom-thoughts.html) seems to crash on some devices. Due to this the SSL implementation "may not receive cryptographically strong values". I'm investigating solutions for this. Thank you for your patience. :)

## Version 1.79 (2) ##
  * Flash screen when notify is necessary and device has no vibrator
  * Store size of tablet view per feed
  * Fixed tab resizing in horizontal mode
  * Make sure to use only one SQLite Connection throughout the whole app. Makes the UI quite smooth and you can start reading while updates are fetched in the background which was quite annoying before.
  * Removed lots of dead code
  * Moved some DB-Access into background tasks, missed these bits before.
  * Fixed some more FCs
  * Fixed "Accept all certificates" not working ([Issue #263](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#263))
  * Faster startup

## Version 1.78 ##
  * Enable API-Level 21 (Lollipop, 5.0)
  * Fixed several bugs regarding the improved TLS-security ([Issue #233](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#233))
  * Enable SSL- and HTTP-Auth-Preferences to be specified per Wifi network too ([Issue #261](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#261))
  * Fixed download of attached media files ([Issue #256](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#256))

## Version 1.77 (2) ##
  * Added support for TLS v1.3, dropped support for SSL v3
  * Fixed problems with ImageCache not downloading images
  * Added menuitem to filter "articles with cached images"
  * Fixed some locking problems
  * Updated translations, license statements and thanks
  * Dropped support for android versions below 4.0 (Api-Level 14)
  * Fixed SSL problems ([Issue #258](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#258))
  * Fixed SSL problems ([Issue #233](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#233))

## Version 1.76 ##
  * Fix swiping on kitkat
  * Enable hardwareAcceleration in Manifest
  * Clean up references on context in WebViewClient
  * Add Version-Code to about-box for easier debugging
  * Removed preference "MarkReadInMenu" since there is a dialog before marking multiple feeds as read.

## Version 1.75 ##
  * Fixed some FCs
  * Fixed broken themes
  * Fixed PRNG used for SSL connections
  * Fixed resizing of views in tablet layout

## Version 1.74 ##
  * Added preference for min and max size of cached files
  * Increased length of max size of ImageCache
  * Fixed some bugs regarding caching of files
  * Fixed crashes after rotating while dialogs are visible

## Version 1.73 (4) ##
  * Fixed "Go back after mark all read"
  * Added style for blockquote-tag
  * Improved logging and error reporting
  * Add "Copy URL to clipboard" option to context menu of URLs
  * Fixed long-press on feed getting redirected to category
  * Fixed duplicate virtual categories when no unread articles exist
  * Attempted fix for large tablets getting no tablet layout
  * Fixed bug that prevented users of api level < 11 from opening articles
  * Fixed wrong action bar items beeing displayed
  * Fixed NPE on article open
  * Fixed some more FCs
  * Improved refresh of starred/published articles

## Version 1.72 (2) ##
  * Implemented better error reporting mechanism
  * Modified tablet layout, reduced complexity of code
  * Bugfixes
  * Added author to html template if available

## Version 1.71 ##
  * Moved lots of stuff to the background so the UI should be more responsive
  * Fixed several bugs introduced with the christmas-update
  * Fixed a bug that slowed down article fetching quite much

## Version 1.7 ##
  * Completely rewrote DB-Adapters for lists, cursors are now managed by the system which should reduce the number of FCs
  * Added support for themes, initially there are two dark (dark and completely black) and two light (light and white) themes available
  * More lightweight operation when moving between articles/feeds
  * Make panels in two-pane layout resizable (divider can be moved)
  * Fixed some bugs in handling of labels but there are still server-side bugs
  * Fixed [bug #197](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=197): App hangs when "mark all as read" is hit
  * Fixed [bug #161](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=161): "Fresh articles" display all unread articles
  * Fixed [bug #143](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=143): New preference for "Online only when on WiFi"
  * Fixed [bug #204](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=204): some preferences don't update immediately
  * Scroll headlines when using two-pane layout and opening next article
  * Add warning dialog before marking several or all feeds as read
  * Security-Bugfix for fragment vulnerability

## Version 1.63 ##
  * Enabled zoom controls again. If they are enabled in preferences the gestures in article-view (swipe and scroll up/down to show/hide ActionBar) won't work anymore!
  * Make frames of tablet-layout resizable

## Version 1.62 (1) ##
  * Fixed some rotation related issues, app should not crash anymore but it might display wrong stuff sometimes
  * Fixed crash when entering preferences on some old devices
  * Fixed crash with foreign key error on article deletion
  * Add information about articles last sync time to "about" dialog
  * Fixed Swipe
  * Several bugfixes

## Version 1.61 (1) ##
  * Added preference to disable split-view (swipe does not work yet)
  * Bugfixes

## Version 1.6 ##
  * Added two-pane tablet-layout
  * Added preference to set some preference based on which wifi-network is currently used
  * Many changes to ImageCache and DB-access
  * Updated german translation (thanks to Martin Mueller)

## Version 1.53 (7) ##
  * Added all drawables for different screen-sizes
  * Modified "Subscribe to feed" and "Share to published" layouts
  * Allow API-Level 18
  * Fix [Issue #169](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#169): Published Articles Won't Sync (Removed loop which was only necessary to reduce memory requirement, had to be removed to allow marking all articles "unstarred" and then importing the newly requested articles.)
  * Fix [Issue #170](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#170): Mark as Read in All Articles no longer works (Modified new markRead() method in DBHelper to work with virtual categories (all, fresh, published, starred))
  * Fix [Issue #168](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#168): "Mark articles above read" no longer works as of v 1.52 (Small Bugfix)
  * Fix [Issue #172](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#172): Can't view image alt-text (Small Bugfix, store content again to allow HTML-parsing for ALT-Text of images)
  * French translation (thanks to Jean-François BILGER)
  * Fixed bug in ImageCache
  * Fixed NPE and a bug in SubscribeActivity
  * Added preference to disable hiding of the actionbar
  * Fixed wrong apilevel in SubscribeActivity

## Version 1.52 (2) ##
  * Improvements by Igor Lyubimov (Hyphenation, HTML-Template, performance, date/time formatting, button navigation)
  * Hide ActionBar while scrolling down
  * Better dark/light themes for articles
  * Performance: faster refresh on startup
  * Counters managed locally (reduces server communication)
  * ActionBar buttons: read/unread, star, publish, share
  * Bugfixes

## Version 1.51 (1) ##
  * Replaced article-header with a custom actionbar-layout (click title to view the full title if it doesn't fit in the space)
  * Reduce memory usage when low-memory conditions are detected
  * Bugfixes
  * Resolved locking issue which prevented the ImageCache from ever finishing

## Version 1.50 ##
  * Fixed [Issue 150](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=150): sub date/time options in preferences menu do not auto-disable
  * Fixed [Issue 151](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=151): handling of disabled date/time format string
  * Fixed [Issue 155](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=155): category totals often don't update correctly
  * Fixed [Issue 140](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=140): View image captions (longpress on image)
  * Fixed rotation of article with dark background
  * Fixed [Issue 145](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=145): starred list won't fully sync
  * Temporary workaround for [Issue 144](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=144): No menu button when viewing articles

## Version 1.49 ##
  * Added a preference to "Invert browsing articles"
  * Fixed problems with the new preference-dialog
  * Added confirmation dialog to unsubscribe from feed

## Version 1.48 (1) ##
  * New Preference-Layout, new Icons, fixed compatibility with old devices
  * Fixed progressbar for ImageCache
  * Fixed Exceptions when returning from article
  * Fixed problem with ImageCache
  * Added spanish translation (thanks to Eduardo Ruiz from http://www.edubox.org)

## Version 1.47 (2) ##
  * Fixed Activity-Indicators
  * Implemented "Subscribe to Feed" and "Unsubscribe" (only Server > 1.7.6)
  * Added Paste-Button for URLs in Subscribe-View
  * Dark background now without white border
  * Link-color for dark background set to light blue
  * Articleheader layout and borders fixed
  * Removed unnecessary preferences
  * Bugfixes

## Version 1.46 (3) ##
  * Fixed bugs in new connection method
  * Rewrote code for swipe, should work better now on all screen sizes
  * New Preference: Scale article font by n% (Fixed for old api levels)
  * Fixed bugs in tasker integration
  * Disabled automatic vacuum, this can still be started manually via preferences
  * Added long-press options in article, share article and (if selected an URL) share URL.
  * Fixed HTML entities in titles (only for newly fetched articles, I store them with decoded title instead of decoding it everytime it is displayed)

## Version 1.45 ##
  * Remember reading position inside articles
  * Reduce memory usage
  * Several bugfixes

## Version 1.44 ##
  * Add preference for fallback to old connector which supports digest authentication but doesn't support SNI
  * Fixed http-auth for apache connector
  * Added preference to hide the button "Mark Read" from the actionbar
  * Restore position in list after orientation change
  * Fixed errors in ShareToPublish and Tasker-Integration

## Version 1.43 ##
  * Added ability to schedule updates with Tasker/Locale

## Version 1.42 ##
  * Switching to next or previous articles/feeds is much faster now
  * Major refactoring to make use of fragments and someday be able to implement a two-pane-layout...
  * Fixed several Bugs, closed forgotten cursors and layout-problems
  * Switched HTTP-Library (Apache -> HttpUrlConnection) for SSL-SNI Support
  * Minor bugfixes

## Version 1.41 ##
  * Added "Share to Published" ability, just select TTRSS when you want to share any content to your published-feed.

## Version 1.4 ##
  * Added Label-Editing for articles
  * Fixed some bugs preventing labels from beeing refreshed properly
  * Fixed some UI glitches concerning the progressbar

## Version 1.39 ##
  * Added preference for "Load images" which, when disabled, forces the Webview to not load any content from the web.
  * Fixed [Issue #99](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#99) "Sharing with Firefox Sync"
  * Implemented [Issue #81](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#81): Show temporarily read items in "new articles", the read articles now are all left in the displayed list.
  * Longer update-interval for background refresh

## Version 1.38 ##
  * Adapt to API-changes, number of articles to be fetched at once has been reduced to 60, got no idea why but am now dealing with it properly.
  * Fixed some bugs in the mechanism to fetch new articles.

## Version 1.37 ##
  * Changed events, refreshing of the UI should now be really fast
  * Changed update-mechanism
  * Fixed refresh-button
  * Added separate Build for old devices (below Android 2.1)
  * Bugfixes

## Version 1.36 ##
  * Notify user about upcoming DB-clean-up (VACUUM)
  * Improve DB-Locking by enabling it (please don't judge me, it's been years since I wrote the line db.setLockingEnabled(false))
  * UI refreshes again when items change (eg. are marked as read, published)
  * Added icons from SDK so all icons look the same
  * Cleaned up the ActionBar
  * Now using API-Level 17

## Version 1.35 ##
  * Articles are again marked as read when beeing read in the webinterface as long as there are new articles to be synced (API doesn't offer "changes since date/time", I can only guess what has changed)
  * Added long-press menu in Article-List: "Mark everything above as read"
  * Another attempt at fixing the IllegalStateExceptions
  * Small bugfixes
  * I need help regarding the SQLiteException "cannot commit transaction - SQL statements in progress: COMMIT;", please contact me if you think you can give any advice on this issue.

## Version 1.34 ##
  * Fixed some DB issues regarding closed cursors
  * Fixed automatic refresh of labels on startup
  * No error-reporting when using app in debug-mode
  * Several minor bugfixes for FCs and ANRs

## Version 1.33 ##
  * [Issue #92](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#92): Introduced a new preference for the size of the title inside the ArticleHeaderView.
  * Fixed several bugs which were leading to FCs

## Version 1.32 ##
  * Reintroduced the "Progress\_Indeterminate" notification for API-Levels < 11
  * IllegalStateExceptions should not occur anymore
  * Fixed several bugs which were leading to FCs

## Version 1.31 ##
  * Fixed several bugs.

## Version 1.3 (1) ##
  * Completely re-wrote the communication between interface and backend
> > -> Interface did active polling, now it registers as listener and gets notified about changes
  * Completely changed the way the DB is accessed, drastically reduced the number of locks involved
  * Slight changes to the UI to work with the changed code
  * Lots of changes to reduce the amount of work for DB-Write-Accesses and necessary RAM for the ImageCache
  * Added ActionBarSherlock, included "mark all read" and "display all" in ActionBar, optionally if there is room also "offline" and "ImageCache"
  * Some minor fixes to portrait/landscape-mode and positioning of the swipe-area
  * Performance-optimizations

## Version 1.26 (1) ##
  * Fixed FC on startup. Sry about that. I made changes for Android 4.0 and didn't properly test with older versions.
  * Completed work on background tasks
  * Minor changes

## Version 1.25 ##
  * Reduced time to switch activities
  * Tried to prevent display of empty datasets
  * Fixed ImageCache for devices running Android 4.x
  * Finally resolved concurrency-issues (Android-Devs are to be blamed...)
  * Minor Bugfixes

## Version 1.24 ##
  * Performance-Optimizations for faster startup
  * Fix starred/published articles never beeing purged
  * Minor Bugfixes

## Version 1.23 ##
  * Downloaded attachments are opened when tapping the notification now
  * Player for downloaded files is chosen depending on file-extension (internal display of media-files hasn't changed)
  * Minor Bugfixes

## Version 1.22 ##
  * Added support for new API of TTRSS 1.5.8 (due January 2012)
  * Added Enhancement #65: ([Publish with a note](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=65))
  * Notes are also cached if working offline, later they are automatically synchronized with the server
  * Fixed not-working "mark as unread" menu entry
  * Ignore specific SSLExceptions which often occur on bad connections
  * Added check for http status code "401 Unauthorized"
  * Fetching only new articles once on startup (API gives information about this since v1.5.8)

## Version 1.21 ##
  * Fixed wrong handling of Usage-options
  * Trying to fix ActivityNotFoundExceptions which affected some users
  * Added preference for "Inject Article-Link"

## Version 1.2 ##
  * Added compatibility-code for Android 4.0 (API-Level 14)
  * Started working on a three-pane-layout for tablet-devices (not functional yet)
  * Fixed [Issue #80](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#80): ([Broken list in JSON request](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=80))
  * Fixed Enhancement #77: ([Support lazy servers (no cron job for fetching feeds)](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=77))
  * Fixed Enhancement #79: ([Inject a link to the web article at the end of the RSS article](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=79))
  * Minor bugfixes and refactoring

## Version 1.12 ##
  * Added dialogs to preferences which allow to change the Cache-Folder and the download-location for attached Media-Files

## Version 1.11 ##
  * Modified background-refresh of displayed content, empty lists should not happen anymore (or at least not so often)
  * Changed link-color for dark theme, links are now red and readable on dark background.
  * Added connection-timeouts (5 seconds for connection, 8 seconds for receiving any data) to avoid endless running jobs.

## Version 1.1 ##
  * Added display-preference for dark-background and white text
  * Added preference "Start ImageCache only on Wifi", which prevents the automatically scheduled ImageCache to run on a mobile connection
  * Fixed bug: Error in case of disabled-API

## Version 1.09 (2) ##
  * Vibrate when last article is reached, not only when trying to move further
  * Changed buttons and layout for article-browsing
  * Added left-hand-mode for landscape display
  * Fixed [Issue #75](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#75): ([Own keystore doesn't work any more](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=75))
  * Fixed Bug which prevented articles which had been marked while beeing offline to be synchronized with the server later
  * Removed ArticleCache. Same thing happens automatically when CategoryView is opened.
  * Fixed ImageCache for unreliable connections
  * Starred/Published articles are not purged anymore, article-limit in settings does not apply to these.

## Version 1.08 ##
  * Fixed connectivity-problems (no more endless waiting with spinning activity-wheel)
  * Added preference to disable swipe and instead use buttons for browsing articles. Still a bit buggy.

## Version 1.07 (4) ##
  * Improved speed of getCounters-API-call and database-access in method insertArticle()
  * Allow receiving of gzip-compressed data
  * Removed more unnecessary code due to dropped support of servers below 1.5.3
  * Wrote new Connector with new JSON-parser "GSON" from google (included in Android since API-lvl 11)
  * Fixed some problems with the counters for Fresh/Starred/Published articles
  * Faster refresh of the UI when marking anything
  * Fixed small bug which rendered the ImageCache unusable
  * Fixed Labels and two ANR-Situations
  * Fixed "Mark all read" for Labels
  * Fixed "Invert sort" for category-view
  * Bugfixes

## Version 1.06 ##
  * Removed support for server-versions lower then 1.5.3
  * Added progress-information and UI-refresh to ImageCache but only while articles are fetched
  * Added support for Labels

## Version 1.05 ##
  * Refreshing more often during update, show progress
  * Reduced the number of updates beeing done, every piece of data is updated only once in 2 minutes if necessary or if you do refresh manually (via menu).
  * Store articles marked as read also for whole feeds/categoires. You can mark a feed as read while working offline, all articles in it the moment you marked it read will be synchronized with the server when you get online next time.
  * Refactored ImageCache, removed unused abstractions
  * Fixed some bugs
  * Fixed a problem with articles beeing marked as read only when opening the feed
  * Will remove support for server-versions prior to 1.5.3 in next release

## Version 1.04 ##
  * Do VACUUM only once a month or manually from preferences
  * Fixed encoding of passwords with special characters
  * Tried to fix bug due to wrong server-version detected

## Version 1.03 (2) ##
  * Fixed [Issue #67](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#67): ([Date & time settings not honoured](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=67))
  * New preferences allow you to change date and time formatting
  * Fixed [Issue #68](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#68): ([Justified (Blocksatz) looks awkward in some articles](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=68))
  * New preference allows you to change display of articles (justified or flush left)
  * Fixed [Issue #70](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#70): ([passwords with spaces aren't escaped ](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=70))
  * Fixed reloading of preferences after changes, completely revamped the way preferences are handled and reloaded after beeing changed
  * Dialog-windows are dismissed when they are not needed anymore
  * HTTP-Auth credentials are reloaded after changes
  * Silently (and accidentally) changed versioning-scheme. From now on (or from 1.0 on) we use x.yy or x.yy (z) for small changes.
  * Minor bugs.

## Version 1.02 (2) ##
  * Added preference to automatically start ImageCache/ArticleCache on startup
  * No other updates will be started while Cache is running
  * Fixed some more Bugs

## Version 1.01 ##
  * Automatically fetch all new articles for all feeds on startup, can take a moment but you can just start browsing anyway
  * Improved marking articles as read, not sure if everything is fixed but counts should be allright most of the time now
  * Fixed swipe in Headline-List (go to next/previous feed by swipe or volume-button)
  * Minor improvements

## Version 1.0 ##
  * Added versionCode to StackTrace-Mails
  * Added safety-queries for failed DB operations
  * Fixed Feeds beeing displayed as read after opening
  * Fixed NPE when using old (pre 2.0) Service-API
  * Think we are in a pretty stable state now so I switched to Version 1.0
  * Minor Bugs fixed
  * More minor Bugs fixed

## Version 0.9.7 ##
  * Fixed some bugs introduced with 0.9.6
  * Added compatibility-fixes for Android 3.0
  * Changed some DB-Operations, added SQLite-Vacuum to free space
  * Added preferences to delete Database on startup
  * Hopefully no more stale articles, unread-state should be completely synchronized now
  * Fixed Error-Messages not beeing displayed with Server v1.5.3
  * Fixed POST-Data not beeing interpreted correctly by the server, now sending JSON-encoded data
  * Two more bugs with starred/published articles fixed
  * Fixed OutOfMemoryException when opening virtual categories
  * Fixed ImageCache with non-standard HTML, fixed SQLiteException with broken SQLite on some Devices

## Version 0.9.6 ##
  * Show read articles when browsing "only unread articles" and entering an empty feed (eg. by returning from the last unread article)
  * Minor refactoring and changes due to release of Tiny Tiny RSS 1.5.3 (POST-Requests, Attachments in getHeadlines)
  * Added check for corrupted DB, it is moved to a backup-file and a new DB will be created
  * Show last-opened article/feed when returning from child-view, even if read content is filtered
  * Improved speed of update when using long-press on category -> "Show all articles"
  * Cleanup of old starred/published articles when refreshing
  * Fixed DB-Exceptions

## Version 0.9.5 ##
  * Fixed error-message for API\_DISABLED not beeing displayed
  * Fixed swipe-here-notification not beeing displayed
  * Changed zoom-behaviour (default: Images are scaled, once zoomed: Images are displayed in right aspect-ratio)
  * Fixed broken next/previous article/feed jump when returning from article-view
  * Images are not fetched from online-sources anymore if browsing in Offline-mode
  * Some performance-issues fixed and some background-exceptions too

## Version 0.9.4 ##
  * Reduced permissions as requested by [Issue #58](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#58) ([Is "Phone Calls" Permission Necessary?](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=57))

## Version 0.9.3 ##
  * Added French translation, thanks Régis
  * Long-press a category to show all articles in this category in one list
  * "Show all articles" now also allows browsing with volume-keys or swiping

## Version 0.9.2 ##
  * ImageCache now started as a service so it doesn't get killed anymore by the system
  * ImageCache accessible in menu of CategoryList, FeedList and FeedHeadlineList
  * Added notification for running downloads, using a grey icon to indicate the difference
  * Fixed DB-Problems which prevented efficient caching

## Version 0.9.1 ##
  * New: Added possibility to send error-reports after a crash
  * New: Removed donators-limitations, everyone can use the ImageCache now
  * New: Added Exit-Button to error-dialog to allow completely exiting the app from the dialog
  * Refurbished the reset of preferences (Preferences -> Menu -> Reset)
  * blog.fefe.de now working (fixed relative links but I am unsure whether I have to use full path to article or just the domain-part. Ideas anyone?)
  * Cleanup of preferences
  * Minor bugfixes

## Version 0.9 ##
  * New: Added check for supported server-version on startup
  * New: Added check for saved crash-report and option to send report by mail
  * Minor changes in network-access, Requests are faster now

## Version 0.8.9 (1) ##
  * New: Added custom title-bar in Article-Display (can be disabled in preferences)
  * New: Added setting to change the display-order of all lists
  * New: Added long-press option to Category-View, showing list of all articles inside (Please note that updating all feeds in a category can take a while but it happens in background anyway)
  * Addressed connectivity problems (probably no more annoying "could not connect"-messages)
  * Small bugfixes

## Version 0.8.8 ##
  * Fixed [Issue #49](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#49): ([ttrss-reader-fork closes on launch and with wrong password](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=49))
  * Fixed [Issue #50](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#50): ([New Article unread are shown](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=50))
  * Fixed [Issue #53](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#53): ([crashes with "mark all as read"](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=53))
  * Small fix which prevented new users from starting the app
  * Small fix for a DB-Problem

## Version 0.8.7 ##
  * Performance improvements for ImageCache
  * Minor fixes for retrieving of articles
  * Fixed accidental jump to first feed while browsing

## Version 0.8.6 ##
  * ImageCache download now parallelized into 4 threads (ArticleCache: 8 threads)
  * Some performance tweaks
  * Fixed [Issue #45](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#45): ([SQL Error because of suhosin-blocked article\_id](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=45))
  * Added preference to split long GET-Requests due to problems with server-side security scanners like Suhosin

## Version 0.8.5 ##
  * Synchronizing marked articles now also works for complete feeds and categories
  * When marking feeds/categories as read while beeing offline only local articles can be included, articles that are new on the server but not synchronized to the client cannot.
  * Fixed FC on mark article read while beeing offline

## Version 0.8.4 ##
  * Added options for Image- and ArticleCache: cache for unread articles only
  * Caching now works independent from online/offline state
  * Added Offline-Synchronisation (article-state, star, published is synchronized later when browsing offline)
  * Navigate by keyboard: n for next, b for previous article/feed

## Version 0.8.3 ##
  * Fixed Articles not getting purged according to "Store articles limit"
  * Probably fixed OutOfMemory problem while fetching articles

## Version 0.8.2 ##
  * Added ArticleCache (load all new articles at once) for Donators
  * Refresh unread-state while browsing unread articles
  * Scale images in articleActivity so they match the display-width in original zoom (for other zoom-factors the original size is restored)
  * Set CSS: text-align:"justify" in articleActivity
  * More useful text when loading content
  * Fixed ANR-situation
  * Minor bugfixes/refactoring

## Version 0.8.1 ##
  * Addressed severe memory-issues
  * Moved menu-definitions from code to xml files
  * Added information to preferences-screen
  * Refactored menus
  * Fixed articles not getting marked as read
  * Fixed offline-setting

## Version 0.8 ##
  * Added support for donations, status is checked once after providing the mailadress used to poste the donation
  * For donators: Added pre-downloading of images for offline viewing (see project page for more information)
  * Size of Swipe-Area now depends on density and screen-size and is adjusted accordingly
  * Fixed [Issue #36](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#36): ([functional - suggestion](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=36)) (show read articles for Virtual Categories)
  * Fixed [Issue #37](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#37): ([display source in view "All Articles"](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=37))
  * Fixed [Issue #38](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#38): ([mark all read in "All Articles"](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=38))
  * Fixed [Issue #40](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#40): (Better error-handling for disabled API and Not logged in

## Version 0.7.8 ##
  * Added HTTP-Authentication. Resolves [Issue #32](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#32) ([If an http 401 (denied) is received, try http authentication](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=32))
  * Fixed [Issue 34](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=34): ([Article refreshing problem](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=34))

## Version 0.7.7 ##
  * Now using server-side method for marking feeds/categories as read
  * Fixed sorting of categories
  * Tried to add compatibility-mode for old (pre 1.5) servers

## Version 0.7.6 ##
  * New Application-Icon and Overlay-icons for star/publish
  * Added support for the star- and publish-feature. Resolves [Issue #23](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#23) ([Toggle star/unstar of an article](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=10))
  * Fixed display of fresh/starred/published articles in the virtual feeds
  * Fixed handling of URLs in Article-View
  * Fixed problems with marking articles as read/unread

## Version 0.7.5 ##
  * Fixed sorting of categories
  * Enhanced mediafile-download and notification

## Version 0.7.4 ##
  * Minor bugs fixed
  * Better fling-support for headline-list and articles
  * No recommendation when typing URL
  * Fixed Bug with unread-counts
  * Implemented new API-methods from tt-rss.org to avoid the manual hacking
  * Added german localisation

## Version 0.7.3 ##
  * Problems with API from 1.4.3.1 are (hopefully) resolved. Please mail me if i am wrong :)

## Version 0.7.2 ##
  * Fixed wrong calculation of unread-counts
  * Added welcome-dialog for the first-run

## Version 0.7.1 ##
  * Added donate-button to about-dialog
  * Fixed two unhandled Exceptions
  * Fixed no virtual Categories beeing displayed on first start

## Version 0.7 ##
  * Improved updating of locally stored information
  * Faster startup
  * Better restarting of activities
  * Fixed sorting-issues of feeds
  * Fixed double-login
  * Fixed progress-indicator

## Version 0.6.8 ##
  * Fixed: https://code.google.com/p/ttrss-reader-fork/issues/detail?id=30 (Only one article shown when using old API)
  * Added notification for finished attachment-downloads.

## Version 0.6.6 ##
  * Fixed no articles beeing fetched if API doesn't support getArticles
  * Fixed problem with VirtualCategories not beeing displayed

## Version 0.6.5 ##
  * Fixed problem with attached images
  * Changed update-behaviour

## Version 0.6.4 ##
  * Added Changelog with button for the Donations-wikipage
  * Fixed some concurrent-access-issues with database
  * No more data on sdcard
  * Performance boost by removing in-memory-storage of articles
  * Reduced memory-footprint by reading from DB when needed, not in advance.
  * Minor fixes and major refactoring
  * Fixed fling-detection

## Version 0.6.3 ##
  * Refactored the settings-code
  * Refactored the JsonConnector and its interface
  * Rewrote DataController (now: Data) from scratch
  * Don't request Headlines and Content of Articles seperated, just fetching the full article now
  * Fixed: Fling in virtual category causing NPE
  * Fixed: getArticles is now working, even with with virtual categories
  * Using Sets instead of Lists in backend to avoid having to deal with duplicates. Refactored all occurrences.
  * Avoiding synchronisation-issues and parallel access in DBHelper
  * Better Fling-recognition

## Version 0.6.2 (Bugfix-release) ##
  * Fixed NPE in CategoryActivity
  * Added Update-Check in main-menu
  * Resolved Licensing issues

## Version 0.6.1 ##
  * Passwords are now allowed to contain special characters
  * Attached Audio/Video files can now be downloaded or directly played

## Version 0.6 ##
  * No more String-identifiers, everything migrated to int
  * Most DB-Querys are no raw-querys anymore
  * isOnline works but is not always handled correctly
  * We can now display attached Videos in the built-in mediaplayer
  * Minor bugs fixed

## Version 0.5.12 (Bugfix-release) ##
  * Resolved [Issue #23](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#23) ([Another FC while using tt-rss-reader](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=23))
  * Resolved [Issue #24](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#24) ([Another FC while using tt-rss-reader](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=24))
  * Resolved Attachments not beeing fetched properly
  * Resolved FC with empty articles
  * Stability improvements.

## Version 0.5.11 (Bugfix-release) ##
  * Fixed [Issue #16](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#16) ([error:NOT\_LOGGED\_IN if the app idle some time](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=16))
  * Fixed DB beeing closed when onDestroy called but not re-opened when app is beeing used again (leading to FC)

## Version 0.5.10 (Bugfix-release) ##
  * Fixed [Issue #17](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#17) ([FC while closing app (back button)](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=17))
  * Fixed [Issue #20](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#20) ([long article titles are not readable](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=20))
  * Fixed [Issue #21](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#21) ([FC while open browser, because article content is empty](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=21))
  * Fixed [Issue #22](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=#22) ([FC after starting the app](https://code.google.com/p/ttrss-reader-fork/issues/detail?id=22))

## Version 0.5.9 ##
  * Allow own SSL-certificates with own Keystore on SDCard
  * Created new OnSharedPreferenceChangeListener

## Version 0.5.8 ##
  * Bugfix-Release: Fixed Issues #14 and #15

## Version 0.5.7 ##
  * SSL-Connection possible, toggle "Accept all SSL certificates" in preferences
  * Added functionality to load all new articles since last update on startup, "Update articles with content on startup" in preferences
  * Shouldn't crash anymore when using non-modified Tiny Tiny RSS API
  * No more crashing when started first time, use "Refresh" from menu after changing your server-settings
  * Fixed some NPEs

## Version 0.5.6 ##
  * Fixed isOnline always returning false on device (does work in emulator though)
  * Fixed crash on startup if no settings were specified yet
  * Refactored Main-Activity and put StorageReceiver in seperate class

## Version 0.5.5 ##
  * Update unread-counts on startup
  * Articles on SD-Card, small data like headlines stay in internal storage
  * Article-content stays only in memory if sdcard not available
  * DB-update if version changes, at the moment the old DB is just deleted.
  * DB\_VERSION in Preferences-Storage, not settable by user
  * Better handling for synchronized-Methods in DataController, should lead to less time spent waiting for mutex.
  * IllegalStateException because Adapter was modified from background-thread instead of beeing changed from the activity.

## Version 0.5.4 ##
  * Disabled concurrent modifications in Refresher.java and Updater.java because of Exceptions (see comments in source)
  * forceRefresh not disabled automatically anymore, so we can forceRefresh several Adapters in one step (neccessary for Main-Activity with Categories and FeedHeadlines, see Comment in DataController.java)
  * Fixed articles not having set their URL and comments-URL

## Version 0.5.3 ##
  * Fixed several Exceptions
  * Signed with real key, before it was just for debugging
  * Fixed history of Activities. You can switch application and always return to the position you were before.
  * Added local caching in DB for Articles, Feeds and Categories.
  * HTML content is saved base64-encoded in DB to avoid syntax-problems while inserting
  * Removed Tabs, they were unneccessary since we have uncategorized feeds as a virtual category now
  * Added preference for "automatically open url for empty article".
  * Added preference for "automatically load data of categories below the current level".
  * Added preference for "use volume-keys to navigate to previous/next articles".
  * Added preference for "vibrate when first/last articles is reached".

## Version 0.4.0 ##
  * Mark everything as read button
  * Share Article-URL via Twitter, Mail...
  * Zoom-Button for Articles

## Version 0.3.0 ##
  * Next/previous-Button in Article-View Options-menu
  * Use Swipe for Previous/Next Article
  * Preference for "Use Swipe", default on
  * Swipe only in bottom area of the Article-View, display hint if swiping elsewhere
  * Added support for virtual-category "uncategorized Feeds" (categoryId=0)
  * Added Tab on Main-Screen for uncategorized Feeds
  * Added Option "DisplayOnlyUnread" to hide read articles and feeds without unread articles
  * Added Option "DisplayOnlyUnread" to Options-Menü
  * Added Option "Toggle Display Unread" to Main-Menu, FeedList-Menu and FeedHeadlineList-Menu
  * Show attached images
  * Automatically open webpage for empty Feed-Entrys
  * Automatically leave FeedHeadlineView if there are no items (see FeedHeadlineListActivity.java:207)