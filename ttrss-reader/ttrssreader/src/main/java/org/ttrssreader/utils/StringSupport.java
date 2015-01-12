/*
 * Copyright (c) 2015, Nils Braden
 *
 * This file is part of ttrss-reader-fork. This program is free software; you
 * can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program; If
 * not, see http://www.gnu.org/licenses/.
 */

package org.ttrssreader.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// contains code from the Apache Software foundation
public class StringSupport {
    
    /**
     * Splits the ids into Sets of Strings with maxCount ids each.
     * 
     * @param ids
     *            the set of ids to be split
     * @param maxCount
     *            the maximum length of each list
     * @return a set of Strings with comma-separated ids
     */
    public static <T> Set<String> convertListToString(Collection<T> values, int maxCount) {
        Set<String> ret = new HashSet<String>();
        if (values == null || values.isEmpty())
            return ret;
        
        StringBuilder sb = new StringBuilder();
        int count = 0;
        
        Iterator<T> it = values.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o == null)
                continue;
            
            sb.append(o);
            
            if (count == maxCount) {
                ret.add(sb.substring(0, sb.length() - 1));
                sb = new StringBuilder();
                count = 0;
            } else {
                sb.append(",");
                count++;
            }
        }
        
        if (sb.length() > 0)
            ret.add(sb.substring(0, sb.length() - 1));
        
        return ret;
    }
    
    public static String[] setToArray(Set<String> set) {
        String[] ret = new String[set.size()];
        int i = 0;
        for (String s : set) {
            ret[i++] = s;
        }
        return ret;
    }
    
}
