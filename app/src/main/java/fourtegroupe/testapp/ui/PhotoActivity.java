package fourtegroupe.testapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import fourtegroupe.testapp.R;
import fourtegroupe.testapp.TestAppApplication;

public class PhotoActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initToolbar();

        imageView = (ImageView) findViewById(R.id.full_image);

        int num = getIntent().getIntExtra(MainActivity.NUM_IMAGE, 1);
        toolbar.setTitle("Number " + num);

        switch (num) {
            case 1:
                TestAppApplication.getImageLoader().displayImage(MainActivity.IMAGE_URL_1, imageView);
                break;
            case 2:
                TestAppApplication.getImageLoader().displayImage(MainActivity.IMAGE_URL_2, imageView);
                break;
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
