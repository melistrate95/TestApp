package fourtegroupe.testapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import fourtegroupe.testapp.R;
import fourtegroupe.testapp.ui.views.CheckableImageView;
import fourtegroupe.testapp.util.Utils;

public class MainActivity extends AppCompatActivity {

    public static final String NUM_IMAGE = "NUM_IMAGE";

    private int selectedImage = 0;

    protected Toolbar toolbar;
    protected CheckableImageView imageView1;
    protected CheckableImageView imageView2;
    protected View selectView1;
    protected View selectView2;

    protected FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        imageView1 = (CheckableImageView) findViewById(R.id.image1);
        imageView2 = (CheckableImageView) findViewById(R.id.image2);

        selectView1 = findViewById(R.id.select1);
        selectView2 = findViewById(R.id.select2);

        imageView1.setImageBitmap(Utils.getDownScaleBitmap(this, R.drawable.image1));
        imageView2.setImageBitmap(Utils.getDownScaleBitmap(this, R.drawable.image2));

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.setChecked(true);
                selectView1.setVisibility(View.VISIBLE);
                if (imageView2.isChecked()) {
                    imageView2.setChecked(false);
                    selectView2.setVisibility(View.INVISIBLE);
                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView2.setChecked(true);
                selectView2.setVisibility(View.VISIBLE);
                if (imageView1.isChecked()) {
                    imageView1.setChecked(false);
                    selectView1.setVisibility(View.INVISIBLE);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView1.isChecked()) {
                    selectedImage = 1;
                }
                if (imageView2.isChecked()) {
                    selectedImage = 2;
                }
                if (selectedImage == 0) {
                    Toast.makeText(MainActivity.this, R.string.toast_choose_image, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                    intent.putExtra(NUM_IMAGE, selectedImage);
                    startActivity(intent);
                }
            }
        });
    }
}
