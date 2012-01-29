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


import java.util.Random;
import java.util.Vector;

/**
 * @author Massimo Gaddini
 *
 */
public class PrimMazeGenerator implements MazeGenerator {

    class PrimCell extends MazeCell {

        private static final byte CELL_IN       = 0x10;
        private static final byte CELL_FRONTIER = 0x20;

        byte                      mState;

        PrimCell(int x, int y) {
            super(x, y);
        }
        
        public void init() {
            super.init();
            mState = 0;
        }

        boolean isOut() {
            return mState == 0;
        }

        boolean isIn() {
            return ( mState & CELL_IN ) != 0;
        }

        void setFrontier() {
            mState |= CELL_FRONTIER;
        }

        void setIn() {
            mState |= CELL_IN;
        }
    }

    private PrimCell[][]     mGrid      = null;
    private Vector<PrimCell> mFrontier  = new Vector<PrimCell>();
    private Vector<PrimCell> mNeighbors = new Vector<PrimCell>();
    private Random           mRandom    = new Random();
    private int              mHeight    = 0;
    private int              mWidth     = 0;

    private void init(int height, int width) {

        mRandom.setSeed(System.currentTimeMillis());

        mFrontier.clear();

        if ( ( height != mHeight ) || ( width != mWidth )) {
            mGrid = new PrimCell[height][width];

        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (mGrid[i][j] == null) {
                    mGrid[i][j] = new PrimCell(i, j);
                }

                mGrid[i][j].init();
            }
        }

        this.mHeight = height;
        this.mWidth = width;
    }

    private void addFrontierCell(int x, int y) {
        if ( ( x >= 0 ) && ( y >= 0 ) && ( y < mWidth ) && ( x < mHeight )
                && ( mGrid[x][y].isOut() )) {
            mGrid[x][y].setFrontier();
            mFrontier.add(mGrid[x][y]);
        }
    }

    private void markInCell(int x, int y) {
        mGrid[x][y].setIn();
        addFrontierCell(x - 1, y);
        addFrontierCell(x + 1, y);
        addFrontierCell(x, y - 1);
        addFrontierCell(x, y + 1);
    }

    private void findNeighbors(int x, int y) {
        mNeighbors.clear();

        if ( ( x > 0 ) && ( mGrid[x - 1][y].isIn() )) {
            mNeighbors.add(mGrid[x - 1][y]);
        }

        if ( ( ( x + 1 ) < mHeight ) && ( mGrid[x + 1][y].isIn() )) {
            mNeighbors.add(mGrid[x + 1][y]);
        }

        if ( ( y > 0 ) && ( mGrid[x][y - 1].isIn() )) {
            mNeighbors.add(mGrid[x][y - 1]);
        }

        if ( ( ( y + 1 ) < mWidth ) && ( mGrid[x][y + 1].isIn() )) {
            mNeighbors.add(mGrid[x][y + 1]);
        }
    }

    public MazeCell[][] generate(int height, int width) {
        init(height, width);

        markInCell(mRandom.nextInt(height * width) / width, mRandom.nextInt(width * height) % width);
        while (!mFrontier.isEmpty()) {
            int currIndex = mRandom.nextInt(mFrontier.size());
            PrimCell curr = mFrontier.get(currIndex);
            mFrontier.remove(currIndex);

            findNeighbors(curr.getX(), curr.getY());

            PrimCell currNeighbor = mNeighbors.get(mRandom.nextInt(mNeighbors.size()));

            curr.openTo(currNeighbor);
            currNeighbor.openTo(curr);

            markInCell(curr.getX(), curr.getY());
        }

        return mGrid;
    }
}