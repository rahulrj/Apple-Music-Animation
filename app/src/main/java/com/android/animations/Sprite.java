package com.android.animations;

import org.jbox2d.common.Vec2;
import org.joml.Matrix4f;

public class Sprite {

    public Texture mDrawTexture;

    public Sprite(Texture texture) {
        mDrawTexture = texture;
    }

    public void draw(Vec2 position, float rotation, float scale, Matrix4f view) {
        mDrawTexture.bindTexture(0);
        Matrix4f mtranslate = new Matrix4f().translate(position.x, position.y, 0.0f);
        Matrix4f mscale = new Matrix4f().scale((float) mDrawTexture.getWidth() * scale, (float) mDrawTexture.getHeight() * scale, 1.0f);
        Matrix4f mrotate = new Matrix4f().rotate(rotation * (float) (180.0 / Math.PI), 0.0f, 0.0f, -1.0f);


        Matrix4f mvp = new Matrix4f().mul(view).mul(mtranslate).mul(mrotate).mul(mscale);
        Square.sDrawSquare.draw(mvp);
    }
}
