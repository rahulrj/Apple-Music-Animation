package com.haydntrigg.android;

import android.opengl.GLES20;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haydntrigg.game.supersovietsheep.R;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.joml.Matrix4f;

import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.joml.Vector4d;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by HAYDN on 1/7/2015.
 */
public class Game {

    static float CircleArcPoint(final float radius, final float x) {
        float x2 = x * x;
        float r2 = radius * radius;

        float y = (float) Math.sqrt(r2 - x2);
        return y;
    }

    private MainActivity Parent = null;
    TextView textView;
    int Width, Height;
    Matrix4f View = new Matrix4f();
    Texture boxTexture, groundTexture, ballTexture, trailTexture;
    Sprite boxSprite, groundSprite, ballSprite, trailSprite;
    World b2World;
    Vector4f DrawWhite = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    Button ResetButton, SpawnButton, TimeButton;
    Boolean IsSlow = false;

    enum ObjectType {
        Box,
        Floor,
        Ball
    }

    ObjectType SpawnType = ObjectType.Box;

    Map<Integer, Body> mCirclesMap = new HashMap<>();
    Body mPlanet = null;
    private double mLastTouchX;
    private double mLastTouchY;
    private int mPushStrength = 1000;


    Game(MainActivity parent) {
        Parent = parent;
        textView = (TextView) Parent.findViewById(R.id.frame_rate);

        b2World = new World(new Vec2(0.0f, 0f));
    }

    private void Reset() {
        for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) b2World.destroyBody(b);
        //CreateFloor(new Vec2(6.5f, 1f));
        // CreateBox(new Vec2(6.5f, 2.55f));
        //CreateBox(new Vec2(6.0f, 1.55f));
        //CreateBox(new Vec2(7.0f, 1.55f));
        createBallsOnTheLeftSide();
        //createPlanet(new Vec2(7.0f, 10.0f), null);
        createBallsOnTheRightSide();

        //CreateBall(new Vec2(10.0f, 5.0f), new Vec2(-7.5f, -5.5f));
    }

    private void createBallsOnTheLeftSide() {

        CreateBall(new Vec2(-1.0f, 10.0f), null, 1);
        CreateBall(new Vec2(-3.0f, 10.0f), null, 2);
        CreateBall(new Vec2(-5.0f, 10.0f), null, 3);
        CreateBall(new Vec2(-7.0f, 10.0f), null, 4);
        CreateBall(new Vec2(-9.0f, 10.0f), null, 5);


        CreateBall(new Vec2(-1.0f, 11.5f), null, 6);
        CreateBall(new Vec2(-3.0f, 11.5f), null, 7);
        CreateBall(new Vec2(-5.0f, 11.5f), null, 8);
        CreateBall(new Vec2(-7.0f, 11.5f), null, 9);
        CreateBall(new Vec2(-9.0f, 11.5f), null, 10);


//        for (int i = 0; i < 2; i++) {
//            CreateBall(new Vec2(-1.0f, 13.0f * ((float) Height / (float) Width) * (1.0f - e.getY() / Height)), null);
//        }
    }

    private void createBallsOnTheRightSide() {
        CreateBall(new Vec2(14.0f, 10.0f), null, 11);
        CreateBall(new Vec2(16.0f, 10.0f), null, 12);
        CreateBall(new Vec2(18.0f, 10.0f), null, 13);
        CreateBall(new Vec2(20.0f, 10.0f), null, 14);
        CreateBall(new Vec2(22.0f, 10.0f), null, 15);

        CreateBall(new Vec2(14.0f, 11.5f), null, 16);
        CreateBall(new Vec2(16.0f, 11.5f), null, 17);
        CreateBall(new Vec2(18.0f, 11.5f), null, 18);
        CreateBall(new Vec2(20.0f, 11.5f), null, 19);
        CreateBall(new Vec2(22.0f, 11.5f), null, 20);


    }

//    private void update() {
//        b2World.step(1 / 30, 10, 10);
//        b2World.clearForces();
//        for (int i = 0; i < debrisVector.length; i++) {
//            var debrisPosition:b2Vec2 = debrisVector[i].GetWorldCenter();
//            for (var j :int=0;
//            j<planetVector.length ;
//            j++){
//                var planetShape:b2CircleShape = planetVector[j].GetFixtureList().GetShape()
//                as b2CircleShape;
//                var planetRadius:Number = planetShape.GetRadius();
//                var planetPosition:b2Vec2 = planetVector[j].GetWorldCenter();
//                var planetDistance:b2Vec2 = new b2Vec2(0, 0);
//                planetDistance.Add(debrisPosition);
//                planetDistance.Subtract(planetPosition);
//                var finalDistance:Number = planetDistance.Length();
//                if (finalDistance <= planetRadius * 3) {
//                    planetDistance.NegativeSelf();
//                    var vecSum:Number = Math.abs(planetDistance.x) + Math.abs(planetDistance.y);
//                    planetDistance.Multiply((1 / vecSum) * planetRadius / finalDistance);
//                    debrisVector[i].ApplyForce(planetDistance, debrisVector[i].GetWorldCenter());
//                }
//            }
//        }
//        world.DrawDebugData();
//    }

//    private void CreateBox(Vec2 position) {
//        BodyDef bodyDef = new BodyDef();
//
//        bodyDef.position = position;
//        bodyDef.angle = 0.0f;
//        bodyDef.linearVelocity = new Vec2(0.0f, 0.0f);
//        bodyDef.angularVelocity = 0.0f;
//        bodyDef.fixedRotation = false;
//        bodyDef.active = true;
//        bodyDef.bullet = false;
//        bodyDef.allowSleep = true;
//        bodyDef.gravityScale = 1.0f;
//        bodyDef.linearDamping = 0.0f;
//        bodyDef.angularDamping = 0.0f;
//        bodyDef.userData = (Object) ObjectType.Box;
//        bodyDef.type = BodyType.DYNAMIC;
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(0.5f, 0.5f);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.userData = null;
//        fixtureDef.friction = 0.35f;
//        fixtureDef.restitution = 0.05f;
//        fixtureDef.density = 0.75f;
//        fixtureDef.isSensor = false;
//
//        Body body = b2World.createBody(bodyDef);
//        body.createFixture(fixtureDef);
//    }


    private void CreateBall(Vec2 position, @Nullable Vec2 velocity, int id) {
        Vec2 v = velocity == null ? new Vec2() : velocity;

        //Vec2 v=new Vec2(0.002f,0.001f);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position = position;
        bodyDef.gravityScale = 0.0f;


        //bodyDef.angle = 0.0f;
        //bodyDef.linearVelocity = v;
        // bodyDef.angularVelocity = 0.0f;
        //bodyDef.fixedRotation = false;
        //bodyDef.active = true;
        //bodyDef.bullet = false;
        //bodyDef.allowSleep = true;
//        if (position.x > 5.0f) {
//            bodyDef.gravityScale = -0.05f;
//        } else {
//            bodyDef.gravityScale = 0.05f;
//        }


        bodyDef.linearDamping = 2.0f;
        //bodyDef.angularDamping = 0.0f;
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

        //fixtureDef.isSensor = false;

        Body body = b2World.createBody(bodyDef);
        body.createFixture(fixtureDef);
        mCirclesMap.put(id, body);
    }

    private void createPlanet(Vec2 position, @Nullable Vec2 velocity) {
        Vec2 v = velocity == null ? new Vec2() : velocity;

        //Vec2 v=new Vec2(0.002f,0.001f);
        BodyDef bodyDef = new BodyDef();

        bodyDef.position = position;
        // bodyDef.angle = 0.0f;
        //bodyDef.linearVelocity = v;
        //bodyDef.angularVelocity = 0.0f;
        //bodyDef.fixedRotation = false;
        //bodyDef.active = true;
        //bodyDef.bullet = false;
        //bodyDef.allowSleep = true;
        //bodyDef.linearDamping = 0.0f;
        //bodyDef.angularDamping = 0.0f;
        bodyDef.userData = (Object) ObjectType.Ball;
        //bodyDef.type = BodyType.DYNAMIC;

        CircleShape shape = new CircleShape();
        shape.setRadius(0.2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.userData = null;
        fixtureDef.restitution = 0f;
        fixtureDef.density = 1.0f;

        Body body = b2World.createBody(bodyDef);
        body.createFixture(fixtureDef);
        mPlanet = body;
    }

//    private void CreateFloor(Vec2 position) {
//        BodyDef bodyDef = new BodyDef();
//
//        bodyDef.position = position;
//        bodyDef.angle = 0.0f;
//        bodyDef.linearVelocity = new Vec2(0.0f, 0.0f);
//        bodyDef.angularVelocity = 0.0f;
//        bodyDef.fixedRotation = false;
//        bodyDef.active = true;
//        bodyDef.bullet = false;
//        bodyDef.allowSleep = true;
//        bodyDef.gravityScale = 1.0f;
//        bodyDef.linearDamping = 0.0f;
//        bodyDef.angularDamping = 0.0f;
//        bodyDef.userData = (Object) ObjectType.Floor;
//        bodyDef.type = BodyType.KINEMATIC;
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(5.0f, 0.05f);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.userData = null;
//        fixtureDef.friction = 0.5f;
//        fixtureDef.restitution = 0.05f;
//        fixtureDef.density = 1.0f;
//        fixtureDef.isSensor = false;
//
//        Body body = b2World.createBody(bodyDef);
//        body.createFixture(fixtureDef);
//    }

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

        boxTexture = new Texture(Parent.getApplicationContext(), R.drawable.box);
        boxSprite = new Sprite(boxTexture);

        ballTexture = new Texture(Parent.getApplicationContext(), R.drawable.ball1);
        ballSprite = new Sprite(ballTexture);

        trailTexture = new Texture(Parent.getApplicationContext(), R.drawable.trail);
        trailSprite = new Sprite(trailTexture);

        groundTexture = new Texture(Parent.getApplicationContext(), R.drawable.ground);
        groundSprite = new Sprite(groundTexture);
        GLES20.glClearColor(235f / 255.0f, 235f / 255.0f, 255f / 255.0f, 255f / 255.0f);

        ResetButton = (Button) Parent.findViewById(R.id.reset_button);
        SpawnButton = (Button) Parent.findViewById(R.id.spawntype_button);
        TimeButton = (Button) Parent.findViewById(R.id.time_button);

        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Parent.semaphore.acquire(1);
                    Reset();
                } catch (Exception e) {

                } finally {
                    Parent.semaphore.release(1);
                }
            }
        });

        SpawnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Parent.semaphore.acquire(1);
                    switch (SpawnType) {
                        case Box:
                            SpawnType = ObjectType.Ball;
                            break;
                        default:
                            SpawnType = ObjectType.Box;
                            break;
                    }
                } catch (Exception e) {

                } finally {
                    Parent.semaphore.release(1);
                }
            }
        });

        TimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Parent.semaphore.acquire(1);
                    IsSlow = !IsSlow;

                } catch (Exception e) {

                } finally {
                    Parent.semaphore.release(1);
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Reset();
    }

    public void Update(float delta) {
        if (IsSlow) {
            b2World.step(delta * 0.2f, 20, 20);
        } else {
            b2World.step(delta, 20, 20);
        }

        b2World.clearForces();
        for (Map.Entry<Integer, Body> entry : mCirclesMap.entrySet()) {

            Vec2 debrisPosition = entry.getValue().getWorldCenter();
            //CircleShape planetShape = (CircleShape) mPlanet.getFixtureList().getShape();
            //float planetRadius = planetShape.getRadius();

            //Vec2 planetPosition = mPlanet.getWorldCenter();
            Vec2 planetPosition = new Vec2(7.0f, 10.0f);
            Vec2 planetDistance = new Vec2(0, 0);
            planetDistance.addLocal(debrisPosition);
            planetDistance.subLocal(planetPosition);
            float finalDistance = planetDistance.length();
            planetDistance.negateLocal();
            float vecSum = Math.abs(planetDistance.x) + Math.abs(planetDistance.y);
            planetDistance.mulLocal(2.0f);

            double distanceFromCenter = distanceBetweenPoints(planetPosition, entry.getValue().getPosition());
            float linearDamping = (float) (distanceFromCenter > 5 ? 2 : 2 + (5 - distanceFromCenter));
            Log.d("RAHUL",""+distanceFromCenter);
            entry.getValue().setLinearDamping(linearDamping);

            if (entry.getKey() == 6) {
                if (finalDistance < 4.0 && finalDistance > 3.0) {
                    //Log.d("RAHUL1", "YES");
                    //Vec2 impluse = new Vec2(0, 0.3f);
                    Vec2 impulse = new Vec2(0, (float) (1.0 / (finalDistance * 2.2)));
                    entry.getValue().applyLinearImpulse(impulse, entry.getValue().getWorldCenter());
                }
            }
            if (entry.getKey() == 16) {
                if (finalDistance < 3.5 && finalDistance > 2.5) {
                    Vec2 impulse = new Vec2(0, (float) (1.0 / (finalDistance * 2.2)));
                    entry.getValue().applyLinearImpulse(impulse, entry.getValue().getWorldCenter());
                }
            }

            if (entry.getKey() == 1) {
                if (finalDistance < 4.0 && finalDistance > 3.0) {
                    Vec2 impulse = new Vec2(0, (float) (-1.0 / (finalDistance * 2.0)));
                    entry.getValue().applyLinearImpulse(impulse, entry.getValue().getWorldCenter());
                }
            }
            if (entry.getKey() == 11) {
                if (finalDistance < 3.5 && finalDistance > 2.5) {
                    Vec2 impulse = new Vec2(0, (float) (-1.0 / (finalDistance * 2.8)));
                    entry.getValue().applyLinearImpulse(impulse, entry.getValue().getWorldCenter());
                }
            }


            entry.getValue().applyForce(planetDistance, entry.getValue().getWorldCenter());
        }


//        for (Body b = b2World.getBodyList(); b != null; b = b.getNext()) {
//            Vec2 position = b.getPosition();
//            if (position.y < -5 || position.x < -5 || position.x > 18) b2World.destroyBody(b);
//        }
    }


    private double distanceBetweenPoints(Vec2 point1, Vec2 point2) {
        return Math.hypot(point2.x - point1.x, point2.y - point1.y);
    }

    public void Draw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        int num_objects = b2World.getBodyCount();
        if (num_objects >= 0) {
            Body body = b2World.getBodyList();
            for (int i = 0; i < num_objects; i++) {
                Square.Color = DrawWhite;
                switch ((ObjectType) body.getUserData()) {
                    case Box:
                        // boxSprite.Draw(body.getWorldCenter(), body.getAngle(), 1.05f / 94.0f, View);
                        break;
                    case Floor:
                        //groundSprite.Draw(body.getWorldCenter(), body.getAngle(), 1.05f / 100.0f, View);
                        break;
                    case Ball:
                        Vec2 position = body.getWorldCenter();
                        // 0.5/1.05=0.4761 Now 1.2/0.4761=2.52
                        ballSprite.Draw(position, body.getAngle(), 2.73f / 100.0f, View);
//                        if (ballSprite.DrawTexture != null) {
//                            Log.d("RAHUL", ballSprite.DrawTexture.GetWidth() * (1.05f / 100.0f) + " " + ballSprite.DrawTexture.GetHeight());
//                        }


//                        Vec2 velocity = new Vec2(body.getLinearVelocity());
//                        float length = velocity.length();
//                        velocity.normalize();
//                        if (length > 1.0) {
//
//                            Vec2 direction = new Vec2(-velocity.y, velocity.x);
//
//                            final float trail_offset_x = 0.3f;
//                            final float trail_offset_l = 0.1f;
//                            final float base_size = 1.05f / 48.0f;
//                            final float length_size = base_size * length * 3.5f;
//
//                            Vec2 offsetA = new Vec2(direction.x * trail_offset_x, direction.y * trail_offset_x); // Left
//                            Vec2 offsetB = new Vec2(direction.x * -trail_offset_x, direction.y * -trail_offset_x); // Right
//
//
//                            final float side_distance = -(CircleArcPoint(0.5f + trail_offset_l / 2.0f, trail_offset_x) + trail_offset_l + length_size / 2);
//                            final float middle_distance = -(CircleArcPoint(0.5f + trail_offset_l / 2.0f, 0.0f) + trail_offset_l + length_size / 2);
//                            Vec2 offsetC = new Vec2(velocity.x * side_distance, velocity.y * side_distance); // Backwards Offset Sides
//                            Vec2 offsetD = new Vec2(velocity.x * middle_distance, velocity.y * middle_distance); // Backwards Offset Middle
//
//
//                            Vec2 positionA = new Vec2(position.x + offsetC.x + offsetA.x, position.y + offsetC.y + offsetA.y);
//                            Vec2 positionB = new Vec2(position.x + offsetC.x + offsetB.x, position.y + offsetC.y + offsetB.y);
//                            Vec2 positionC = new Vec2(position.x + offsetD.x, position.y + offsetD.y);
//                            //Vec2 positionB = new Vec2(offsetB.x + offsetC.x + position.x,offsetB.y+offsetC.y + position.y);
//                            //Vec2 positionC = new Vec2(velocity.x * middle_distance + position.x,velocity.y * middle_distance + position.y);
//
//
//                            float angle = (float) Math.atan2(direction.y, -direction.x);
//                            float transparency = Math.min((length - 1.0f) / 5.0f, 1.0f);
//                            transparency *= transparency;
//                            Square.Color = new Vector4f(1.0f, 1.0f, 1.0f, transparency);
//                            trailSprite.Draw(positionA, angle, new Vec2(base_size, length_size), View);
//                            trailSprite.Draw(positionB, angle, new Vec2(base_size, length_size), View);
//                            trailSprite.Draw(positionC, angle, new Vec2(base_size, length_size), View);
//                        }


                        break;
                }
                body = body.getNext();
            }
        }
    }

    public void UI() {
        textView.setText("JBox2D Box Example. Written by Haydn Trigg");
        switch (SpawnType) {
            case Box:
                SpawnButton.setText("Box");
                break;
            case Ball:
                SpawnButton.setText("Ball");
                break;
            default:
                SpawnButton.setText("Unknown?");
                break;
        }
        if (IsSlow) TimeButton.setText("Slow");
        else TimeButton.setText("Fast");
    }


    public void TouchEvent(MotionEvent event) {
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
                    Vec2 direction = new Vec2((float) (mPushStrength * dx), (float) (-mPushStrength * dy));
                    entry.getValue().applyForce(direction, entry.getValue().getWorldCenter());
                    //Log.d("RAHUL", direction.toString());
                }


                break;
            case ACTION_UP:
                break;

        }
    }

    public void SetSize(int width, int height) {
        Width = width;
        Height = height;

        final float height_ratio = ((float) height) / ((float) width);
        final float base_units = 13f;
        final float pixels_per_unit = 100.0f;
        float virtual_width = base_units;
        float virtual_height = virtual_width * height_ratio;


        View = new Matrix4f().ortho(0, virtual_width, 0, virtual_height, 1, -1);
        //.orthoM(View,0,0,Width,0,Height,1,-1);
    }
}
