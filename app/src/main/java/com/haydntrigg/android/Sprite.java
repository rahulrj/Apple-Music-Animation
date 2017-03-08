package com.haydntrigg.android;

import org.jbox2d.common.Vec2;
import org.joml.Matrix4f;

public class Sprite {

    private Texture DrawTexture;
    public Sprite(Texture texture)
    {
        DrawTexture = texture;
    }

    public void Draw(Vec2 position, float rotation, float scale,Matrix4f view)
    {
        DrawTexture.BindTexture(0);
        Matrix4f mtranslate = new Matrix4f().translate(position.x, position.y, 0.0f);
        Matrix4f mscale = new Matrix4f().scale((float) DrawTexture.GetWidth() * scale, (float) DrawTexture.GetHeight() * scale, 1.0f);
        Matrix4f mrotate = new Matrix4f().rotate(rotation * (float)(180.0/Math.PI), 0.0f, 0.0f, -1.0f);


        Matrix4f mvp = new Matrix4f().mul(view).mul(mtranslate).mul(mrotate).mul(mscale);

        Square.DrawSquare.draw(mvp);
    }

    public void Draw(Vec2 position, float rotation, Vec2 scale,Matrix4f view)
    {
        DrawTexture.BindTexture(0);
        Matrix4f mtranslate = new Matrix4f().translate(position.x, position.y, 0.0f);
        Matrix4f mscale = new Matrix4f().scale((float) DrawTexture.GetWidth() * scale.x, (float) DrawTexture.GetHeight() * scale.y, 1.0f);
        Matrix4f mrotate = new Matrix4f().rotate(rotation * (float)(180.0/Math.PI), 0.0f, 0.0f, -1.0f);


        Matrix4f mvp = new Matrix4f().mul(view).mul(mtranslate).mul(mrotate).mul(mscale);

        Square.DrawSquare.draw(mvp);
    }

    public void Draw(Vec2 position, float rotation, Vec2 scale,Matrix4f view, float transparency)
    {
        DrawTexture.BindTexture(0);
        Matrix4f mtranslate = new Matrix4f().translate(position.x, position.y, 0.0f);
        Matrix4f mscale = new Matrix4f().scale((float) DrawTexture.GetWidth() * scale.x, (float) DrawTexture.GetHeight() * scale.y, 1.0f);
        Matrix4f mrotate = new Matrix4f().rotate(rotation * (float)(180.0/Math.PI), 0.0f, 0.0f, -1.0f);


        Matrix4f mvp = new Matrix4f().mul(view).mul(mtranslate).mul(mrotate).mul(mscale);
        Square.DrawSquare.draw(mvp);
    }
}
