package org.telegram.messenger;

import android.content.SharedPreferences;

public final class KitGramSettings {
    private static final String PREFS = "kitgram_settings";
    public static final String KEEP_DELETED_MESSAGES = "keep_deleted_messages";
    public static final String KEEP_EDITED_MESSAGES = "keep_edited_messages";
    public static final String HIDE_TYPING = "hide_typing";
    public static final String HIDE_READ_TIME = "hide_read_time";

    private KitGramSettings() {
    }

    public static SharedPreferences getPreferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences(PREFS, 0);
    }

    public static boolean isEnabled(String key) {
        return getPreferences().getBoolean(key, false);
    }
}