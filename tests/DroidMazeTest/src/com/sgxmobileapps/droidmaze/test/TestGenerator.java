package com.sgxmobileapps.droidmaze.test;

import com.sgxmobileapps.droidmaze.maze.Maze;
import com.sgxmobileapps.droidmaze.maze.generator.BacktrackingMazeGenerator;
import com.sgxmobileapps.droidmaze.maze.generator.PrimMazeGenerator;

import android.test.AndroidTestCase;

public class TestGenerator extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testPrimGenerator() {
		Maze maze = new Maze();
		
		maze.setMazeSize(20, 20);
		maze.setMazeGenerator(new PrimMazeGenerator());
		
		maze.generateMaze();
	}
	
	public void testBacktrackingGenerator() {
        Maze maze = new Maze();
        
        maze.setMazeSize(30, 30);
        maze.setMazeGenerator(new BacktrackingMazeGenerator());
        
        maze.generateMaze();
    }
}
