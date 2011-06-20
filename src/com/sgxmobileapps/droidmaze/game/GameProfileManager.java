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

package com.sgxmobileapps.droidmaze.game;

import com.sgxmobileapps.droidmaze.maze.generator.IterativeBacktrackingMazeGenerator;
//import com.sgxmobileapps.droidmaze.maze.generator.KruskalMazeGenerator;
import com.sgxmobileapps.droidmaze.maze.generator.MazeGenerator;

/**
 * TODO
 * @author Massimo Gaddini
 * 
 */
public class GameProfileManager {

    private String        mNickname;
    private int           mLevel;
    private MazeGenerator mGenerator = null;
    private int           mMazeWidth;
    private int           mMazeHeight;
    private float         mAccelerationFactor;

    public GameProfileManager() {
        /* TODO: load state from db */

        mNickname = "Smartgaddix";
        mLevel = 1;
        mGenerator = new IterativeBacktrackingMazeGenerator();
        mMazeHeight = 40;
        mMazeWidth = 40;
        mAccelerationFactor = (float) 0.5;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return mNickname;
    }

    /**
     * @param nickname
     *            the nickname to set
     */
    public void setNickname(String nickname) {
        mNickname = nickname;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return mLevel;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level) {
        mLevel = level;
    }

    /**
     * @return the generator
     */
    public MazeGenerator getGenerator() {
        return mGenerator;
    }

    /**
     * @param generator
     *            the generator to set
     */
    public void setGenerator(MazeGenerator generator) {
        mGenerator = generator;
    }

    /**
     * @return the mazeWidth
     */
    public int getMazeWidth() {
        return mMazeWidth;
    }

    /**
     * @param mazeWidth
     *            the mazeWidth to set
     */
    public void setMazeWidth(int mazeWidth) {
        mMazeWidth = mazeWidth;
    }

    /**
     * @return the mazeHeight
     */
    public int getMazeHeight() {
        return mMazeHeight;
    }

    /**
     * @param mazeHeight
     *            the mazeHeight to set
     */
    public void setMazeHeight(int mazeHeight) {
        mMazeHeight = mazeHeight;
    }

    /**
     * @return the accelerationFactor
     */
    public float getAccelerationFactor() {
        return mAccelerationFactor;
    }

    /**
     * @param accelerationFactor
     *            the accelerationFactor to set
     */
    public void setAccelerationFactor(float accelerationFactor) {
        mAccelerationFactor = accelerationFactor;
    }
}
