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

import android.graphics.Color;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.sgxmobileapps.droidmaze.game.GameProfileManager;
import com.sgxmobileapps.droidmaze.maze.Maze;
import com.sgxmobileapps.droidmaze.R;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.util.Callback;

import java.util.concurrent.Callable;


/**
 * @author Massimo Gaddini
 *
 */
public class MazeActivity extends BaseActivity implements IAccelerometerListener {

    private PhysicsWorld       mPhysicsWorld;
    private TextureRegion      mMarkerTexture;
    private Font               mDroidFont;

    private Maze               mMaze                 = new Maze();
    private GameProfileManager mLevelManager         = new GameProfileManager(); /* TODO */
    private int                mElapsedSeconds       = 0;
    

    public void onLoadResources() {

        /* Texture */
        Texture droidFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        Texture texture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        
        /* Font */
        FontFactory.setAssetBasePath("font/");
        mDroidFont = FontFactory.createFromAsset(droidFontTexture, this, "Droid.ttf", (int)(getScoreBarHeight()*0.5), true, Color.BLACK);

        /* TextureRegion */
        mMarkerTexture = TextureRegionFactory.createFromAsset(texture, this, "gfx/face_circle.png", 0, 0); // 32x32
        
        mEngine.getTextureManager().loadTextures(texture, droidFontTexture);
        mEngine.getFontManager().loadFonts(mDroidFont);

        enableAccelerometerSensor(this);
    }

    public Scene onLoadScene() {

        final Scene scene = new Scene(2);
        scene.setBackground(new ColorBackground((float) ( 51.0 / 255.0 ),
                (float) ( 189.0 / 255.0 ), (float) ( 200.0 / 255.0 )));

        mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        scene.registerUpdateHandler(mPhysicsWorld);
        
        final ChangeableText elapsedText = new ChangeableText(0, (getScoreBarHeight() - mDroidFont.getLineHeight())/2, mDroidFont, "00:00", "XXXXX".length());
        scene.getLastChild().attachChild(elapsedText);

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
                        mMaze.drawMaze(0, getScoreBarHeight(), getMazeAreaHeight(), getMazeAreaWidth(),
                                getEngine().getScene().getChild(0), mPhysicsWorld);
                        mMaze.addMarker(mMarkerTexture, getEngine().getScene().getChild(1),
                                mPhysicsWorld);
                        
                        scene.registerUpdateHandler(new TimerHandler(1.0f, true, new ITimerCallback() {
                            
                            public void onTimePassed(final TimerHandler pTimerHandler) {
                                mElapsedSeconds = (int)mEngine.getSecondsElapsedTotal();
                                elapsedText.setText(String.format("%02d:%02d", mElapsedSeconds/60, mElapsedSeconds%60));
                            }
                        }));

                    }
                });

        return scene;
    }

    public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
        mPhysicsWorld.setGravity(new Vector2(-pAccelerometerData.getX(), pAccelerometerData.getY()));
    }

    public void onLoadComplete() {
        // TODO Auto-generated method stub
        
    }
}
