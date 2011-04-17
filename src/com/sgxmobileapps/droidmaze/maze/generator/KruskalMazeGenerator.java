package com.sgxmobileapps.droidmaze.maze.generator;

import com.sgxmobileapps.droidmaze.maze.MazeCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.Vector;

public class KruskalMazeGenerator implements MazeGenerator {

	class CellTree {
	  CellTree mParent;

	  public CellTree() {
		  mParent = null;
	  }
	  
	  public CellTree getRoot(){
		  if (mParent == null)
			  return this;
		  
		  return mParent.getRoot(); 
	  }
	  
	  public boolean contains(CellTree other) {
		  return (getRoot() == other.getRoot());
	  }
	  
	  public void join(CellTree other){
		  other.getRoot().mParent = this;
	  }
	}

/*	
	class KruskalCell extends MazeCell implements Comparable<KruskalCell> {
		private TreeSet<KruskalCell> mSet;
        
		KruskalCell(int x, int y){
            super(x,y);
        }
		
		public void init(){
			mSet = new TreeSet<KruskalCell>();
            mSet.add(this);
		}
		
		public boolean joinToSet(KruskalCell otherCell) {
			if (!mSet.containsAll(otherCell.mSet)) {
				mSet.addAll(otherCell.mSet);
				otherCell.mSet.addAll(mSet);
				otherCell.mSet = mSet;
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
*/	
	class KruskalCell extends MazeCell {
		private CellTree mSet;
        
		KruskalCell(int x, int y){
            super(x,y);
        }
		
		public void init(){
			mSet = new CellTree();
   		}
		
		public boolean joinTree(KruskalCell otherCell) {
			if (!mSet.contains(otherCell.mSet)) {
				mSet.join(otherCell.mSet);
				return true;
			}
			
			return false;
		}
    }
	
	private KruskalCell[][] mGrid = null;
	List<Integer> mWalls = null;
	private Random mRandom = new Random();
	private int mHeight = 0;
	private int mWidth = 0;

	private void init(int height, int width){

		mRandom.setSeed(System.currentTimeMillis());

		if ((height != mHeight) || (width != mWidth)) {
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
		
		Integer[] walls = new Integer[height*(width-1) + width*(height-1)];
		for (int i = 0; i < walls.length; i++) {
			walls[i] = i;
		}
				
		mWalls = Arrays.asList(walls);
		Collections.shuffle(mWalls, mRandom);

		this.mHeight = height;
		this.mWidth = width;
	}

	public MazeCell[][] generate(int height, int width){
		int i, j, is, js;
		
		init(height, width);
		
		for (Integer wall: mWalls) {
			i = wall / (2*mWidth - 1);
			j = wall % (2*mWidth - 1);
			
			if (j >= (mWidth - 1)) {
				/* bottom wall */
				is = i+1;
				j -= (mWidth - 1);
				js = j;
			} else {
				is = i;
				js = j+1;
			}
			
			if ( (is >= height) || (js >= width) )
				continue;
			
			if (mGrid[i][j].joinTree(mGrid[is][js])) {
				mGrid[i][j].openTo(mGrid[is][js]);
				mGrid[is][js].openTo(mGrid[i][j]);
			}			
		}
		
		return mGrid;
	}
}