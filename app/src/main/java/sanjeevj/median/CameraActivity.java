package sanjeevj.median;

/**Sanjeev Janarthanan
 *Final Project
 *An Android Application which takes pictures and can store them in a gallery.
 *CameraActivity.java
 *Version 3
 *May 27, 2015
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Environment.getDataDirectory;

/**
 * Created by Sanjeev.Janarthanan on 5/15/2015.
 */
public class CameraActivity extends Activity {

    /*
     * This is a robust way of storing the name of the activity in a class constant. This constant
     * is used when error messages are displayed to the user.
     */
    public static final String TAG = CameraActivity.class.getSimpleName();
    /*
     * A member variable which is a Camera object.
     */
    private Camera mCamera;
    /*
     * A member variable which is a CameraPreview object.
     */
    private CameraPreview mPreview;
    /*
     * This class constant specifies that the captured media is an image.
     */
    public static final int MEDIA_TYPE_IMAGE = 1;
    /*
     * This class constant specifies hhat the captured media is an image.
     */
    public static final int MEDIA_TYPE_VIDEO = 2;

    /*
     * This method defines the activity (how widgets will behave) of the menu screen of my application.
     * @param   savedInstanceState the last available state of this activity. This point of this param
     *                             is to save data from when an activity closes and then reopens.
     */
    @Override //This tells the compiler that the following method overrides a method of its superclass
    public void onCreate(Bundle savedInstanceState) {
        //The following codes creates an instance of this activity using a savedInstanceState (if one is
        //available). The layout is set by the specified layout file.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //In my MainActivity, an intent was sent to this activity. The following line of code recieves that
        //intent.
        Intent i = getIntent();

        //Checks to see whether this phone has a functioning camera.
        if(!checkCameraHardware(getApplicationContext())) {
            //This sends a message to the Log (visible in Android Studio) while the app is being debugged.
            Log.d(TAG, "Camera Hardware Faulty");
        }

        //A robust way of creating an instance of Camera.
        mCamera = getCameraInstance();
        if (mCamera == null) {
            Log.d(TAG, "Error initializing camera");
        }

        //The following code creates our Preview view and sets it as the main view of our layout.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        //This shows a short message to the user when they open this activity.
        Toast.makeText(getApplicationContext(), "You MUST take photos in landscape", Toast.LENGTH_LONG).show();

        //This object is sort of like an OnClickListener. It waits for the takePicture() method to be called. After
        //this happens, this method process the image from that call.
        final Camera.PictureCallback mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //After the takePicture() method is called, all previews are closed. Therefore we must restart the preview
                //once a picture is taken.
                mCamera.startPreview();
                File pictureFile = null;
                //This try-catch block attempts to create a file wherein the captured picture will be stored.
                try {
                    pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (pictureFile == null){
                    Log.d(TAG, "Error creating media file, check storage permissions: ");
                    return;
                }
                //This try-catch block writes the picture to the created file, if possible.
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        };


        final Button captureButton = (Button) findViewById(R.id.button_capture);
        //final Button backToMenuButton = (Button) findViewById(R.id.menuButton);

        // Add a listener to the Capture button
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );

       //View.OnClickListener listener = new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
               //releaseCamera();
                //finish();
            //}
        //};
        //backToMenuButton.setOnClickListener(listener);
    }

    /*
     * This method checks whether or not the phone has a working camera.
     * @param   context: the context of current state of the application/object. It lets newly created objects
     *                   understand what has been going on. Typically you call it to get information regarding
     *                   another part of your program (activity, package/application).
     * @return  boolean: whether or not the phone's camera is functional.
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            //This device has a camera.
            return true;
        } else {
            //No camera on this device
            return false;
        }
    }

    /*
     * This method is a safe way to create an instance of a camera object.
     * @return  c a camera object.
     */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            //Attempt to get a Camera instance.
            c = Camera.open();
        }
        catch (Exception e){
            //Camera is not available (in use or does not exist).
            Log.d(TAG, "Error opening camera: " + e.getMessage());
        }
        // returns null if camera is unavailable
        return c;
    }

    /*
     * This method creates a file Uri for saving an image or video.
     * @param   type one of two class constants defining which type of media file is being processed.
     * @return  Uri: the Uri associated with the created file.
     */
    private static Uri getOutputMediaFileUri(int type) throws IOException {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * This method creates a file wherein an image or video can be saved.
     * @param   type one of two class constants defining which type of media file is being processed.
     * @return  File: the created File object.
     */
    private static File getOutputMediaFile(int type) throws IOException {
        //The following code creates a directory. This is confusing, but a File object can be both a file
        //like you or I know, or it can also be a directory wherein multiple File objects are stored. Here,
        //we are creating a directory.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        //The following if statements check, in order, whether or not the directory has been successfully
        //created, and whether or not the location is viable.
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        //The following line creates a timestamp which we will use to name the file.
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        File mediaFile;
        //The following if/else statements create a file, named appropiately for the type of media,
        //wherein the captured media will be stored.
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "Median_" + timeStamp + "_" + ".jpg");

        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /*
     * This method is never called by the developer. Certain "pause events" trigger the phone to call
     * this method
     */
    @Override
    protected void onPause() {
        super.onPause();
        //It is essential to release the camera once an application is done using it. Otherwise, other applications
        //will not be able to access the camera.
        releaseCamera();
    }

    /*
     * This method safely releases the camera for other applications to use.
     */
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
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
