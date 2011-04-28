/**
 * Copyright 2011 Massimo Gaddini
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  
 */
package com.sgxmobileapps.droidmaze.ui;

import android.hardware.SensorManager;
import android.util.DisplayMetrics;

import com.badlogic.gdx.math.Vector2;
import com.sgxmobileapps.droidmaze.game.GameProfileManager;
import com.sgxmobileapps.droidmaze.maze.Maze;
import com.sgxmobileapps.droidmaze.R;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Callback;

import java.util.concurrent.Callable;


/**
 * @author Massimo Gaddini
 *
 */
public class MazeActivity extends BaseGameActivity implements IAccelerometerListener {

    private static final float SCORE_BAR_HEIGHT_PERC = 0.1f;
    private static final float AD_BAR_HEIGHT_PERC = 0.1f;

    private PhysicsWorld       mPhysicsWorld;
    private Camera             mCamera;
    private int                mCameraWidth;
    private int                mCameraHeight;
    private int                mScoreBarWidth;
    private int                mScoreBarHeight;
    private int                mAdBarWidth;
    private int                mAdBarHeight;
    private int                mMazeAreaWidth;
    private int                mMazeAreaHeight;
    private Texture            mTexture;
    private TextureRegion      mMarkerTexture;

    private Maze               mMaze                 = new Maze();
    private GameProfileManager mLevelManager         = new GameProfileManager(); /* TODO */

    private void computeDimension() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mCameraHeight = dm.heightPixels;
        mCameraWidth = dm.widthPixels;

        mScoreBarHeight = (int) ( mCameraHeight * SCORE_BAR_HEIGHT_PERC );
        mScoreBarWidth = mCameraWidth;
        
        mAdBarHeight = (int) ( mCameraHeight * AD_BAR_HEIGHT_PERC );
        mAdBarWidth = mCameraWidth;

        mMazeAreaHeight = mCameraHeight - mScoreBarHeight - mAdBarHeight;
        mMazeAreaWidth = mCameraWidth;
    }

    public Engine onLoadEngine() {

        computeDimension();

        mCamera = new Camera(0, 0, mCameraWidth, mCameraHeight);

        return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
                new RatioResolutionPolicy(mCameraWidth, mCameraHeight), mCamera));
    }

    public void onLoadResources() {

        /* Texture */
        mTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        /* TextureRegion */
        mMarkerTexture = TextureRegionFactory.createFromAsset(mTexture, this,
                "gfx/face_circle.png", 0, 0); // 32x32
        mEngine.getTextureManager().loadTexture(mTexture);

        enableAccelerometerSensor(this);
    }

    public Scene onLoadScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        final Scene scene = new Scene(2);
        scene.setBackground(new ColorBackground((float) ( 51.0 / 255.0 ),
                (float) ( 189.0 / 255.0 ), (float) ( 200.0 / 255.0 )));

        mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

        scene.registerUpdateHandler(mPhysicsWorld);

        this.doAsync(R.string.dialog_loading_title, R.string.dialog_loading_message,
                new Callable<Void>() {

                    public Void call() throws Exception {
                        mMaze.setMazeGenerator(mLevelManager.getGenerator());
                        mMaze.setMazeSize(mLevelManager.getMazeHeight(), mLevelManager
                                .getMazeWidth());
                        mMaze.generateMaze();
                        return null;
                    }

                }, new Callback<Void>() {

                    public void onCallback(Void pCallbackValue) {
                        mMaze.drawMaze(0, mScoreBarHeight, mMazeAreaHeight, mMazeAreaWidth,
                                getEngine().getScene().getChild(0), mPhysicsWorld);
                        mMaze.addMarker(mMarkerTexture, getEngine().getScene().getChild(1),
                                mPhysicsWorld);
                    }
                });

        return scene;
    }

    public void onLoadComplete() {

    }

    public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
        mPhysicsWorld
                .setGravity(new Vector2(-pAccelerometerData.getX(), pAccelerometerData.getY()));
    }
}
