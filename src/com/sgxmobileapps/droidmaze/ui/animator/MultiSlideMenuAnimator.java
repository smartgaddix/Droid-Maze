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
package com.sgxmobileapps.droidmaze.ui.animator;

import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.util.HorizontalAlign;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

import java.util.ArrayList;


/**
 * @author Massimo Gaddini
 *
 */
public class MultiSlideMenuAnimator extends SlideMenuAnimator {
    
    private float mDuration = 1.0f;

    /**
     * 
     */
    public MultiSlideMenuAnimator() {
        super();
    }

    /**
     * @param pMenuItemSpacing
     * @param pEaseFunction
     */
    public MultiSlideMenuAnimator(float pMenuItemSpacing, IEaseFunction pEaseFunction) {
        super(pMenuItemSpacing, pEaseFunction);
    }

    /**
     * @param pMenuItemSpacing
     */
    public MultiSlideMenuAnimator(float pMenuItemSpacing) {
        super(pMenuItemSpacing);
    }

    /**
     * @param pHorizontalAlign
     * @param pMenuItemSpacing
     * @param pEaseFunction
     */
    public MultiSlideMenuAnimator(HorizontalAlign pHorizontalAlign, float pMenuItemSpacing,
            IEaseFunction pEaseFunction) {
        super(pHorizontalAlign, pMenuItemSpacing, pEaseFunction);
    }

    /**
     * @param pHorizontalAlign
     * @param pMenuItemSpacing
     */
    public MultiSlideMenuAnimator(HorizontalAlign pHorizontalAlign, float pMenuItemSpacing) {
        super(pHorizontalAlign, pMenuItemSpacing);
    }

    /**
     * @param pHorizontalAlign
     * @param pEaseFunction
     */
    public MultiSlideMenuAnimator(HorizontalAlign pHorizontalAlign, IEaseFunction pEaseFunction) {
        super(pHorizontalAlign, pEaseFunction);
    }

    /**
     * @param pHorizontalAlign
     */
    public MultiSlideMenuAnimator(HorizontalAlign pHorizontalAlign) {
        super(pHorizontalAlign);
    }

    /**
     * @param pEaseFunction
     */
    public MultiSlideMenuAnimator(IEaseFunction pEaseFunction) {
        super(pEaseFunction);
    }
    
    /**
     * @param duration the duration to set
     */
    public void setDuration(float duration) {
        mDuration = duration;
    }
    
    protected float getMaximumHeight(ArrayList<IMenuItem> pMenuItems) {
        float maximumHeight = Float.MIN_VALUE;
        for(int i = pMenuItems.size() - 1; i >= 0; i--) {
            IMenuItem menuItem = pMenuItems.get(i);
            maximumHeight = Math.max(maximumHeight, menuItem.getHeightScaled());
        }
        return maximumHeight;
    }
    
    @Override
    public void buildAnimations(ArrayList<IMenuItem> pMenuItems, float pCameraWidth, float pCameraHeight) {
        float maximumWidth = getMaximumWidth(pMenuItems);
        float maximumHeight = getMaximumHeight(pMenuItems);
        float overallHeight = getOverallHeight(pMenuItems);

        float baseX = (pCameraWidth - maximumWidth) * 0.5f;
        float baseY = (pCameraHeight - overallHeight) * 0.5f;

        float offsetY = 0;
        int menuItemCount = pMenuItems.size();
        for(int i = 0; i < menuItemCount; i++) {
            IMenuItem menuItem = pMenuItems.get(i);

            float offsetX;
            switch(mHorizontalAlign) {
                case LEFT:
                    offsetX = 0;
                    break;
                case RIGHT:
                    offsetX = maximumWidth - menuItem.getWidthScaled();
                    break;
                case CENTER:
                default:
                    offsetX = (maximumWidth - menuItem.getWidthScaled()) * 0.5f;
                    break;
            }
            
            if (i%2 == 0) {
                MoveModifier moveModifier = new MoveModifier(mDuration, baseX + offsetX, baseX + offsetX, -maximumHeight, baseY + offsetY, mEaseFunction);
                moveModifier.setRemoveWhenFinished(false);
                menuItem.registerEntityModifier(moveModifier);
            } else {
                MoveModifier moveModifier = new MoveModifier(mDuration, baseX + offsetX, baseX + offsetX, pCameraHeight+maximumHeight, baseY + offsetY, mEaseFunction);
                moveModifier.setRemoveWhenFinished(false);
                menuItem.registerEntityModifier(moveModifier);
            }

            offsetY += menuItem.getHeight() + mMenuItemSpacing;
        }
    }

    @Override
    public void prepareAnimations(ArrayList<IMenuItem> pMenuItems, float pCameraWidth, float pCameraHeight) {
        float maximumWidth = getMaximumWidth(pMenuItems);
        float maximumHeight = getMaximumHeight(pMenuItems);
        float baseX = (pCameraWidth - maximumWidth) * 0.5f;

        int menuItemCount = pMenuItems.size();
        for(int i = 0; i < menuItemCount; i++) {
            IMenuItem menuItem = pMenuItems.get(i);
            
            float offsetX;
            switch(mHorizontalAlign) {
                case LEFT:
                    offsetX = 0;
                    break;
                case RIGHT:
                    offsetX = maximumWidth - menuItem.getWidthScaled();
                    break;
                case CENTER:
                default:
                    offsetX = (maximumWidth - menuItem.getWidthScaled()) * 0.5f;
                    break;
            }

            menuItem.setPosition(baseX + offsetX, -maximumHeight);
        }
    }
}
