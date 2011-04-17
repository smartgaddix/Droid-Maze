/**
 * 
 */
package com.sgxmobileapps.droidmaze.maze;

/**
 * @author massimo
 *
 */
public abstract class MazeCell {
    
    public static final byte NORTH_WALL = 0x01;
    public static final byte EAST_WALL = 0x02;
    public static final byte SOUTH_WALL = 0x04;
    public static final byte WEST_WALL = 0x08;

    private byte mCarvedWalls;
    private int mX, mY;
    
    public MazeCell(int x, int y){
        mX = x;
        mY = y;
        mCarvedWalls = 0;
    }
    
    public int getX(){
        return mX;
    }
    
    public int getY(){
        return mY;
    }
    
    public byte getOppositeWall(byte wall) {
        if (wall >= 0x04)
            return (byte)(wall / 4);

        return (byte)(wall * 4);
    }

    public void openTo(MazeCell to){

        if (mX < to.mX)
            mCarvedWalls |= SOUTH_WALL;

        if (mX > to.mX)
            mCarvedWalls |= NORTH_WALL;

        if (mY < to.mY)
            mCarvedWalls |= EAST_WALL;

        if (mY > to.mY)
            mCarvedWalls |= WEST_WALL;
    }
    
    public boolean isClosed(){
        return (mCarvedWalls == 0);
    }
    
    public boolean isEastWallClosed(){
        return (mCarvedWalls & EAST_WALL) == 0;
    }

    public boolean isSouthWallClosed(){
        return (mCarvedWalls & SOUTH_WALL) == 0;
    }
}
