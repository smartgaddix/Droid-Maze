/**
 * 
 */
package com.sgxmobileapps.droidmaze.game;

import com.sgxmobileapps.droidmaze.maze.generator.BacktrackingMazeGenerator;
import com.sgxmobileapps.droidmaze.maze.generator.KruskalMazeGenerator;
import com.sgxmobileapps.droidmaze.maze.generator.MazeGenerator;
import com.sgxmobileapps.droidmaze.maze.generator.PrimMazeGenerator;

/**
 * @author massimo
 *
 */
public class GameProfileManager {
	
    private String mNickname;
	private int	mLevel;
	private MazeGenerator mGenerator = null;
	private int mMazeWidth;
	private int mMazeHeight;
	private float mAccelerationFactor;
	
	public GameProfileManager(){
		/* TODO: load state from db */
		
	    mNickname = "Smartgaddix";
		mLevel = 1;
		mGenerator = new KruskalMazeGenerator();
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
     * @param nickname the nickname to set
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
     * @param level the level to set
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
     * @param generator the generator to set
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
     * @param mazeWidth the mazeWidth to set
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
     * @param mazeHeight the mazeHeight to set
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
     * @param accelerationFactor the accelerationFactor to set
     */
    public void setAccelerationFactor(float accelerationFactor) {
        mAccelerationFactor = accelerationFactor;
    }
}
