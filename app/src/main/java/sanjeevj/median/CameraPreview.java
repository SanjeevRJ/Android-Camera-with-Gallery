package sanjeevj.median;

/**Sanjeev Janarthanan
 *Final Project
 *An Android Application which takes pictures and can store them in a gallery.
 *CameraPreview.java
 *Version 3
 *May 27, 2015
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Sanjeev.Janarthanan on 5/15/2015.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    /*
     * This is a robust way of storing the name of the activity in a class constant. This constant
     * is used when error messages are displayed to the user.
     */
    public static final String TAG = CameraPreview.class.getSimpleName();
    /*
     * This member variable can control a the display of a surface over an extended period of time.
     */
    private SurfaceHolder mHolder;
    /*
     * A member variable which is a Camera object
     */
    private Camera mCamera;

    /*
     * This is an object and NOT an activity. As such, the following is a constructor.
     * @param   context   the context of current state of the application.
     */
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        //Initializes our SurfaceHolder member variable.
        mHolder = getHolder();
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /*
     * This method is called when a SurfaceHolder object is created.
     * @param   holder a SurfaceHolder object.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        //This try-catch block attempts to set the view of the SurfaceHolder object to a camera preview.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } else {
                Log.d(TAG, "Error setting camera preview:");
            }
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }
    /*
     * This method is called when the SurfaceHolder object is destroyed.
     * @param   holder a SurfaceHolder object.
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Once the camera is no longer needed (which we can assume once the preview has stopped),
        //releasing the camera is necessary.
        mCamera.release();
    }
    /*
     * This method is called when the SurfaceHolder "changes." The SurfaceHolder changes, mainly, when the screen
     * is rotated.
     * @param   holder a SurfaceHolder object
     * @param   format not used currently. (I can implement this, and the following two params, once I account
     *                 for multiple orientations of the screen).
     * @param   w not currently used
     * @param   h not currently used
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null){
            //The preview surface does not exist.
            return;
        }

        //The preview must be stopped before making changes.
        try {
            mCamera.stopPreview();
        } catch (Exception e){

        }

        // Start the preview with new settings.
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
