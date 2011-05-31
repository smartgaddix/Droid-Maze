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

import android.util.DisplayMetrics;

import com.sgxmobileapps.droidmaze.game.GameProfileManager;
import com.sgxmobileapps.droidmaze.ui.shape.LevelBarShape;
import com.sgxmobileapps.droidmaze.ui.shape.LoadingShape;
import com.sgxmobileapps.droidmaze.ui.shape.MazeShape;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Callback;


/**
 * Main game activity
 * 
 * @author Massimo Gaddini
 *
 */
public class MazeActivity extends BaseGameActivity {
    private static final float SCORE_BAR_HEIGHT_PERC = 0.1f;
    private static final float AD_BAR_HEIGHT_PERC = 0.1f;

    private int                mCameraWidth;
    private int                mCameraHeight;
    private int                mScoreBarWidth;
    private int                mScoreBarHeight;
    private int                mAdBarWidth;
    private int                mAdBarHeight;
    private int                mMazeAreaWidth;
    private int                mMazeAreaHeight;
    
    private GameProfileManager mLevelManager = new GameProfileManager(); /* TODO */
    private LevelBarShape mLevelBar; 
    private MazeShape mMaze;
    private LoadingShape mLoading;
    
    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.IGameInterface#onLoadEngine()
     */
    @Override
    public Engine onLoadEngine() {
        computeDimension();
        Camera camera = new Camera(0, 0, mCameraWidth, mCameraHeight);
        return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
                new RatioResolutionPolicy(mCameraWidth, mCameraHeight), camera));
    }

    
    /* (non-Javadoc)
     * @see org.anddev.andengine.ui.IGameInterface#onLoadResources()
     */
    @Override
    public void onLoadResources() {
        mLevelBar = new LevelBarShape(0, 0, mScoreBarWidth, mScoreBarHeight);
        mLevelBar.loadResources(getEngine().getTextureManager(), getEngine().getFontManager(), this);
        
        mMaze = new MazeShape(0, mScoreBarHeight, 
        		mMazeAreaWidth, mMazeAreaHeight, 
        		mLevelManager.getMazeHeight(), mLevelManager.getMazeWidth(), 
        		mLevelManager.getGenerator(), this);
        mMaze.loadResources(getEngine().getTextureManager(), getEngine().getFontManager(), this);
        
        mLoading = new LoadingShape(0, 0, mCameraWidth, mCameraHeight);
        mLoading.loadResources(getEngine().getTextureManager(), getEngine().getFontManager(), this);
    }

    /* 
     * @see org.anddev.andengine.ui.IGameInterface#onLoadScene()
     */
    @Override
    public Scene onLoadScene() {

        final Scene scene = new Scene(2);
        scene.setBackground(new ColorBackground((float) ( 51.0 / 255.0 ),
                (float) ( 189.0 / 255.0 ), (float) ( 200.0 / 255.0 )));

        mLevelBar.init(false, null, null);
        scene.getLastChild().attachChild(mLevelBar);

        mMaze.init(false, 
                new Callback<Boolean>(){

                    public void onCallback(Boolean pCallbackValue) {
                        mLoading.setVisible(false);
                        scene.getFirstChild().detachChild(mLoading);
                    	mLevelBar.enable(getEngine());
                        mMaze.enable(getEngine());
                    }
            
                }, null);
        scene.getLastChild().attachChild(mMaze);
        
        mLoading.init(true, null, null);
        scene.getFirstChild().attachChild(mLoading);

        return scene;
    }
    
    /* 
     * @see org.anddev.andengine.ui.IGameInterface#onLoadComplete()
     */
    @Override
    public void onLoadComplete() {
        
    }
    
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
}
