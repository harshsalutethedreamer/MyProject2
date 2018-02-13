package org.shpstartup.android.yocount;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by harshgupta on 28/10/16.
 */
public class ImageContract {
    public interface ImageColumns{
        String IMAGECATEGORY_ID="_id";
        String IMAGECATEGORY_NAME="imagecategory_name";
        String IMAGENAME="name";
        String IMAGECATEGORY_CREATED_DATE="imagecategory_date";
    }

    public static final String CONTENT_AUTHORITY="org.shpstartup.android.yocount.provider";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_IMAGE="imagecount";
    public static final Uri URI_TABLE=Uri.parse(BASE_CONTENT_URI.toString()+"/"+PATH_IMAGE);

    public static final String[] TOP_LEVEL_PATHS = {
            PATH_IMAGE
    };

    public static class ImageCategory implements ImageColumns,BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_IMAGE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".imagecount";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".imagecount";

        public static Uri buildFriendUri(String friendId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(friendId).build();
        }

        public static String getFriendId(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }
}
