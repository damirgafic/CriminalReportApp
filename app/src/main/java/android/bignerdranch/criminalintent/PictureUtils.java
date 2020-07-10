package android.bignerdranch.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path,
        int destWidth, int destHeight) {
        // Read in dimensions of the picture on the disk:
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // Do math to figure out the scaling
        int inSampleSize = 1;
        if(srcHeight > destHeight || srcWidth > destWidth) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;

            inSampleSize = Math.round(
                    heightScale > widthScale ? heightScale : widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Read in and create the final bitmap
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        // holds an ordered pair
        Point size = new Point();
        // ask the activity what it's default window size is.
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        // return the scaled Bitmap using the size of the screen.
        return getScaledBitmap(path, size.x, size.y);
    }
}
