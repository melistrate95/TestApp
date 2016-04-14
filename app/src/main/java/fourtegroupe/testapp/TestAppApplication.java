package fourtegroupe.testapp;

import android.app.Application;

import fourtegroupe.testapp.util.ImageLoader;

public class TestAppApplication extends Application {

    private static ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        imageLoader = new ImageLoader(this);
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }
}
