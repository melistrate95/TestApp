package fourtegroupe.testapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    MemoryCache memoryCache;
    FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;

    public ImageLoader(Context context) {
        fileCache = new FileCache(context);
        memoryCache = new MemoryCache();
        executorService = Executors.newFixedThreadPool(5);
    }

    public void displayImage(String url, ImageView imageView) {
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            queuePhoto(url, imageView);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        Photo photo = new Photo(url, imageView);
        executorService.submit(new PhotosLoader(photo));
    }

    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);

        Bitmap bitmap = decodeFile(f);
        if (bitmap != null) {
            return bitmap;
        }

        try {
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.copyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
            }
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            return BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

    private class Photo {
        public String url;
        public ImageView imageView;

        public Photo(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        Photo photo;

        PhotosLoader(Photo photo) {
            this.photo = photo;
        }

        @Override
        public void run() {
            if (imageViewReused(photo)) {
                return;
            }
            Bitmap bitmap = getBitmap(photo.url);
            memoryCache.put(photo.url, bitmap);
            if (imageViewReused(photo)) {
                return;
            }
            BitmapDisplayer bitmapDisplayer = new BitmapDisplayer(bitmap, photo);
            Activity a = (Activity) photo.imageView.getContext();
            a.runOnUiThread(bitmapDisplayer);
        }
    }

    boolean imageViewReused(Photo photo) {
        String tag = imageViews.get(photo.imageView);
        return tag == null || !tag.equals(photo.url);
    }

    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        Photo photo;

        public BitmapDisplayer(Bitmap bitmap, Photo photo) {
            this.bitmap = bitmap;
            this.photo = photo;
        }

        public void run() {
            if (imageViewReused(photo)) {
                return;
            }
            if (bitmap != null) {
                photo.imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
