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

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.BaseRectangle;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.font.FontManager;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;


/**
 * Shape implementation of the level bar displaying time elapsed and current level.
 * 
 * @author Massimo Gaddini
 *
 */
public class LevelBarShape extends BaseRectangle implements ComplexShape{
    
    /** 
     * Elapsed seconds displayed in the level bar
     */
    private int  mElapsedSeconds = 0;
    
  
    /**
     * Font used for text in the level bar
     */
    private Font mDroidFont;

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
        Texture droidFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
 
        /* Font */
        FontFactory.setAssetBasePath("font/");
        mDroidFont = FontFactory.createFromAsset(droidFontTexture, ctx, "Droid.ttf", getHeightScaled()/2, true, Color.BLACK);
        
        textureManager.loadTexture(droidFontTexture);
        fontManager.loadFont(mDroidFont);
    }

    /* 
     * @see com.sgxmobileapps.droidmaze.ui.shape.ComplexShape#initShape()
     */
    public void initShape() {
        /* transparent white */
        setColor(1.0f, 1.0f, 1.0f, 0.0f);
        
        /* label for elapsed time */
        final ChangeableText elapsedText = new ChangeableText(0, (getHeightScaled() - mDroidFont.getLineHeight())/2, mDroidFont, "00:00", "XXXXX".length());
        attachChild(elapsedText);
        
        /* timer handler for updating the elapsed time label */
        registerUpdateHandler(new TimerHandler(1.0f, true, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mElapsedSeconds += (int)pTimerHandler.getTimerSeconds();
                elapsedText.setText(String.format("%02d:%02d", mElapsedSeconds/60, mElapsedSeconds%60));
            }
        }));
    }
}
