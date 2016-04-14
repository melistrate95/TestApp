package fourtegroupe.testapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {

    public static Bitmap getDownScaleBitmap(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }
}
