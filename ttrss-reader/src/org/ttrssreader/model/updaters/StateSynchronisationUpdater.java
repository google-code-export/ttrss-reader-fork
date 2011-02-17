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

package org.ttrssreader.model.updaters;

import org.ttrssreader.controllers.Data;
import org.ttrssreader.utils.Utils;
import android.util.Log;

public class StateSynchronisationUpdater implements IUpdatable {
    
    @Override
    public void update() {
        Log.i(Utils.TAG, "Synchronizing status of articles...");
        Data.getInstance().synchronizeStatus();
    }
    
}