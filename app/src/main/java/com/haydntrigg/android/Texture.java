package com.haydntrigg.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by HAYDN on 1/7/2015.
 */
public class Texture {

    private int TextureID[] = {0};
    private int Width,Height;
    public Texture(final Context context, int resource_id)
    {
        loadTexture(context,resource_id);
    }

    public void Destroy()
    {
        GLES20.glDeleteTextures(1,TextureID,0);
    }


    public int GetTextureID()
    {
        return TextureID[0];
    }

    public int GetWidth()
    {
        return Width;
    }

    public int GetHeight()
    {
        return Height;
    }

    public void BindTexture(int id)
    {
        GLES20.glActiveTexture(id);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureID[0]);
    }

    private void loadTexture(final Context context, final int resourceId)
    {
        GLES20.glGenTextures(1, TextureID, 0);

        try
        {
            if (TextureID[0] != 0)
            {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;   // No pre-scaling

                // Read in the resource
                 Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
                bitmap=drawTextToBitmap(context,"Hello",bitmap);

                Width = bitmap.getWidth();
                Height = bitmap.getHeight();

                // Bind to the texture in OpenGL
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, TextureID[0]);

                // Set filtering
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

                // Load the bitmap into the bound texture.
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
                //GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES30.GL_COMPRESSED_, bitmap, 0);
                //Buffer buffer = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight());
                //bitmap.copyPixelsFromBuffer(buffer);
                //GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0, GLES20.GL_RGB, bitmap.getWidth(),bitmap.getHeight(),0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE, buffer);
                //GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB, bitmap.getWidth(),bitmap.getHeight(),0,GLES30.GL_RGBA,GLES30.GL_UNSIGNED_BYTE, buffer);

                // Recycle the bitmap, since its data has been loaded into OpenGL.
                bitmap.recycle();
            }

            if (TextureID[0] == 0)
            {
                throw new RuntimeException("Error loading texture.");
            }
        }
        catch (Exception e)
        {
            GLES20.glDeleteTextures(1,TextureID,0);
            TextureID[0] = 0;
            throw e;
        }
    }

    public Bitmap drawTextToBitmap(Context context, String text,Bitmap bitmap) {
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.WHITE);
        // text size in pixels
        paint.setTextSize((int) (9 * scale));
        // text shadow
        //paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(text, x, y, paint);

        return bitmap;
    }
}
