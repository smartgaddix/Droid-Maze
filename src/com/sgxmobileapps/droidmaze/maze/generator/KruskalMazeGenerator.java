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
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * @author Massimo Gaddini
 */
public class KruskalMazeGenerator implements MazeGenerator {
    
    class KruskalCell extends MazeCell implements Comparable<KruskalCell> {

        private TreeSet<KruskalCell> mSet;

        KruskalCell(int x, int y) {
            super(x, y);
        }

        public void init() {
            mSet = new TreeSet<KruskalCell>();
            mSet.add(this);
        }

        public boolean joinSet(KruskalCell otherCell) {
            if (!mSet.contains(otherCell)) {
                mSet.addAll(otherCell.mSet);
                
                Iterator<KruskalCell> it = otherCell.mSet.iterator();
                while (it.hasNext()) {
                    KruskalCell cell = it.next();
                    cell.mSet = mSet;
                }
                
                return true;
            }

            return false;
        }

        public int compareTo(KruskalCell another) { 
        	if (getX() < another.getX())
        		return -1; 
        	else if (getX() > another.getX()) 
        		return 1; 
        	else { 
        		if (getY() < another.getY()) 
        			return -1; 
        		else if (getY() > another.getY()) 
        			return 1;
        		} 
        	return 0; 
        }
         
        @Override 
        public boolean equals(Object obj) { 
        	if (this == obj) 
        		return true;
         
        	if (obj == null) 
        		return false;
         
        	if (getClass() != obj.getClass()) 
        		return false;
         
        	KruskalCell other = (KruskalCell) obj;
         
        	if ((getX() == other.getX()) && (getY() == other.getY()) ) 
        		return true;
 
        	return false; 
        } 
    }

    private KruskalCell[][] mGrid   = null;
    List<Integer>           mWalls  = null;
    private Random          mRandom = new Random();
    private int             mHeight = 0;
    private int             mWidth  = 0;

    private void init(int height, int width) {

        mRandom.setSeed(System.currentTimeMillis());

        if ( ( height != mHeight ) || ( width != mWidth )) {
            mGrid = new KruskalCell[height][width];
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (mGrid[i][j] == null) {
                    mGrid[i][j] = new KruskalCell(i, j);
                }

                mGrid[i][j].init();
            }
        }

        Integer[] walls = new Integer[height * ( width - 1 ) + width * ( height - 1 )];
        for (int i = 0; i < walls.length; i++) {
            walls[i] = i;
        }

        mWalls = Arrays.asList(walls);
        Collections.shuffle(mWalls, mRandom);

        this.mHeight = height;
        this.mWidth = width;
    }

    public MazeCell[][] generate(int height, int width) {
        int i, j, is, js;

        init(height, width);

        for (Integer wall : mWalls) {
            i = wall / ( 2 * mWidth - 1 );
            j = wall % ( 2 * mWidth - 1 );

            if (j >= ( mWidth - 1 )) {
                /* south wall */
                is = i + 1;
                j -= ( mWidth - 1 );
                js = j;
            } else {
                is = i;
                js = j + 1;
            }

            if ( ( is >= height ) || ( js >= width )) {
                continue;
            }

            if (mGrid[i][j].joinSet(mGrid[is][js])) {
                mGrid[i][j].openTo(mGrid[is][js]);
                mGrid[is][js].openTo(mGrid[i][j]);
            }
        }

        return mGrid;
    }
}