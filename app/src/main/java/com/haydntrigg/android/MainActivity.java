package com.haydntrigg.android;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.haydntrigg.game.supersovietsheep.R;

import java.util.Calendar;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MainActivity extends Activity implements Renderer {
    Semaphore semaphore = new Semaphore(1,true);
    private GLSurfaceView glSurfaceView = null;
    private SurfaceHolder surfaceHolder = null;
    private MainActivity activity = this;
    private RelativeLayout LayoutBody;

    private final Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            try
            {
                semaphore.acquire(1);
                game.UI();
            }
            catch (Exception e)
            {

            }
            finally
            {
                semaphore.release(1);
            }
        }
    };
    Game game;
    long lastTicks = -1;
    boolean init = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        LayoutBody = (RelativeLayout) findViewById(R.id.layout_body);
        LayoutBody.setVisibility(View.INVISIBLE);



        // Get the Surface View & Holder
        glSurfaceView = (GLSurfaceView) findViewById(R.id.game_canvas);

        //Chose EGL Config Here To Set Element Size For RGB data Alpha,
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(this);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);



        game = new Game(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try
        {
            semaphore.acquire(1);
            GLES20.glClearColor(100.0f / 255.0f, 149f / 255.0f, 237f / 255.0f, 255f / 255.0f);
            game.Init();
            runOnUiThread(uiRunnable);
            LayoutBody.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {

        }
        finally
        {
            semaphore.release(1);
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        try
        {
            semaphore.acquire(1);
            gl.glViewport(0, 0, width, height);
            game.SetSize(width, height);
        }
        catch (Exception e)
        {

        }
        finally
        {
            semaphore.release(1);
        }
    }
    float Accumulator = 0.0f;
    public void onDrawFrame(GL10 gl) {
        try
        {
            semaphore.acquire(1);
            if(lastTicks == -1) lastTicks = Calendar.getInstance().getTime().getTime();
            final float min_timestep = 1.0f / 100.0f;
            // Calculate Delta Ticks
            long nowticks = Calendar.getInstance().getTime().getTime();
            Accumulator += (float) (nowticks - lastTicks) / 1000.0f;
            lastTicks = nowticks;
            // Update for the total amount of time and any remainder. This ensures smoothest framerate.
            while (Accumulator > min_timestep) {
                game.Update(min_timestep);
                Accumulator -= min_timestep;
            }
            //game.Update(total_delta);
            game.Draw();
            runOnUiThread(uiRunnable);
        }
        catch (Exception e)
        {

        }
        finally
        {
            semaphore.release(1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try
        {
            semaphore.acquire(1);
            game.TouchEvent(event);
        }
        catch (Exception e)
        {

        }
        finally
        {
            semaphore.release(1);
        }

        return true;
    }
}