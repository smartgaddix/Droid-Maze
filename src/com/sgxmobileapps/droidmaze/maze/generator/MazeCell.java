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
package com.sgxmobileapps.droidmaze.maze.generator;

/**
 * @author Massimo Gaddini
 */
public abstract class MazeCell {

    public static final byte NORTH_WALL = 0x01;
    public static final byte EAST_WALL  = 0x02;
    public static final byte SOUTH_WALL = 0x04;
    public static final byte WEST_WALL  = 0x08;

    private byte             mCarvedWalls;
    private int              mX, mY;

    public MazeCell(int x, int y) {
        mX = x;
        mY = y;
        mCarvedWalls = 0;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public byte getOppositeWall(byte wall) {
        if (wall >= 0x04) {
            return (byte) ( wall / 4 );
        }

        return (byte) ( wall * 4 );
    }

    public void openTo(MazeCell to) {

        if (mX < to.mX) {
            mCarvedWalls |= SOUTH_WALL;
        }

        if (mX > to.mX) {
            mCarvedWalls |= NORTH_WALL;
        }

        if (mY < to.mY) {
            mCarvedWalls |= EAST_WALL;
        }

        if (mY > to.mY) {
            mCarvedWalls |= WEST_WALL;
        }
    }

    public boolean isClosed() {
        return ( mCarvedWalls == 0 );
    }

    public boolean isEastWallClosed() {
        return ( mCarvedWalls & EAST_WALL ) == 0;
    }

    public boolean isSouthWallClosed() {
        return ( mCarvedWalls & SOUTH_WALL ) == 0;
    }
}
