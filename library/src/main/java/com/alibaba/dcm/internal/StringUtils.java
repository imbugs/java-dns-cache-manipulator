/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.alibaba.dcm.internal;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author tinghe
 * @version $Id: StringUtil.java, v 0.1 2018年01月15日 下午6:29 tinghe Exp $
 */
public class StringUtils {
    public static final String EMPTY = "";

    public static boolean isBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }

    public static String trim(String str) {
        return trim(str, null, 0);
    }

    private static String trim(String str, String stripChars, int mode) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        int start = 0;
        int end = length;

        if (mode <= 0) {
            if (stripChars == null) {
                while ((start < end) && (Character.isWhitespace(str.charAt(start)))) {
                    start++;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end) && (stripChars.indexOf(str.charAt(start)) != -1)) {
                    start++;
                }
            }
        }

        if (mode >= 0) {
            if (stripChars == null) {
                while ((start < end) && (Character.isWhitespace(str.charAt(end - 1)))) {
                    end--;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                    end--;
                }
            }
        }

        if ((start > 0) || (end < length)) {
            return str.substring(start, end);
        }

        return str;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String join(Collection collection, char separator) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), separator);
    }

    public static String join(Iterator iterator, char separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return toString(first);
        }

        // two or more elements
        StringBuffer buf = new StringBuffer(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            buf.append(separator);
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }

        return buf.toString();
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}