package sanjeevj.median;

/**Sanjeev Janarthanan
 *Final Project
 *An Android Application which takes pictures and can store them in a gallery.
 *SettingsActivity.java
 *Version 3
 *May 27, 2015
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sanjeev.Janarthanan on 5/27/2015.
 */
public class SettingsActivity extends ActionBarActivity {

    /*
     * This method defines the activity (how widgets will behave) of the menu screen of my application.
     * @param   savedInstanceState the last available state of this activity. This point of this param
     *                             is to save data from when an activity closes and then reopens.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText enterNumPhotos = (EditText) findViewById(R.id.numPhotos);
        final EditText enterSecDelay = (EditText) findViewById(R.id.secDelay);
        final Button backToMenuButton = (Button) findViewById(R.id.menuButton);

        Intent i = getIntent();

        Toast.makeText(getApplicationContext(), "Settings layout is only supported in portrait", Toast.LENGTH_LONG).show();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        backToMenuButton.setOnClickListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
