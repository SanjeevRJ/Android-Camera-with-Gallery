package sanjeevj.median;

/**Sanjeev Janarthanan
 *Final Project
 *An Android Application which takes pictures and can store them in a gallery.
 *GalleryActivity.java
 *Version 3
 *May 27, 2015
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Created by Sanjeev.Janarthanan on 5/13/2015.
 */
public class GalleryActivity extends Activity {
    /*
     * This instance variable stores the number of ImageButtons being used.
     */
    private int numActiveImgButtons;
    /*
     * This instance variable stores ALL ImageButtons in an ArrayList.
     */
    private ArrayList<ImageButton> imageButtons;
    /*
     * This is an apporpiate size, in pixels, for images being displayed in this activity.
     */
    private static final int IMAGE_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //The code below seems very redundant, but I see no other way to do it. Since I'm initializing
        //objects with different names, I cannot use a for loop.
        imageButtons = new ArrayList<>();
        final ImageButton selectImage1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButtons.add(selectImage1);
        final ImageButton selectImage2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButtons.add(selectImage2);
        final ImageButton selectImage3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButtons.add(selectImage3);
        final ImageButton selectImage4 = (ImageButton) findViewById(R.id.imageButton4);
        imageButtons.add(selectImage4);
        final ImageButton selectImage5 = (ImageButton) findViewById(R.id.imageButton5);
        imageButtons.add(selectImage5);

        final ImageView displayedImage = (ImageView) findViewById(R.id.displayedImage);
        final Button backToMenuButton = (Button) findViewById(R.id.backToMenu);

        Intent i = getIntent();


        ArrayList<File> allImgFiles = new ArrayList<>();
        //This following is hardcoded, but I programatically found the string representation of this directory
        File directory = new File("/storage/emulated/0/Pictures/MyCameraApp/");
        //Here, I'm populating an ArrayList with all the files contained in the directory where I
        //store images taken in this app.
        File[] files = directory.listFiles();
        for (int j = 0; j < files.length; j++) {
            allImgFiles.add(files[j]);
        }

        //Here, I initialize the value of active ImageButtons. this member variable will only be less than the
        //total number of ImageButtons if there are less pictures than ImageButtons.
        numActiveImgButtons = allImgFiles.size();
        if (numActiveImgButtons > imageButtons.size()) {
            numActiveImgButtons = imageButtons.size();
        }

        //This handles the case where the user has taken no pictures.
        if (allImgFiles.size() <= 0) {
            Toast.makeText(getApplicationContext(), "Take some photos before accessing the gallery", Toast.LENGTH_LONG).show();
            finish();
        }

        //The following populates an ArrayList with only the images that will be used for ImageButtons.
        ArrayList<File> imgFiles = new ArrayList<>(numActiveImgButtons);
        //The following if-else statements account for the case where there are less pictures than
        //ImageButtons.
        if (allImgFiles.size() >= numActiveImgButtons) {
            for (int k = numActiveImgButtons; k > 0; k--) {
               imgFiles.add(allImgFiles.get(allImgFiles.size()-k));
            }
        } else {
            for (int l = allImgFiles.size(); l > 0; l--) {
                imgFiles.add(allImgFiles.get(allImgFiles.size()-l));
            }
        }

        //The following populates an ArrayList with Bitmap representations of the pictures stored in
        //the files to be used for the ImageButtons.
        final ArrayList<Bitmap> imgBitmaps = new ArrayList();
        for (int j = 0; j < imgFiles.size(); j++) {
            imgBitmaps.add(decodeSampledBitmapFromFile(imgFiles.get(j), IMAGE_SIZE, IMAGE_SIZE));
        }

        //The following sets the Bitmap representations to the appropiate ImageButtons.
        for (int k = 0; k < numActiveImgButtons; k++) {
            imageButtons.get(k).setImageBitmap(imgBitmaps.get(k));
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.getId() returns the Id (an int) of the widget that has been interacted with.
                //So, I cycle through all of the active ImageButtons and see whether their Id matches.
                //If the Id matches, I set the appropiate Bitmap to the displayedImage view.
                for (int i = 0; i < numActiveImgButtons; i++) {
                    if (v.getId() == imageButtons.get(i).getId()) {
                        displayedImage.setImageBitmap(imgBitmaps.get(i));
                        break;
                    }
                }
            }
        };
        //This sets the above listener to all of the active ImageButtons.
        for (int l = 0; l < numActiveImgButtons; l++) {
            imageButtons.get(l).setOnClickListener(listener);
        }

        View.OnClickListener listener6 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        backToMenuButton.setOnClickListener(listener6);
    }

    /*
     * This method resizes a bitmap to an appropiate size (for the sake of memory).
     * @param   options A BitmapFactory.Options object. A BitmapFactory contains helpful methods
     *                  for working with Bitmaps.
     * @param   reqWidth An int defining an appropiate size.
     * @param   reqHeight An int defining an appropiate size.
     * @return  inSampleSize an int to be used when creating an appropiately sized Bitmap.
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //Reduces the height and width of the image until they are at least half of the
        //requested height and width.
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /*
     * This method decodes a file, and returns it a Bitmap representation
     * @param   file the file to be decoded
     * @param   reqWidth an appropiate size
     * @param   reqHeight an appropiate size
     * @return  Bitmap a Bitmap representation of the file.
     */
    public static Bitmap decodeSampledBitmapFromFile(File file,
                                                         int reqWidth, int reqHeight) {

        //Decodes File with inJustDecodeBounds=true to check dimensions.
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        //Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //Decode bitmap with inSampleSize set, to save memory space.
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
