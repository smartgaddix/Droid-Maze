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


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Massimo Gaddini
 */
public class BacktrackingMazeGenerator implements MazeGenerator {

    class BacktrackingCell extends MazeCell {

        List<Byte> mDirections;

        BacktrackingCell(int x, int y) {
            super(x, y);
            mDirections = Arrays.asList(NORTH_WALL, EAST_WALL, SOUTH_WALL, WEST_WALL);
        }

        void init(Random random) {
            super.init();
            Collections.shuffle(mDirections, random);
        }
    }

    private BacktrackingCell[][] mGrid   = null;
    private Random               mRandom = new Random();
    private int                  mHeight = 0;
    private int                  mWidth  = 0;

    private void init(int height, int width) {

        mRandom.setSeed(System.currentTimeMillis());

        if ( ( height != mHeight ) || ( width != mWidth )) {
            mGrid = new BacktrackingCell[height][width];
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (mGrid[i][j] == null) {
                    mGrid[i][j] = new BacktrackingCell(i, j);
                }

                mGrid[i][j].init(mRandom);
            }
        }

        this.mHeight = height;
        this.mWidth = width;
    }

    private BacktrackingCell getNextCell(BacktrackingCell cell, Byte direction) {
        if (direction == MazeCell.NORTH_WALL) {
            if ( ( cell.getX() - 1 ) >= 0) {
                return mGrid[cell.getX() - 1][cell.getY()];
            }
        } else if (direction == MazeCell.EAST_WALL) {
            if ( ( cell.getY() + 1 ) < mWidth) {
                return mGrid[cell.getX()][cell.getY() + 1];
            }
        } else if (direction == MazeCell.SOUTH_WALL) {
            if ( ( cell.getX() + 1 ) < mHeight) {
                return mGrid[cell.getX() + 1][cell.getY()];
            }
        } else if (direction == MazeCell.WEST_WALL) {
            if ( ( cell.getY() - 1 ) >= 0) {
                return mGrid[cell.getX()][cell.getY() - 1];
            }
        }

        return null;
    }

    private void carveWallFrom(BacktrackingCell currCell) {
        BacktrackingCell nextCell;

        for (Byte wall : currCell.mDirections) {
            nextCell = getNextCell(currCell, wall);
            if ( ( nextCell != null ) && nextCell.isClosed()) {
                currCell.openTo(nextCell);
                nextCell.openTo(currCell);

                carveWallFrom(nextCell);
            }
        }
    }

    /*
     * @see
     * com.sgxmobileapps.droidmaze.maze.generator.MazeGenerator#generate(int,
     * int)
     */
    public MazeCell[][] generate(int height, int width) {
        init(height, width);
        carveWallFrom(mGrid[mRandom.nextInt(height)][mRandom.nextInt(width)]);
        return mGrid;
    }

}
