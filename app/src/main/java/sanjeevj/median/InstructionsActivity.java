package sanjeevj.median;

/**Sanjeev Janarthanan
 *Final Project
 *An Android Application which takes pictures and can store them in a gallery.
 *InstructionsActivity.java
 *Version 3
 *May 27, 2015
 */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sanjeev.Janarthanan on 5/12/2015.
 */
public class InstructionsActivity extends ActionBarActivity {

    /*
     * This method defines the activity (how wdigets will behave) of the menu screen of my application.
     * @param   savedInstanceState the last available state of this activity. This point of this param
     *                             is to save data from when an activity closes and then reopens.
     */
    @Override //This tells the compiler that the following method overrides a method of its superclass
    protected void onCreate(Bundle savedInstanceState) {
        //The following codes creates an instance of this activity using a savedInstanceState (if one is
        //available). The layout is set by the specified layout file.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        //Here, I'm creating objects that are associated with widgets in my XML file. This so that I can
        //interact with them in my activity.
        final Button backToMenuButton = (Button) findViewById(R.id.backToMenu);
        final EditText instructionsText = (EditText) findViewById(R.id.theInstructions);

        //In my MainActivity, an intent was sent to this activity. The following line of code recieves that
        //intent.
        Intent i = getIntent();

        //This causes the editText widget to be unchangeable.
        instructionsText.setFocusable(false);

        //This shows a short message to the user when they open this activity.
        Toast.makeText(getApplicationContext(), "Instructions layout is only supported in portrait", Toast.LENGTH_LONG).show();

        //When an OnClickListener is opened on a View (a widget such as a button), it "listens" for interaction
        //from the user, then exectues whatever code is in its onClick method.
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This closes the activity and returns the user back to the parent activity, MainActivity.
                finish();
            }
        };
        backToMenuButton.setOnClickListener(listener);
    }

    /*
     * The following methods are added by Android Studio to handle any interactions the user may have
     * with the action bar (the bar at the top of every Android phone).
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}

