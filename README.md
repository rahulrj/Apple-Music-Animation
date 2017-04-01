# Apple Music Animation

## Overview
This demo emulates the animation of the circles which are present in Apple Music iOS app. Unfortunately, Apple Music Android
app doesn't come with such animations. So i tried my hands and made it for Android. The initial version of the animation
covers the most important functionalities of that.

## GIF demo
The above video link is a high resolution demo for the animation. In case you dont want to redirect to that demo, here is a
GIF demo that will play in here, but it has a low frame rate. I didn't find any tool to convert a high quality video into
a GIF with a high frame rate.

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ![](animation_demo.gif)

## How is this made?
Few libraies that helped me enormously in making this animation is
1. [Box2d](https://github.com/erincatto/Box2D)  by Erin Catto. Although this library is very huge and it covers all the
different aspects of simulating the real world physics in programming world, i was able to make this animation by using only
a handful of its APIs. Box2d is the most popular physics engine in the world. It contains APIs for almost everything that we
want to simulate like Collision, contacts between bodies, radial gravity. Box2d forms the basis of many game engines like
Cocos2d, which also involves the rendering engine in itself.

2. [JBox2D Demo](https://github.com/HaydnTrigg/JBox2D-Demo) by Hadyn Trigg. JBox2d is actually the Java code of Box2d plus
some additional APIs. I didn't know how to render the objects on screen in Android when i started with Box2d. The rendering
engine in the animation is based out on this demo app.

3. [JOML](https://github.com/JOML-CI/JOML) This is the Java OpenGL Mathematics library is used to calculate matrices for OpenGL that OpenGL shaders can use to draw geometry to the screen. Its also bundled in the JBox2d demo.


I will be publishing a blog regarding how this animaton is made and what all conecpts of physics goes into this. Please watch
out my [medium blog](https://medium.com/@rahulraja) and my [personal blog](http://rahulrj.github.io/).


## Demo
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <a href="https://www.youtube.com/watch?v=lzkK9rtabZw"><img src="http://img.youtube.com/vi/lzkK9rtabZw/maxresdefault.jpg" border="10" /></a>



## Next Release
1. Click to enlarge the circles
2. Reset Animation
3. Swirl animation of the circles while they move.
4. And of course a blog regarding this.
