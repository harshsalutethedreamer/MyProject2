package org.shpstartup.android.yocount;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by harshgupta on 10/09/16.
 */
public class NumeroContract {
    interface NumeroColumns{
        String NUMERO_ID ="_id";
        String NUMERO_CATEGORY="numero_category";
        String NUMERO_DESCRIPTION="numero_description";
        String NUMERO_COUNT="numero_count";
        String NUMERO_DATE="numero_date";
        String NUMERO_SPECIAL="numero_special";
        String NUMERO_CREATEDDATE="numero_createddate";
        String NUMERO_UPDATEDDATE="numero_updateddate";
    }

    public static final String CONTENT_AUTHORITY="org.shpstartup.android.yocount.provider";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_FRIENDS="yocount";
    public static final Uri URI_TABLE = Uri.parse(BASE_CONTENT_URI.toString()+"/"+PATH_FRIENDS);

    public static final String[] TOP_LEVE_PATHS = {
            PATH_FRIENDS
    };

            public static class Numeros implements NumeroColumns,BaseColumns {
                public static final Uri CONTENT_URI =
                        BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_FRIENDS).build();

                public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".yocount";
                public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".yocount";

                public static Uri buildFriendUri(String friendId) {
                    return CONTENT_URI.buildUpon().appendEncodedPath(friendId).build();
                }

                public static String getFriendId(Uri uri){
                    return uri.getPathSegments().get(1);
                }

            }
}
