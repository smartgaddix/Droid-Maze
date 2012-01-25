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
package com.sgxmobileapps.droidmaze.ui.shape;

import android.content.Context;
import android.graphics.Color;

import com.sgxmobileapps.droidmaze.game.GameLevelManager;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.BaseRectangle;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.font.FontManager;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.util.Callback;
import org.anddev.andengine.util.HorizontalAlign;

import javax.microedition.khronos.opengles.GL10;


/**
 * Shape implementation of the level bar displaying time elapsed and current level.
 * 
 * @author Massimo Gaddini
 *
 */
public class LevelBarShape extends Rectangle implements ComplexShape{
    
    /** 
     * Elapsed seconds displayed in the level bar
     */
    private long mElapsedSeconds = 0;
    
    /**
     * Text label for elapsed time
     */
    private ChangeableText mElapsedText;
    
    /**
     * Text label for total time
     */
    private ChangeableText mTotalText;
    
    /**
     * Text label for level number
     */
    private Text mLevelText;
    
    /*
     * Game level manager
     */
    private GameLevelManager mGameLevelManager; 
    
    /**
     * Font used for text of time in the level bar
     */
    private Font mTimeFont;
    
    /**
     * Font used for text of level number in the level bar
     */
    private Font mFontLevel;

    
    
    /**
     * Creates a LevelBarShape class
     * @param pX 
     * @param pY
     * @param pWidth
     * @param pHeight
     */
    public LevelBarShape(float pX, float pY, float pWidth, float pHeight) {
        super(pX, pY, pWidth, pHeight);
    }


    /* 
     * @see com.sgxmobileapps.droidmaze.ui.shape.ComplexShape#loadResources(org.anddev.andengine.opengl.texture.TextureManager, org.anddev.andengine.opengl.font.FontManager, android.content.Context)
     */
    public void loadResources(TextureManager textureManager, FontManager fontManager, Context ctx) {
        /* Texture */
        BitmapTextureAtlas droidFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        BitmapTextureAtlas levelFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
 
        /* Font */
        FontFactory.setAssetBasePath("font/");
        mTimeFont = FontFactory.createFromAsset(droidFontTexture, ctx, "Droid.ttf", getHeightScaled()/2, true, Color.BLACK);
        
        mFontLevel = FontFactory.createFromAsset(levelFontTexture, ctx, "Droid.ttf", getHeightScaled(), true, Color.BLACK);
        
        textureManager.loadTextures(droidFontTexture, levelFontTexture);
        fontManager.loadFonts(mTimeFont, mFontLevel);
    }

    /* (non-Javadoc)
     * @see com.sgxmobileapps.droidmaze.ui.shape.ComplexShape#init(boolean, org.anddev.andengine.util.Callback, org.anddev.andengine.util.Callback, android.content.Context)
     */
    public void init(boolean visible, Callback<Boolean> callback, Callback<Exception> exceptionCallback, Context ctx) {
        setVisible(visible);
        
        mGameLevelManager = GameLevelManager.getInstance(ctx);
                
//        /* ground black line */
//        Line ground = new Line(0, getHeightScaled()-2, getWidthScaled(), getHeightScaled()-2);
//        ground.setLineWidth(4);
//        ground.setColor(0.0f, 0.0f, 0.0f, 1.0f);
//        attachChild(ground);
        
        /* label for elapsed time */
        mElapsedText = new ChangeableText(0, (getHeightScaled() - mTimeFont.getLineHeight())/2, mTimeFont, "00:00", "XXXXX".length());
        attachChild(mElapsedText);
        
        /* label for level */
        String levelText = String.format("%02d", (int)(Math.random()*50)/*mGameLevelManager.getLevel()*/);
        mLevelText = new Text((getWidthScaled() - mFontLevel.getStringWidth("XX"))/2, 
                (getHeightScaled() - mFontLevel.getLineHeight())/2, 
                mFontLevel, levelText);
        attachChild(mLevelText);
        
        /* label for total time */
        mTotalText = new ChangeableText(getWidthScaled() - mTimeFont.getStringWidth("XX:XX"), (getHeightScaled() - mTimeFont.getLineHeight())/2, mTimeFont, "00:00", "XXXXX".length());
        attachChild(mTotalText);
        
        if (callback != null) {
            callback.onCallback(true);
        }
    }


    /* (non-Javadoc)
     * @see com.sgxmobileapps.droidmaze.ui.shape.ComplexShape#enable(org.anddev.andengine.engine.Engine)
     */
    public void enable(Engine engine) {
        setVisible(true);
        
        /* timer handler for updating the elapsed time label */
        engine.registerUpdateHandler(new TimerHandler(1.0f, true, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mElapsedSeconds += (long)pTimerHandler.getTimerSeconds();
                mElapsedText.setText(String.format("%02d:%02d", mElapsedSeconds/60, mElapsedSeconds%60));
                
                long totalTime = mGameLevelManager.getTotalTime() + mElapsedSeconds;
                
                mTotalText.setText(String.format("%02d:%02d", totalTime/60, totalTime%60));
            }
        }));
    } 
}
