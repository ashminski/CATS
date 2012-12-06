package edu.vt.ece4564.cats_app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ShowPostsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_posts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_posts, menu);
        return true;
    }
}
