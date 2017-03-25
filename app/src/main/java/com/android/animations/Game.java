package com.android.animations;

import android.opengl.GLES20;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.joml.Matrix4f;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;


class Game {

    private MainActivity mParentActivity = null;
    private TextView mResetTextView;
    private Matrix4f mView = new Matrix4f();
    private Texture mBallTexture;
    private Sprite mBallSprite;
    private World mB2World;
    private Vector4f mDrawWhite = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private float[] LEFT_CIRCLES_X = {-3.0f, -5.0f, -7.0f, -9.0f, -11.0f};
    private float[] RIGHT_CIRCLES_X = {16.0f, 18.0f, 20.0f, 22.0f, 24.0f};
    private final float[] CIRCLES_Y_COORDINATES = {10.0f, 11.5f};
    private final float HORIZONTAL_WALL_LENGTH = 125.0f;
    private final float VERTICAL_WALL_LENGTH = 25.0f;


    private enum ObjectType {
        Wall,
        Ball
    }


    private Map<Integer, Body> mCirclesMap = new HashMap<>();
    private double mLastTouchX;
    private double mLastTouchY;
    private static final int PUSH_STRENGTH = 25000;


    Game(MainActivity parent) {
        mParentActivity = parent;
        mResetTextView = (TextView) mParentActivity.findViewById(R.id.frame_rate);
        mB2World = new World(new Vec2(0.0f, 0f));
    }

    private void reset() {
        for (Body b = mB2World.getBodyList(); b != null; b = b.getNext()) mB2World.destroyBody(b);
        createHorizontalWall(new Vec2(-50f, 1f));
        createHorizontalWall(new Vec2(-50f, 20f));
        createVerticalWall(new Vec2(-30f, 0f));
        createVerticalWall(new Vec2(75f, 0f));

        createBallsOnTheLeftSide();
        createBallsOnTheRightSide();
    }

    private void createBallsOnTheLeftSide() {
        for (int i = 1; i <= 10; i++) {
            if (i > 5) {
                createBall(new Vec2(LEFT_CIRCLES_X[i - 6], CIRCLES_Y_COORDINATES[1]), null, i);
            } else {
                createBall(new Vec2(LEFT_CIRCLES_X[i - 1], CIRCLES_Y_COORDINATES[0]), null, i);
            }
        }
    }

    private void createBallsOnTheRightSide() {
        for (int i = 1; i <= 10; i++) {
            if (i > 5) {
                createBall(new Vec2(RIGHT_CIRCLES_X[i - 6], CIRCLES_Y_COORDINATES[1]), null, i + 10);
            } else {
                createBall(new Vec2(RIGHT_CIRCLES_X[i - 1], CIRCLES_Y_COORDINATES[0]), null, i + 10);
            }
        }
    }

    private void createBall(Vec2 position, @Nullable Vec2 velocity, int id) {
        Vec2 v = velocity == null ? new Vec2() : velocity;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position = position;
        bodyDef.gravityScale = 0.0f;
        bodyDef.linearDamping = 2.0f;
        bodyDef.userData = (Object) ObjectType.Ball;
        bodyDef.type = BodyType.DYNAMIC;

        CircleShape shape = new CircleShape();
        shape.setRadius(1.3f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0f;
        fixtureDef.density = 1.0f;

        Body body = mB2World.createBody(bodyDef);
        body.createFixture(fixtureDef);
        mCirclesMap.put(id, body);
    }

    private void createHorizontalWall(Vec2 position) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.userData = (Object) ObjectType.Wall;
        bodyDef.type = BodyType.KINEMATIC;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(HORIZONTAL_WALL_LENGTH, 0.05f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        Body body = mB2World.createBody(bodyDef);
        body.createFixture(fixtureDef);
    }


    private void createVerticalWall(Vec2 position) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        bodyDef.userData = (Object) ObjectType.Wall;
        bodyDef.type = BodyType.KINEMATIC;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.05f, VERTICAL_WALL_LENGTH);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        Body body = mB2World.createBody(bodyDef);
        body.createFixture(fixtureDef);
    }

    public void Init() {

        Square.InitSquare();

        // Enable 2D Textures
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        // Enable Culling
        //GLES20.glFrontFace(GLES20.GL_CCW);
        //GLES20.glEnable(GLES20.GL_CULL_FACE);
        //GLES20.glCullFace(GLES20.GL_BACK);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);


        mBallTexture = new Texture(mParentActivity.getApplicationContext(), R.drawable.ball);
        mBallSprite = new Sprite(mBallTexture);

        GLES20.glClearColor(235f / 255.0f, 235f / 255.0f, 255f / 255.0f, 255f / 255.0f);

        mResetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mParentActivity.getSemaphore().acquire(1);
                    reset();
                } catch (Exception e) {

                } finally {
                    mParentActivity.getSemaphore().release(1);
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reset();
    }

    void Update(float delta) {

        mB2World.step(delta, 20, 20);

        mB2World.clearForces();
        for (Map.Entry<Integer, Body> entry : mCirclesMap.entrySet()) {

            Vec2 circlePosition = entry.getValue().getWorldCenter();
            Vec2 centerPosition = new Vec2(7.0f, 10.0f);
            Vec2 centertDistance = new Vec2(0, 0);
            centertDistance.addLocal(circlePosition);
            centertDistance.subLocal(centerPosition);

            float finalDistance = centertDistance.length();
            centertDistance.negateLocal();

            centertDistance.mulLocal((float) (1.0 / (finalDistance * 0.030)));

            double distanceFromCenter = distanceBetweenPoints(centerPosition, entry.getValue().getPosition());
            float linearDamping = (float) (distanceFromCenter > 5 ? 2 : 2 + (5 - distanceFromCenter));
            entry.getValue().setLinearDamping(linearDamping);

            if (entry.getKey() == 6) {
                if (finalDistance < 5.0 && finalDistance > 3.0) {
                    Vec2 impulse = new Vec2(0, (float) (1.0 / (finalDistance * 0.8)));
                    entry.getValue().applyLinearImpulse(impulse, entry.getValue().getWorldCenter());
                }
            }

            if (entry.getKey() == 1) {
                if (finalDistance < 5.0 && finalDistance > 3.0) {
                    Vec2 impulse = new Vec2(0, (float) (-1.0 / (finalDistance * 0.8)));
                    entry.getValue().applyLinearImpulse(impulse, entry.getValue().getWorldCenter());
                }
            }

            if (entry.getKey() == 16) {
                if (finalDistance < 4.0 && finalDistance > 2.0) {
                    Vec2 impulse = new Vec2(0, (float) (1.0 / (finalDistance * 0.8)));
                    entry.getValue().applyLinearImpulse(impulse, entry.getValue().getWorldCenter());
                }
            }


            if (entry.getKey() == 11) {
                if (finalDistance < 4.0 && finalDistance > 2.0) {
                    Vec2 impulse = new Vec2(0, (float) (-1.0 / (finalDistance * 0.8)));
                    entry.getValue().applyLinearImpulse(impulse, entry.getValue().getWorldCenter());
                }
            }

            entry.getValue().applyForce(centertDistance, entry.getValue().getWorldCenter());
        }
    }


    private double distanceBetweenPoints(Vec2 point1, Vec2 point2) {
        return Math.hypot(point2.x - point1.x, point2.y - point1.y);
    }

    void draw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        int num_objects = mB2World.getBodyCount();
        if (num_objects >= 0) {
            Body body = mB2World.getBodyList();
            for (int i = 0; i < num_objects; i++) {
                Square.mColor = mDrawWhite;
                switch ((ObjectType) body.getUserData()) {
                    case Ball:
                        Vec2 position = body.getWorldCenter();
                        // 0.5/1.05=0.4761 Now 1.2/0.4761=2.52
                        mBallSprite.draw(position, body.getAngle(), 2.73f / 100.0f, mView);
                        break;
                }
                body = body.getNext();
            }
        }
    }

    void setUpUi() {
        mResetTextView.setText("Reset");
    }

    /**
     * Move the circles on touch event
     */
    void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN:
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();
                break;
            case ACTION_MOVE:
                double x = event.getX();
                double y = event.getY();
                double dx = x - mLastTouchX;
                double dy = y - mLastTouchY;
                double b = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                dx = (b == 0) ? 0 : (dx / b);
                dy = (b == 0) ? 0 : (dy / b);
                if (dx == 0 && dy == 0) {
                    return;
                }
                for (Map.Entry<Integer, Body> entry : mCirclesMap.entrySet()) {
                    Vec2 direction = new Vec2((float) (PUSH_STRENGTH * dx), (float) (-PUSH_STRENGTH * dy));
                    entry.getValue().applyForce(direction, entry.getValue().getWorldCenter());
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            case ACTION_UP:
                break;

        }
    }

    void SetSize(int width, int height) {
        final float height_ratio = ((float) height) / ((float) width);
        final float base_units = 13f;
        float virtual_width = base_units;
        float virtual_height = virtual_width * height_ratio;
        mView = new Matrix4f().ortho(0, virtual_width, 0, virtual_height, 1, -1);
    }
}
