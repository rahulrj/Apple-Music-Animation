package com.android.animations;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.Calendar;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MainActivity extends Activity implements Renderer {
    private Semaphore mSemaphore = new Semaphore(1, true);
    private GLSurfaceView mGlSurfaceView = null;
    private MainActivity mActivity = this;
    private RelativeLayout mLayoutBody;
    private Game mGame;
    private long mLastTicks = -1;
    private boolean mInit = false;

    private final Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mSemaphore.acquire(1);
                mGame.setUpUi();
            } catch (Exception e) {

            } finally {
                mSemaphore.release(1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mLayoutBody = (RelativeLayout) findViewById(R.id.layout_body);
        mLayoutBody.setVisibility(View.INVISIBLE);


        // Get the Surface View & Holder
        mGlSurfaceView = (GLSurfaceView) findViewById(R.id.game_canvas);

        //Chose EGL Config Here To Set Element Size For RGB data Alpha,
        mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        mGlSurfaceView.setEGLContextClientVersion(2);
        mGlSurfaceView.setRenderer(this);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);


        mGame = new Game(this);

    }

    public Semaphore getSemaphore() {
        return mSemaphore;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            mSemaphore.acquire(1);
            GLES20.glClearColor(100.0f / 255.0f, 149f / 255.0f, 237f / 255.0f, 255f / 255.0f);
            mGame.Init();
            runOnUiThread(uiRunnable);
            mLayoutBody.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        } finally {
            mSemaphore.release(1);
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        try {
            mSemaphore.acquire(1);
            gl.glViewport(0, 0, width, height);
            mGame.SetSize(width, height);
        } catch (Exception e) {

        } finally {
            mSemaphore.release(1);
        }
    }

    float Accumulator = 0.0f;

    public void onDrawFrame(GL10 gl) {
        try {
            //Log.d("RAHUL","DRAW");
            mSemaphore.acquire(1);
            if (mLastTicks == -1) mLastTicks = Calendar.getInstance().getTime().getTime();
            final float min_timestep = 1.0f / 100.0f;
            // Calculate Delta Ticks
            long nowticks = Calendar.getInstance().getTime().getTime();
            Accumulator += (float) (nowticks - mLastTicks) / 1000.0f;
            mLastTicks = nowticks;
            // Update for the total amount of time and any remainder. This ensures smoothest framerate.
            while (Accumulator > min_timestep) {
                mGame.Update(min_timestep);
                Accumulator -= min_timestep;
            }
            //game.Update(total_delta);
            mGame.draw();
            //runOnUiThread(uiRunnable);
        } catch (Exception e) {

        } finally {
            mSemaphore.release(1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mSemaphore.acquire(1);
            mGame.onTouchEvent(event);
        } catch (Exception e) {
            // ignore
        } finally {
            mSemaphore.release(1);
        }

        return true;
    }
}